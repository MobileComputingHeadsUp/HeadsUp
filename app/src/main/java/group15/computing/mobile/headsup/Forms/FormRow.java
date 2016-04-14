package group15.computing.mobile.headsup.Forms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import group15.computing.mobile.headsup.R;

/**
 * Created by sergiopuleri on 4/13/16.
 */

// Base class from which each type of form row will inherit form..
// Dropdown, Free Response, Check all that apply, and a Header.
// The only common functionality is that they all have a label.
public class FormRow {

    // Need to use this enum to tell the list view the different types
    // Of views that we are using. And kinda sorta RTTI ;)
    public enum FormRowTypes {
        DROPDOWN,
        FREE_RESPONSE,
        CHECK,
        HEADER
    }

    private String label;
    private String sublabel;
    private FormRowTypes type;

    // Super constructor to set the type and label
    public FormRow(String label, FormRowTypes type) {
        this.label = label;
        this.type = type;
    }

    // If call this constructor, we are making a header!
    // Instantiating a base class FormRow is only done for headers for sections.
    public FormRow(String label, String sublabel) {
        this.label = label;
        this.sublabel = sublabel;
        this.type = FormRowTypes.HEADER;
    }

    public String getLabel() {
        return label;
    }

    public String getSublabel() {
        return sublabel;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public FormRowTypes getType() {
        return type;
    }

    public void setType(FormRowTypes type) {
        this.type = type;
    }


    // Each form row subtype will configure the view however they like.
    // This concrete FormRow will only be used for the HEADER VIEW
    public void configView(View convertView) {
        // Lookup view for data population
        TextView label = (TextView) convertView.findViewById(R.id.headerText);
        TextView sublabel = (TextView) convertView.findViewById(R.id.headerSubText);
        if (label != null) {
            // populate the label with the data
            label.setText(this.getLabel());
        }
        if (sublabel != null) {
            // populate the sub-label with the data
            sublabel.setText(this.getSublabel());
        }
    }

    // Used to first init the view for the list row.
    // This implementation is used for Header View.
    // all other rows will override this
    public View getInflatedLayout(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.form_header_view, null);
    }

    // Used to add the data in this form row to the space profile object
    // That we will be saving to the DB.
    // This implementation is used for the Header view, so it will do nothing...
    // All other form rows will override and do something
    public void addDataToSpaceProfile(SpaceProfile spaceProfile) {
        // header view doesnt need to save data
    }
}
