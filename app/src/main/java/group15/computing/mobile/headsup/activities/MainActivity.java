package group15.computing.mobile.headsup.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.beacon_detection.BeaconEvent;
import group15.computing.mobile.headsup.beacon_detection.RequestedAction;
import group15.computing.mobile.headsup.utilities.APIClient;
import group15.computing.mobile.headsup.utilities.Constants;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Used for debugging purposes.
    private IntentFilter uidFilter;
    private BroadcastReceiver uidReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the timer and url broadcast receiever.
        createUrlListener();

        if(!Authentication.getInstance().isSignedIn()){
            Log.d(TAG, "IS NOT LOGGED IN");
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        TextView description = (TextView) findViewById(R.id.main_description);
        if(Authentication.getInstance().isSignedIn()){
            description.setText("Don't worry! We're looking for spaces now.");
            startRanging();
        }else{
            description.setText("Tap to sign in.");
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRanging();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRanging();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createUrlListener(){
        // Register the Url broadcast listener
        uidFilter = new IntentFilter(Constants.UID_FOUND);
        uidReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "RECEIVED URL");
                String url = intent.getStringExtra(BeaconEvent.DATA);
                makeToast("Beacon Found!");
                makeRequestToUrl(url);
            }
        };
    }


    private void startRanging(){
        // Start ranging.
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region(Constants.ALL_BEACONS_RANGE_ID, null, null, null);
        try{
            beaconManager.startRangingBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        // Register the receiver.
        registerReceiver(uidReceiver, uidFilter);
    }

    private void stopRanging(){
        // Stop ranging.
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region(Constants.ALL_BEACONS_RANGE_ID, null, null, null);
        try{
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        // Unregister the receiver
        try{
            unregisterReceiver(uidReceiver);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void makeRequestToUrl(String uri){

        APIClient.hitBeacon(uri, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(TAG, "Failed to retrieve data.");

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                // Get the data from the response
                String requestedAction = response.optString("action");
                Log.d(TAG, "THE ACTION IS " + requestedAction);
                RequestedAction action = RequestedAction.valueOf(requestedAction);
                action.execute(MainActivity.this, response);
                stopRanging(); // TODO: Dont necesarilly stop ranging. For debugging this makes things easier.

            }
        });
    }

    private void makeToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
