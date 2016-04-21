package group15.computing.mobile.headsup.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.test.SingleLaunchActivityTestCase;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.Auth.User;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.beacon_detection.RequestedAction;
import group15.computing.mobile.headsup.utilities.APIClient;
import group15.computing.mobile.headsup.utilities.Constants;

public class SignInActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "SIGN IN ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Configure sign-in options.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(Constants.GOOGLE_SERVICES_WEB_CLIENT_ID)
                .build();

        // Build a GoogleApiClient with access to sign-in api
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Start the sign in intent.
        signIn();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO: Actually handle this!
    }

    @Override
    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.sign_out_button:
//                signOut();
//                break;
//        }
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallbacks<Status>() {
            // TODO: Handle errors and success better.
            @Override
            public void onSuccess(Status status) {
                Log.d(TAG, "LOGGED OUT SUCCESFULLY");
            }

            @Override
            public void onFailure(Status status) {
                Log.d(TAG, "LOGGED OUT FAILED");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(!result.isSuccess()){
                Toast.makeText(this, "You must sign in to continue", Toast.LENGTH_LONG).show();
                signIn();
            }
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(final GoogleSignInResult result){
        if(result.isSuccess()){

            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            // Authenticate with the server.
            String idToken = acct.getIdToken();
            APIClient.signInUser(idToken, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (statusCode == 500 || statusCode == 403) {
                        returnFailure();
                    }

                    JSONObject userRes = null;
                    try {
                        // Get the user from the response
                        userRes = response.getJSONObject("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Construct the user object with the JSON response and google account.
                    User user = new User(userRes, acct);
                    Authentication.getInstance().userSignedIn(user);

                    Log.d(TAG, "Logged In");
                    makeToast("Welcome " + acct.getDisplayName());

                    // Get the data from the response
                    String requestedAction = response.optString("action");
                    Log.d(TAG, "THE ACTION IS " + requestedAction);
                    RequestedAction action = RequestedAction.valueOf(requestedAction);
                    action.execute(SignInActivity.this, response);

                    returnSuccess();
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                        returnFailure();

                }
            });
        }else{
            returnFailure();
        }
    }

    private void returnSuccess(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void returnFailure(){
        Log.d(TAG, "Error could not log in");
        makeToast("Error could not log in");

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void makeToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignInActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
