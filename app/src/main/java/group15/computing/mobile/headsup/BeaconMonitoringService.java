package group15.computing.mobile.headsup;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

/**
 * Created by Bradley on 3/26/2016.
 */
public class BeaconMonitoringService extends Service implements BeaconConsumer, MonitorNotifier{

    Intent beaconDiscovered;
    BeaconManager beaconManager;
    private final String TAG = "MONITOR_SERVICE";

    public BeaconMonitoringService() {
        beaconDiscovered = new Intent(Constants.BEACON_DISCOVERED);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Configure the service to search for URL frames.
        beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the URL frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));
        beaconManager.bind(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        beaconManager.setMonitorNotifier(this);

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "MONITOR SERVICE FOUND ONE!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(beaconDiscovered);
        beaconManager.unbind(this);
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "LEFT REGION");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
