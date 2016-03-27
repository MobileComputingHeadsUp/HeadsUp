package group15.computing.mobile.headsup.utilities;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by Bradley on 3/26/2016.
 */
public class WebClient {
    private AsyncHttpClient client;
    private final String API_BASE_URL = "";

    public WebClient(){
        this.client = new AsyncHttpClient();
    }

    public void get(String url, JsonHttpResponseHandler responseHandler){
        client.get(API_BASE_URL + url, responseHandler);
    }
}
