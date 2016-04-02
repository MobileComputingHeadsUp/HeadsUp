package group15.computing.mobile.headsup;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import java.util.Observable;
import java.util.Observer;

import group15.computing.mobile.headsup.beacon_detection.BeaconEvent;
import group15.computing.mobile.headsup.beacon_detection.BeaconMonitor;
import group15.computing.mobile.headsup.beacon_detection.BeaconRanger;
import group15.computing.mobile.headsup.utilities.Constants;

/**
 * Created by Bradley on 3/26/2016.
 */
public class HeadsUp extends Application implements BeaconConsumer, Observer {
    private static final String TAG = "HeadsUp";
    private BeaconManager beaconManager;
    private Region region;

    @Override
    public void onCreate(){
        super.onCreate();

        region = new Region(Constants.ALL_BEACONS_RANGE_ID, null, null, null);

        // Set up the beacon manager to look for eddystone URL frames.
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        // Create the notifiers.
        BeaconMonitor monitor = new BeaconMonitor();
        BeaconRanger ranger = new BeaconRanger();
        monitor.addObserver(this);
        ranger.addObserver(this);

        // Set the notifiers.
        beaconManager.setMonitorNotifier(monitor);
        beaconManager.setRangeNotifier(ranger);

        // Start monitoring.
        try{
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {

        // Cast the data to a BeaconEvent and broadcast the intent.
        BeaconEvent event = (BeaconEvent) data;
        Log.d(TAG, "Beacon Event: " + event.toString() + " occurred.");
        event.execute(this);
    }
}
