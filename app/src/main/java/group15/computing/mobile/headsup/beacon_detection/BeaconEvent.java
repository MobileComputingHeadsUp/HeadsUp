package group15.computing.mobile.headsup.beacon_detection;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import group15.computing.mobile.headsup.utilities.Constants;

/**
 * Created by Bradley on 3/26/2016.
 */
public enum BeaconEvent {

    // This enum represents the events that can happen as a result of stuff with beacons.
    ENTER_REGION(){ // Used when monitering and enter region is called.
        @Override
        public void execute(Context context){

            // An Enter Region event will trigger a launch intent
            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(Constants.PACKAGE_NAME);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);

            try{
                contentIntent.send();
            } catch(PendingIntent.CanceledException e){
                e.printStackTrace();
            }
        }
    },
    EXIT_REGION(new Intent(Constants.EXIT_REGION)){ // Used when montiering and exit region is called.
        @Override
        public void execute(Context context){
            context.sendBroadcast(this.intent);
        }
    },
    FOUND_UID(new Intent(Constants.UID_FOUND)){ // Used to broadcast a single UID when a beacon is found while ranging.
        @Override
        public void execute(Context context){
            context.sendBroadcast(this.intent);
        }
    },
    BEACONS_FOUND(new Intent(Constants.BEACONS_FOUND)){ // Used to broadcast an array (in json) of beaconIds found while ranging.
        @Override
        public void execute(Context context){
            context.sendBroadcast(this.intent);
        }
    };

    public static final String DATA = "beacon-event-data";
    protected Intent intent;

    BeaconEvent(){}
    BeaconEvent(Intent intent){
        this.intent = intent;
    }

    public void attachStringData(String data){
        intent.putExtra(DATA, data);
    }

    public abstract void execute(Context context);
}
