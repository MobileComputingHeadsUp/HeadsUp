package group15.computing.mobile.headsup.beacon_detection;

import android.util.Log;

import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.Observable;

/**
 * Created by Bradley on 3/26/2016.
 */
public class BeaconMonitor extends Observable implements MonitorNotifier {

    private final String TAG = "BEACON_MONITOR";

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "Entered Region.");
        setChanged();
        notifyObservers(BeaconEvent.ENTER_REGION);
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "Exited Region");
        setChanged();
        notifyObservers(BeaconEvent.EXIT_REGION);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
