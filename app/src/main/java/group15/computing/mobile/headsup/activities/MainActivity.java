package group15.computing.mobile.headsup.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.beacon_detection.BeaconEvent;
import group15.computing.mobile.headsup.utilities.APIClient;
import group15.computing.mobile.headsup.utilities.Constants;
import group15.computing.mobile.headsup.utilities.SmartTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Used for debugging purposes.
    private IntentFilter urlFilter;
    private BroadcastReceiver urlReceiver;
    private Timer rangingTimer;
    private SmartTask onTask;
    private SmartTask offTask;
    private final int searchPeriod = 30000; // Search for 15 seconds on, 15 off.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the timer and url broadcast receiever.
        createUrlListener();

        // Create the timer tasks
        onTask = new SmartTask() {
            @Override
            public void run() {
                super.run();
                startRanging();
                Log.d(TAG, "Searching for beacons.");
            }
        };
        offTask = new SmartTask() {
            @Override
            public void run() {
                super.run();
                Log.d(TAG, "Stopped searching for beacons");
                stopRanging();
            }
        };

        if(!Authentication.getInstance().isSignedIn()){
            Log.d(TAG, "IS NOT LOGGED IN");
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        rangingTimer = new Timer("rangingTimer", true);

        if(Authentication.getInstance().isSignedIn()){
            searchForBeacons();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSearching();
        rangingTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSearching();
        rangingTimer.cancel();
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
        urlFilter = new IntentFilter(Constants.URL_FOUND);
        urlReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "RECEIVED URL");
                String url = intent.getStringExtra(BeaconEvent.DATA);
                makeToast("Beacon Found!");
//                makeToast("Url " + url + " found.");
                makeRequestToUrl(url);
            }
        };
    }

    private void searchForBeacons(){

        // Scheduele a task that starts ranging immediately.
        if(!onTask.isRunning()){
            rangingTimer.scheduleAtFixedRate(onTask, 0, searchPeriod);
        }

        // Schedule a task that starts halfway through the period.
        if(!onTask.isRunning()){
            rangingTimer.scheduleAtFixedRate(offTask, searchPeriod/2, searchPeriod);
        }
    }

    private void stopSearching(){
        onTask.cancel();
        offTask.cancel();
        stopRanging();
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
        registerReceiver(urlReceiver, urlFilter);
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
            unregisterReceiver(urlReceiver);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void makeRequestToUrl(String url){

        // TODO: Perhaps parse the url to know what to do with it. For testing purposes, I am
        // TODO: just making making a get request on that url.
        APIClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // Get the data from the response
                    JSONObject data = response.getJSONObject("data");

                    // TODO: We will need to make classes to deserialize the data into and do stuff with.
                    Log.d(TAG, data.toString());
                    makeToast("GOT THE DATA :)");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, "Failed to retrieve data.");
//                makeToast("Failed to retrieve data.");
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
