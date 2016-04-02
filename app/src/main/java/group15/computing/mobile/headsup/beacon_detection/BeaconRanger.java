package group15.computing.mobile.headsup.beacon_detection;

import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.Collection;
import java.util.Observable;

/**
 * Created by Bradley on 3/26/2016.
 */
public class BeaconRanger extends Observable implements RangeNotifier {

    private final String TAG = "BEACON_RANGER";

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
                Log.d(TAG, message);

                setChanged();
                BeaconEvent event = BeaconEvent.FOUND_URL;
                event.attachStringData(url);
                notifyObservers(event);
            }
        }
    }
}
