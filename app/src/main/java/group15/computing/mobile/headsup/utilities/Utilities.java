package group15.computing.mobile.headsup.utilities;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sergiopuleri on 4/13/16.
 */
public class Utilities {
    public static String loadJSONFromAsset(String filename, Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
