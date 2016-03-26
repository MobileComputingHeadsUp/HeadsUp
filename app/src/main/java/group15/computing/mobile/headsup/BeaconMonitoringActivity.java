package group15.computing.mobile.headsup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.Collection;

/**
 * Created by Bradley on 3/25/2016.
 */
public class BeaconMonitoringActivity extends Activity implements BeaconConsumer, RangeNotifier {

    private static final String TAG = "BeaconMonitoring"; // Used for debugging purposes.
    private BeaconManager beaconManager;
    private TextView beaconUrlTV;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_monitoring);

        beaconUrlTV = (TextView) findViewById(R.id.beaconUrl);
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

        // TODO: Try monitoring instead of ranging. I think it can be done like this.
        // TODO: "Monitoring allows you to scan for beacon regions, Ranging is for interacting with individual beacons."
//        beaconManager.setMonitorNotifier(this);
//        try {
//            beaconManager.startMonitoringBeaconsInRegion(region);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for(Beacon beacon: beacons){
            // Check to see if the beacon is an Eddystone-URL frame.
            if(beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10){
                // Decode the url
                String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                Double distance = beacon.getDistance();

                // Display the message to the user and log it.
                String message = "Found a beacon transmitting the URL: " + url + " approx " + distance + " meters away.";
                Log.d(TAG, message);
                updateBeaconUrlTv(message);
            }
        }
    }

    private void updateBeaconUrlTv(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
