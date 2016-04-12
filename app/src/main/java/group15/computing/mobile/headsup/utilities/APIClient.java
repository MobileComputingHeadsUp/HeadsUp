package group15.computing.mobile.headsup.utilities;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.Auth.User;

/**
 * Created by Bradley on 3/26/2016.
 */
public class APIClient {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String API_BASE_URL = "http://f5643e7a.ngrok.io/";

    public static void get(String url, JsonHttpResponseHandler responseHandler){
        client.get(API_BASE_URL + url, responseHandler);
    }

    public static void signInUser(String idToken, JsonHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("id_token", idToken);
        client.get(API_BASE_URL + "auth/google-token", params, responseHandler);
    }

    public static void hitBeacon(String beaconId, JsonHttpResponseHandler responseHandler){
        // Get the current user.
        User currentUser = Authentication.getInstance().getCurrentUser();

        // Add the parameters
        String identifier = beaconId.split("x")[1]; // Remove the 0x part of the id.
        identifier = identifier.toUpperCase();

        RequestParams params = new RequestParams();
        params.put("beacon_identifier", identifier);
        params.put("google_id", currentUser.getId());

        // Make the request
        client.post(API_BASE_URL + "api/beacons/hit_beacon", params, responseHandler);
    }
}
