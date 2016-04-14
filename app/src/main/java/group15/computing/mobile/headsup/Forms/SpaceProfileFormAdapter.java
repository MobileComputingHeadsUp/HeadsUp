package group15.computing.mobile.headsup.Forms;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import group15.computing.mobile.headsup.R;

/**
 * Created by Bradley on 4/11/2016.
 */
public class SpaceProfileFormAdapter extends ArrayAdapter<FormRow> {

    private Activity myContext;

    public SpaceProfileFormAdapter(Activity context, ArrayList<FormRow> rows) {
        super(context, 0, rows);
        myContext = context;
    }

    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
    // TODO: not using currently.
    static class ViewHolder {
        protected DropdownRow data;
        protected TextView text;
        protected Spinner spin;
        protected int position;
    }

    @Override
    public int getViewTypeCount() {
        // We will have 4 view types.
        // 1. Dropdowns
        // 2. Checkboxes
        // 3. Free Responses
        // 4. Section header view
        return FormRow.FormRowTypes.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    // Helps: https://guides.codepath.com/android/Implementing-a-Heterogenous-ListView
    // TODO: do this https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView#improving-performance-with-the-viewholder-pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the form row for this positonn
        FormRow row = getItem(position);

        // Check if existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // All row types are responsible for returning the correct inflated XML layout file
            convertView = row.getInflatedLayout(getContext());
        }

        // Configure this view for this row boiiii
        row.configView(convertView);

        // return the completed view to render
        return convertView;

    }
}
