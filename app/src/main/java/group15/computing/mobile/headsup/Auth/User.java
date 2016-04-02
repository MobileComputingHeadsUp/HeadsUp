package group15.computing.mobile.headsup.Auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Bradley on 3/28/16.
 */
public class User{

    private GoogleSignInAccount googleAccount;

    public User(GoogleSignInAccount account) {
        googleAccount = account;
    }

    public String getUsername() {
        return googleAccount.getDisplayName();
    }

    public String getEmail() {
        return googleAccount.getEmail();
    }
}
