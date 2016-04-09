package group15.computing.mobile.headsup.Auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

/**
 * Created by Bradley on 3/28/16.
 */
public class User{

    private GoogleSignInAccount googleAccount;
    // TODO: Add Profile and other member variables after its been set up on the server.

    public User(JSONObject jsonObject, GoogleSignInAccount googleAccount){
        this.googleAccount = googleAccount;
        constructUserFromJSON(jsonObject);
    }

    private void constructUserFromJSON(JSONObject jsonObject){
        // TODO: Construct the user (profile and such) once its been setup on the server.
    }

    public String getId(){
        return googleAccount.getId();
    }

    // TODO: Add whatever accessors are necessary.
}
