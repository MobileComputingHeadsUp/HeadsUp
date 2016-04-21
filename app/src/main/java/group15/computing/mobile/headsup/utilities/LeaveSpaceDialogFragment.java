package group15.computing.mobile.headsup.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.activities.MainActivity;
import group15.computing.mobile.headsup.activities.ProfileFormActivity;
import group15.computing.mobile.headsup.activities.SpaceDashboard;
import group15.computing.mobile.headsup.beacon_detection.RequestedAction;

/**
 * Created by Bradley on 4/20/16.
 */
public class LeaveSpaceDialogFragment extends DialogFragment {

    private static final String TAG = "LEAVE SPACE DIALOG";
    private Timer timer;
    private SpaceDashboard context;

    public LeaveSpaceDialogFragment(SpaceDashboard context) {
        super();
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you still here?");
        builder.setMessage("We've noticed you are no longer in range of our space. Let us know if you are still here!")
                .setPositiveButton("I'M HERE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        timer.cancel();
                        timer.purge();
                    }
                })
                .setNegativeButton("I LEFT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        timer.cancel();
                        timer.purge();
                        leaveSpace();
                    }
                });

        // Start a timer that will call remove the user from the space if there is no response.
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                leaveSpace();
            }
        }, 7000); // TODO: Make this delay longer after testing.

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void leaveSpace(){

        // TODO: Create the server side endpoints and test this.
        // Remove the user from the space.
        APIClient.leaveSpace(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                // Get the data from the response
                Log.d(TAG, response.toString());

                // Remove the user from the space.
                Authentication.getInstance().setCurrentSpaceID("");

                // Go back to the main activity.
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(TAG, "Failed to leave space.");
            }
        });
    }
}
