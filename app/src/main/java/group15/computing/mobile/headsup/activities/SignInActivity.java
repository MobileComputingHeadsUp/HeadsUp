package group15.computing.mobile.headsup.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.Auth.User;
import group15.computing.mobile.headsup.R;
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


//        Button signOutButton = (Button) findViewById(R.id.sign_out_button);
//        signOutButton.setOnClickListener(this);

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
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());

        if(result.isSuccess()){
            Toast.makeText(this, result.getSignInAccount().getDisplayName(), Toast.LENGTH_LONG).show();

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            final User currentUser = new User(acct);

            // Authenticate with the server.
            String idToken = acct.getIdToken();
            APIClient.signInUser(idToken, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Log.d(TAG, "SUCCESS!!!!!!!!!!!!!!!!!!!!" + response.toString());
                        // Get the data from the response
                        JSONObject data = response.getJSONObject("data");

                        // TODO: Add profile information to the user before adding it.
                        Authentication.getInstance().userSignedIn(currentUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    Log.d(TAG, "FAILED!!!!!!" + errorResponse.toString());
                }

            });

            // TODO: This should be in the callback
            Authentication.getInstance().userSignedIn(currentUser);
            this.finish();
        }
    }
}
