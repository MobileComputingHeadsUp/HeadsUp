package group15.computing.mobile.headsup.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import group15.computing.mobile.headsup.beacon_detection.BeaconEvent;
import group15.computing.mobile.headsup.utilities.Constants;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.utilities.APIClient;

/**
 * Created by Bradley on 3/25/2016.
 */
public class BeaconRangingActivity extends Activity {

    private static final String TAG = "BeaconMonitoring"; // Used for debugging purposes.
    private TextView webResponseTV;

    // We want to pick up whenever we receive a URL.
    private BroadcastReceiver urlReceiver;
    private IntentFilter urlFilter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_monitoring);

        // TODO: Remove after debugging. For now display the json data so we know we got it Eeeiw.
        webResponseTV = (TextView) findViewById(R.id.webResponseTV);

        // Setup up the receiver to get Urls.
        urlFilter = new IntentFilter(Constants.URL_FOUND);
        urlReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "RECEIVED URL");
                String url = intent.getStringExtra(BeaconEvent.DATA);
                makeToast("Url " + url + " found.");
                makeRequestToUrl(url);

                // Stop searching cuz we got it!
                unregisterUrlReceiver();
                stopRanging();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the broadcast receiver
        registerUrlReceiver();

        // Start looking for beacons!
        startRanging();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the broadcast receiver.
        unregisterUrlReceiver();

        // Stop looking for beacons
        stopRanging();

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
                    // TODO: For now, I'm just going to display the data as a string for testing purposes.
                    Log.d(TAG, data.toString());
                    webResponseTV.setText(data.toString());
                    makeToast("GOT THE DATA :)");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Toasts have to be made on the main thread so gotta make this little wrapper to create them elsewhere.
    private void makeToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BeaconRangingActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUrlReceiver(){
        registerReceiver(urlReceiver, urlFilter);
    }

    private void unregisterUrlReceiver(){

        // Unregistering a receiver that has already been unregistered will throw an exception.
        // Kinda stupid but this is a workaround.
        try{
            unregisterReceiver(urlReceiver);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void startRanging(){

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region(Constants.ALL_BEACONS_RANGE_ID, null, null, null);
        try{
            beaconManager.startRangingBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }

    private void stopRanging(){
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region(Constants.ALL_BEACONS_RANGE_ID, null, null, null);
        try{
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }

}
