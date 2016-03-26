package group15.computing.mobile.headsup;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

/**
 * Created by Bradley on 3/26/2016.
 */
public class HeadsUpStarter extends Application implements BootstrapNotifier {
    private static final String TAG = "HeadsUpStarter";
    private RegionBootstrap regionBootstrap;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "App is statring up.");
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        // Detect only Eddystone Beacons
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));

        // TODO: Perhaps add more specific filters later. For now, find all beacons.
        Region region = new Region("all-beacons-startup", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }


    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "Got a call to didEnterRegion in starter.");

        // Disable the regionBootstrap so we can use different Beacon Monitors in the application.
        regionBootstrap.disable();

        // Start the app!
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    public void didExitRegion(Region region) {
        // We don't need to do anything here.
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        // We don't need to do anything here.
    }
}
