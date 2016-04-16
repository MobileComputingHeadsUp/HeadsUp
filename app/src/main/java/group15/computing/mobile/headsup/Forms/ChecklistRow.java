package group15.computing.mobile.headsup.Forms;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import group15.computing.mobile.headsup.R;

/**
 * Created by sergiopuleri on 4/13/16.
 */
public class ChecklistRow extends FormRow {

    private ArrayList<String> options;
    private ArrayList<CheckBox> checkBoxes;
    private Context context;

    public ChecklistRow(Context parent, String label, ArrayList<String> options) {
        super(label, FormRowTypes.CHECK);
        this.options = options;
        this.context = parent;
        this.checkBoxes = new ArrayList<>();
    }

    @Override
    public void addDataToSpaceProfile(SpaceProfile spaceProfile) {
        // Iterate thru checkboxes and see which ones are selected.
        // Add those to the list
        ArrayList<String> checkedOptions = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            // Get it
            CheckBox current = checkBoxes.get(i);

            if (current.isChecked()) {
                // Get the text
                String optionText = current.getText().toString();

                // Add it to the list
                checkedOptions.add(optionText);
            }
        }


        // Convert the array-list to a string array
        String []arr = checkedOptions.toArray(new String[checkedOptions.size()]);

        // Add to the space profile
        spaceProfile.addCheck(getLabel(), arr);
    }

    @Override
    public void configView(View convertView) {
        // Lookup view for data population
        TextView label = (TextView) convertView.findViewById(R.id.text);
        if (label != null) {
            // populate the label with the data
            label.setText(getLabel());
        }

        LinearLayout checklistLinearLayout = (LinearLayout) convertView.findViewById(R.id.checkboxes);

        if (checklistLinearLayout != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // Add checkboxes for the first time only.
            // Might come int issues when we re-use this view? o_O
            if (checkBoxes.size() == 0) {
                for (int i = 0; i < options.size(); i++) {
                    String option = options.get(i);

                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setText(option);

                    if (Build.VERSION.SDK_INT < 23) {
                        checkBox.setTextAppearance(context, R.style.CheckBoxTheme);
                    } else {
                        checkBox.setTextAppearance(R.style.CheckBoxTheme);
                    }
//                    checkBox.setButtonTintList(ColorStateList.valueOf(resources.getColor(R.color.accent, wrapper.getTheme())));
                    checklistLinearLayout.addView(checkBox, layoutParams);
                    checkBoxes.add(checkBox);
                }
            }

        }
    }

    @Override
    public View getInflatedLayout(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.checkall_view, null);
    }
}
