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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group15.computing.mobile.headsup.Forms.ChecklistRow;
import group15.computing.mobile.headsup.Forms.FormRow;
import group15.computing.mobile.headsup.Forms.FreeResponseRow;
import group15.computing.mobile.headsup.Forms.SpaceProfileFormAdapter;
import group15.computing.mobile.headsup.Forms.DropdownRow;
import group15.computing.mobile.headsup.Forms.SpaceProfile;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.beacon_detection.RequestedAction;
import group15.computing.mobile.headsup.utilities.APIClient;
import group15.computing.mobile.headsup.utilities.Utilities;

public class ProfileFormActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE FORM ACTIVITY";
    private ListView formContainer;

    private SpaceProfile spaceProfile;
    private SpaceProfileFormAdapter spaceProfileFormAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        // TODO: Uncomment out. commented out currently bcs I am reading a dummy json file
        String response = getIntent().getStringExtra(RequestedAction.DATA);
//        String response = Utilities.loadJSONFromAsset("dummy_form.json", this);
        createUserProfile(response);
        buildForm(response);

        // Config toolbar title
        setTitle("Space Profile Creation");
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

    private void buildForm(String response) {

        try{
            // Get the json data.
            JSONObject jsonData = new JSONObject(response);
            jsonData = jsonData.getJSONObject("data");
            JSONArray dropDownsJson = jsonData.getJSONArray("dropdown");
            JSONArray checkAllJson = jsonData.getJSONArray("checkAllThatApply");
            JSONArray freeResponseJson = jsonData.getJSONArray("freeResponse");

            // init array of form rows.
            ArrayList<FormRow> formRows = new ArrayList<>();

            // Add a header for the dropdown section!
            formRows.add(new FormRow("Dropdowns", "Please select a single option from each of these dropdowns"));

            // Add the dropwdowns!
            for(int i=0; i<dropDownsJson.length(); i++) {
                // Get the current dropdown object
                JSONObject dropdownJson = dropDownsJson.getJSONObject(i);

                // Get the label
                String label = dropdownJson.optString("label");

                // Get the option strings
                JSONArray optionsJson = dropdownJson.getJSONArray("optionStrings");

                // Create Array List of options
                ArrayList<String> options = new ArrayList<>();

                for(int j=0; j<optionsJson.length(); j++){
                    options.add(optionsJson.getString(j));
                }

                formRows.add(new DropdownRow(this, label, options));
            }

            // Now add check alls
            // Add a header for the check all section!
            formRows.add(new FormRow("Check All That Apply", "Please select all options that apply for each question."));

            // Loop em'
            for (int i = 0; i < checkAllJson.length(); i++) {
                // Get the current check all object
                JSONObject checkJson = checkAllJson.getJSONObject(i);

                // Get the label
                String label = checkJson.optString("label");

                // Get the option strings
                JSONArray optionsJson = checkJson.getJSONArray("optionStrings");

                // Create Array List of options
                ArrayList<String> options = new ArrayList<>();

                for(int j=0; j < optionsJson.length(); j++) {
                    options.add(optionsJson.getString(j));
                }
                // Add to the array of form rows.
                formRows.add(new ChecklistRow(this, label, options));
            }

            // Finally add Free Responses
            // Add a header
            formRows.add(new FormRow("Free Response", "Please submit a text answer to these questions"));

            // Loop em'
            for (int i = 0; i < freeResponseJson.length(); i++) {
                // Get the current check all object
                JSONObject freeResponseJSONObject = freeResponseJson.getJSONObject(i);

                // Get the label
                String label = freeResponseJSONObject.optString("label");

                // Get the max char limit
                int charLimit = freeResponseJSONObject.optInt("charLimit");

                // Construct a new FR Row and aAdd to the array of form rows.
                formRows.add(new FreeResponseRow(label, charLimit));
            }

            // Create the adapter and add it to the list view.
            formContainer = (ListView) findViewById(R.id.formContainer);
            spaceProfileFormAdapter = new SpaceProfileFormAdapter(this, formRows);
            formContainer.setAdapter(spaceProfileFormAdapter);

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void submitForm(View view) {
        // Iterate through all form rows
        for(int i=0; i< spaceProfileFormAdapter.getCount(); i++) {
            // Get the form row
            FormRow row = spaceProfileFormAdapter.getItem(i);

            // Tell the form row to add its data to our space profile
            row.addDataToSpaceProfile(spaceProfile);
        }

        // Convert Space Profile object to JSON
        Gson gson = new Gson();
        String spaceProfileJson = gson.toJson(spaceProfile);
        Log.d(TAG, spaceProfileJson);

        // Save to server
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
