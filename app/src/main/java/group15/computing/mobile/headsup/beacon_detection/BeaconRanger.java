package group15.computing.mobile.headsup.beacon_detection;

import android.util.Log;

import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

/**
 * Created by Bradley on 3/26/2016.
 */
public class BeaconRanger extends Observable implements RangeNotifier {

    private final String TAG = "BEACON_RANGER";

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

        ArrayList<String> beaconIDs = new ArrayList<>();
        for(Beacon beacon: beacons){
            // Check to see if the beacon is an Eddystone-UID frame.
            if(beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00){

                // Extract info from the beacon.
                Double distance = beacon.getDistance(); // TODO: Do something with the distance?
                Identifier uid = beacon.getId2();

                // Display the message to the user and log it.
                String message = "Found a beacon transmitting the UID: " + uid + " approx " + distance + " meters away.";
                Log.i(TAG, message);

                BeaconEvent event = BeaconEvent.FOUND_UID;
                event.attachStringData(uid.toString());

                setChanged();
                notifyObservers(event);

                beaconIDs.add(uid.toString().split("x")[1].toUpperCase());
            }
        }

        // Create a json string with all the beacon ids.
        Gson gson = new Gson();
        String beaconIDsString = gson.toJson(beaconIDs);

        Log.i(TAG, beaconIDsString);

        BeaconEvent event = BeaconEvent.BEACONS_FOUND;
        event.attachStringData(beaconIDsString);
        setChanged();
        notifyObservers(event);
    }
}
