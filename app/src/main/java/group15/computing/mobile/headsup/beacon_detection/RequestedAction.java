package group15.computing.mobile.headsup.beacon_detection;

/**
 * Created by Bradley on 4/9/2016.
 */

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONObject;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.activities.ProfileFormActivity;
import group15.computing.mobile.headsup.activities.SpaceDashboard;
import group15.computing.mobile.headsup.utilities.Constants;


/**
 * Created by Bradley on 3/26/2016.
 */
public enum RequestedAction {

    // This enum represents the events that can happen as a result of stuff with beacons.
    NONE(){
        @Override
        public void execute(Context context, JSONObject data){
            Log.d(TAG, "NO ACTION REQUIRED");
            // Do nothing
        }
    },
    REQUIRED_USER_INFO(){
        @Override
        public void execute(Context context, JSONObject data){
            Log.d(TAG, "REQUIRED_USER_INFO");
            Intent i = new Intent(context, ProfileFormActivity.class);
            i.putExtra(DATA, data.toString());
            context.startActivity(i);
        }
    },
    SIGN_IN(){
        @Override
        public void execute(Context context, JSONObject data){
            // TODO: Go to sign in activity.
            Log.d(TAG, "SIGN IN");

        }
    },
    SPACE_DASH(){
        @Override
        public void execute(Context context, JSONObject data){

            Log.d(TAG, "SPACE_DASH");
            // Add the SpaceID to the Authentication object.
            String spaceID = data.optString("spaceID");
            Authentication.getInstance().setCurrentSpaceID(spaceID);

            Intent i = new Intent(context, SpaceDashboard.class);
            context.startActivity(i);
        }
    };

    protected final static String TAG = "REQUESTED ACTION";
    public abstract void execute(Context context, JSONObject data);
    public static final String DATA = "data";
}
