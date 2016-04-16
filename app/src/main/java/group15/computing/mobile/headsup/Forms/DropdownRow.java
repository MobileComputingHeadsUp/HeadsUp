package group15.computing.mobile.headsup.Forms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import group15.computing.mobile.headsup.R;


/**
 * Created by Bradley on 4/11/2016.
 */
public class DropdownRow extends FormRow {

    private int selected;
    private ArrayAdapter<String> spinnerAdapter;

    public DropdownRow(Context parent, String label, ArrayList<String> options){
        super(label, FormRowTypes.DROPDOWN);
        spinnerAdapter = new ArrayAdapter<>(parent, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  }

    public ArrayAdapter<String> getAdapter() {
        return spinnerAdapter;
    }

    public String getText() {
        return spinnerAdapter.getItem(selected);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }


    @Override
    public void addDataToSpaceProfile(SpaceProfile spaceProfile) {

        // Add it to the space profile
        spaceProfile.addDropdown(getLabel(), getAdapter().getItem(getSelected()));
    }

    @Override
    public void configView(View convertView) {
        // Lookup view for data population
        TextView label = (TextView) convertView.findViewById(R.id.text);
        Spinner spin = (Spinner) convertView.findViewById(R.id.spin);
        if (label != null) {
            // populate the label with the data
            label.setText(getLabel());
        }
        if (spin != null) {
            spin.setAdapter(getAdapter());
            // Used to handle events when the user changes the Spinner selection:
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setSelected(arg2);
                // not sure if need the below. ask brad what the "Text" waas...
//                viewHolder.text.setText(viewHolder.data.getLabel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        // Update the TextView to reflect what's in the Spinner
//        viewHolder.text.setText(viewHolder.data.getLabel());
            spin.setSelection(getSelected());
        }
    }

    @Override
    public View getInflatedLayout(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.dropdown_view, null);
    }
}
