package group15.computing.mobile.headsup.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.common.data.DataHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

import group15.computing.mobile.headsup.Forms.DropDownAdapter;
import group15.computing.mobile.headsup.Forms.DropdownHolder;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.beacon_detection.RequestedAction;

public class ProfileFormActivity extends AppCompatActivity {

    private ListView formContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);



//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choices, android.R.layout.simple_spinner_item);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);



//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray ); //selected item will look like a spinner set from XML
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerArrayAdapter);

        createForm();
    }

    private void createForm(){

//        try{
            // Get the json data.
//            String data = getIntent().getStringExtra(RequestedAction.DATA);
//            JSONObject jsonData = new JSONObject(data);

//
//            ArrayList<String> spinnerArray = new ArrayList<>();
//            spinnerArray.add("test1");
//            spinnerArray.add("test2");
//
//            Spinner spinner = (Spinner)findViewById(R.id.spinner);
//            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
//                    android.R.layout.simple_spinner_dropdown_item,
//                    spinnerArray);
//            spinner.setAdapter(spinnerArrayAdapter);

            formContainer = (ListView) findViewById(R.id.formContainer);

            ArrayList<String> options = new ArrayList<>();
            options.add("Choice");
            options.add("Option");
            options.add("Another");

//            // TODO: Make this happen from the data
            DropdownHolder item = new DropdownHolder(this, "First Dropdown", options);
            DropdownHolder item1 = new DropdownHolder(this, "Second", options);
            DropdownHolder item2 = new DropdownHolder(this, "Third", options);
            DropdownHolder item3 = new DropdownHolder(this, "Fourth", options);
            DropdownHolder item4 = new DropdownHolder(this, "Fifth", options);

            options.add("Testing");
            DropdownHolder item5 = new DropdownHolder(this, "First Dropdown", options);
            DropdownHolder item6 = new DropdownHolder(this, "Second", options);
            DropdownHolder item7 = new DropdownHolder(this, "Third", options);
            DropdownHolder item8 = new DropdownHolder(this, "Fourth", options);
            DropdownHolder item9 = new DropdownHolder(this, "Fifth", options);
            DropdownHolder item10 = new DropdownHolder(this, "Second", options);
            DropdownHolder item11 = new DropdownHolder(this, "Third", options);
            DropdownHolder item12 = new DropdownHolder(this, "Fourth", options);
            DropdownHolder item13 = new DropdownHolder(this, "Fifth", options);
//
            DropDownAdapter d = new DropDownAdapter(this, R.layout.dropdown_view, new DropdownHolder[] { item, item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13 });

            formContainer.setAdapter(d);
//        }catch(JSONException e){
//            e.printStackTrace();
//        }

    }
}
