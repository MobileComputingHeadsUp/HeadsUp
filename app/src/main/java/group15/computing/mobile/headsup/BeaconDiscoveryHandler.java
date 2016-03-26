package group15.computing.mobile.headsup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Bradley on 3/26/2016.
 */
public class BeaconDiscoveryHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RECEIVER", "RECEIVED AN INTENT");
        Intent i = new Intent(context, BeaconRangingActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
