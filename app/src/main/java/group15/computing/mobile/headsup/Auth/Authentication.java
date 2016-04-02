package group15.computing.mobile.headsup.Auth;

/**
 * Created by Bradley on 3/28/16.
 */
public class Authentication {

    private User currentUser;
    private static Authentication authentication;

    private Authentication(){

    }

    public void userSignedIn(User user){
        currentUser = user;
    }

    public void userSignedOut(){
        currentUser = null;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public boolean isSignedIn(){
        return currentUser!=null;
    }

    public static Authentication getInstance(){
        if(authentication==null){
            authentication = new Authentication();
        }
        return authentication;
    }
}
