package group15.computing.mobile.headsup.utilities;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by Bradley on 3/26/2016.
 */
public class APIClient {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String API_BASE_URL = "http://76493b8c.ngrok.io/";

    public static void get(String url, JsonHttpResponseHandler responseHandler){
        client.get(API_BASE_URL + url, responseHandler);
    }

    public static void signInUser(String idToken, JsonHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("id_token", idToken);
        client.get(API_BASE_URL + "auth/google-token", params, responseHandler);
    }
}
