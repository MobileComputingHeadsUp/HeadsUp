package group15.computing.mobile.headsup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by Bradley on 3/26/2016.
 */
public class WebClient {
    private AsyncHttpClient client;
    private final String API_BASE_URL = "";
    // TODO: Urls should be relative on our server.This API_BASE_URL will hold our domain in the future.
    // TODO: for testing purposes, I am passing in full URLs.

    public WebClient(){
        this.client = new AsyncHttpClient();
    }

    public void get(String url, JsonHttpResponseHandler responseHandler){
        client.get(API_BASE_URL + url, responseHandler);
    }
}
