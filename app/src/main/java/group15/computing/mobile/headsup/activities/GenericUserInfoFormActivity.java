package group15.computing.mobile.headsup.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.utilities.APIClient;

public class GenericUserInfoFormActivity extends AppCompatActivity {

    private class Info {
        public String gender;
        public String bio;
        public String birthday;
    }

    private Info userInfo;
    private static final String TAG = "GENERIC USER INFO FORM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_user_info_form);

        this.userInfo = new Info();
        addListenerOnButton();
        addTextEditListener();

    }



    public void onChooseBirthdayClicked(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date Picker");
    }


    public void addTextEditListener() {
        EditText bioText = (EditText) findViewById(R.id.editText);

        // Set text listener
        bioText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // dont need
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // assign text in the text box to our answer string
                String bioText = s.toString();
                userInfo.bio = bioText;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // dont need
            }
        });
    }



    public void addListenerOnButton() {
        RadioGroup genderRadio = (RadioGroup) findViewById(R.id.genderGroup);

        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedButton = (RadioButton) findViewById(checkedId);

                String selectedGender = selectedButton.getText().toString();

                userInfo.gender = selectedGender;

                makeToast(selectedGender);
            }
        });


    }

    public void onSubmitButtonClicked(View v) {
        Log.d(TAG, "THE INFOOOO: ");

        // Convert User Info object to JSON
        Gson gson = new Gson();
        String genericInfoJson = gson.toJson(userInfo);
        Log.d(TAG, genericInfoJson);

        // Save to server
        APIClient.addGenericUserInfo(genericInfoJson, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Get the data from the response
                Log.d(TAG, response.toString());
                makeToast("User Info Saved");

                // Go back to the main activity.
                Intent intent = new Intent(GenericUserInfoFormActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, "Failed to retrieve data.");
                makeToast("Ooops an Error Occurred.");

                // TODO: HANDLE THE ERROR BETTER LOL
            }

        });

    }

    public void setBirthday(Date birthday) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateStr = fmt.format(birthday); // or if you want to save it in String str
        System.out.println(dateStr); // and print after that

        this.userInfo.birthday = dateStr;
        Toast.makeText(GenericUserInfoFormActivity.this,
                "Birthday set! " + birthday.toString(), Toast.LENGTH_SHORT).show();
    }


    private void makeToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GenericUserInfoFormActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
