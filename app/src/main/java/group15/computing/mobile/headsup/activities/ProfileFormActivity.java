package group15.computing.mobile.headsup.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import group15.computing.mobile.headsup.Forms.DropDownAdapter;
import group15.computing.mobile.headsup.Forms.DropdownHolder;
import group15.computing.mobile.headsup.Forms.SpaceProfile;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.beacon_detection.RequestedAction;
import group15.computing.mobile.headsup.utilities.APIClient;

public class ProfileFormActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE FORM ACTIVITY";
    private ListView formContainer;

    private SpaceProfile spaceProfile;
    private DropDownAdapter dropDownAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        String response = getIntent().getStringExtra(RequestedAction.DATA);
        createUserProfile(response);
        createDropdowns(response);
        // TODO: Implement other forms too. (check all that apply/ free response
    }

    private void createUserProfile(String response){
        try{
            JSONObject jsonResponse = new JSONObject(response);
            String spaceID = jsonResponse.getString("spaceID");

            JSONObject data = jsonResponse.getJSONObject("data");
            int requiredUserInfoVersion = data.getInt("requiredUserInfoVersion");
            spaceProfile = new SpaceProfile(spaceID, requiredUserInfoVersion);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void createDropdowns(String response){

        try{
            // Get the json data.
            JSONObject jsonData = new JSONObject(response);
            jsonData = jsonData.getJSONObject("data");
            JSONArray dropDownsJson = jsonData.getJSONArray("dropdown");

            DropdownHolder[] dropDowns = new DropdownHolder[dropDownsJson.length()];

            for(int i=0; i<dropDownsJson.length(); i++){

                // Get the current dropdown object
                JSONObject dropdownJson = dropDownsJson.getJSONObject(i);
                String label = dropdownJson.optString("label"); // Get the label
                JSONArray optionsJson = dropdownJson.getJSONArray("optionStrings");

                ArrayList<String> options = new ArrayList<>();

                for(int j=0; j<optionsJson.length(); j++){
                    options.add(optionsJson.getString(j));
                }

                dropDowns[i] = new DropdownHolder(this, label, options);
            }

            // Create the adapter and add it to the list view.
            formContainer = (ListView) findViewById(R.id.formContainer);
            dropDownAdapter = new DropDownAdapter(this, R.layout.dropdown_view, dropDowns);
            formContainer.setAdapter(dropDownAdapter);

        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public void submitForm(View view){
        for(int i=0; i<dropDownAdapter.getCount(); i++){
            DropdownHolder dropdown = dropDownAdapter.getItem(i);

            HashMap<String, String> dropdownValue = new HashMap<>();
            dropdownValue.put(dropdown.getLabel(), dropdown.getAdapter().getItem(dropdown.getSelected()));
            spaceProfile.addDropdown(dropdownValue);
        }

        Gson gson = new Gson();
        String spaceProfileJson = gson.toJson(spaceProfile);
        Log.d(TAG, spaceProfileJson);

        APIClient.addSpaceProfile(spaceProfileJson, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Get the data from the response
                Log.d(TAG, response.toString());
                makeToast("Profile Created!");
                // TODO: GO TO SPACE DASH
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, "Failed to retrieve data.");
                makeToast("Ooops an Error Occurred.");

                // TODO: HANDLE THE ERROR BETTER LOL
            }
        });

        spaceProfile.clearData();
    }

    private void makeToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ProfileFormActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
