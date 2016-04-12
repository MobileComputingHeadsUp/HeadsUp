package group15.computing.mobile.headsup.Forms;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;


/**
 * Created by Bradley on 4/11/2016.
 */
public class DropdownHolder {

    private int selected;
    private ArrayAdapter<String> spinnerAdapter;
    private String label;

    public DropdownHolder(Context parent, String label, ArrayList<String> options){

        spinnerAdapter = new ArrayAdapter<>(parent, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.label = label;
  }

    public ArrayAdapter<String> getAdapter() {
        return spinnerAdapter;
    }

    public String getText() {
        return spinnerAdapter.getItem(selected);
    }

    public String getLabel() { return label; }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
