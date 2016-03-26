package group15.computing.mobile.headsup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by Bradley on 3/25/2016.
 */
public class BeaconMonitoringActivity extends Activity implements BeaconConsumer, RangeNotifier {

    private static final String TAG = "BeaconMonitoring"; // Used for debugging purposes.
    private BeaconManager beaconManager;
    private TextView beaconUrlTV;
    private TextView webResponseTV;
    private WebClient webClient;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_monitoring);

        // This WebClient will be used to make requests to the server after we receive a URL.
        webClient = new WebClient();
        beaconUrlTV = (TextView) findViewById(R.id.beaconUrl);
        webResponseTV = (TextView) findViewById(R.id.webResponse);
    }

    @Override
    public void onResume(){
        super.onResume();

        // Create the beacon manager.
        beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());

        // Configure manager to detect URL frames.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));
        beaconManager.bind(this);
    }


    @Override
    public void onBeaconServiceConnect() {

        // Regions define criterion of fields to match beacons with. The first parameter is a string id
        // to distinguish this region from other regions in the system. The other parameters are identifiers
        // that identify beacons. If they are set to null, we will detect all beacons.
        Region region = new Region("all-beacons-region", null, null, null);

        try{
            beaconManager.startRangingBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }
        beaconManager.setRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for(Beacon beacon: beacons){
            // Check to see if the beacon is an Eddystone-URL frame.
            if(beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10){

                if(beacon.getId1().toByteArray().length == 0){
                    // Sometimes the appears to send incorrectly. Without this check, the URLCompressor
                    // would occasionally throw ArrayOutOfBounds Exceptions.
                    return;
                }

                // Decode the url
                String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                Double distance = beacon.getDistance();

                // Display the message to the user and log it.
                String message = "Found a beacon transmitting the URL: " + url + " approx " + distance + " meters away.";
                updateBeaconUrlTV(message);

                // Use the url on the main thread.
                makeRequestToURL(url);

            }
        }
    }

    private void makeRequestToURL(final String url){

        // Note: This request MUST be made on the main thread. Otherwise, the thread may be dead
        // by the time the response handler is invoked.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Do some checking on the url to know what to do with it. For testing purposes, I am
                // TODO: just making making a get request on that url.
                webClient.get(url, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            // Get the data from the response
                            JSONObject data = response.getJSONObject("data");

                            // TODO: We will need to make classes to deserialize the data into and do stuff with.
                            // TODO: For now, I'm just going to display the data as a string for testing purposes.
                            Log.d(TAG, data.toString());
                            webResponseTV.setText(data.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void updateBeaconUrlTV(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, text);
                beaconUrlTV.setText(text);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        beaconManager.unbind(this);
    }
}
