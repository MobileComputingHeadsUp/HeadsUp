package group15.computing.mobile.headsup.Forms;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import group15.computing.mobile.headsup.R;

/**
 * Created by Bradley on 4/11/2016.
 */
public class DropDownAdapter extends ArrayAdapter<DropdownHolder> {

    private Activity myContext;

    public DropDownAdapter(Activity context, int textViewResourceId, DropdownHolder[] objects) {
        super(context, textViewResourceId, objects);
        myContext = context;
    }

    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
    static class ViewHolder {
        protected DropdownHolder data;
        protected TextView text;
        protected Spinner spin;
        protected int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // If it hasn't, set up everything:
        LayoutInflater inflator = myContext.getLayoutInflater();
        View view = inflator.inflate(R.layout.dropdown_view, null);

        // Make a new ViewHolder for this row, and modify its data and spinner:
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.text = (TextView) view.findViewById(R.id.text);
        viewHolder.data = getItem(position);
        viewHolder.spin = (Spinner) view.findViewById(R.id.spin);
        viewHolder.spin.setAdapter(viewHolder.data.getAdapter());
        viewHolder.position = position;

        // Used to handle events when the user changes the Spinner selection:
        viewHolder.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                viewHolder.data.setSelected(arg2);
                viewHolder.text.setText(viewHolder.data.getLabel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // Update the TextView to reflect what's in the Spinner
        viewHolder.text.setText(viewHolder.data.getLabel());
        viewHolder.spin.setSelection(getItem(viewHolder.position).getSelected());

        return view;
    }
}
