package group15.computing.mobile.headsup.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import group15.computing.mobile.headsup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = 1995;
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {

        int displayedMonth = month + 1;
        // Set date picker button to invisible
        Button button = (Button) getActivity().findViewById(R.id.chooseDate);
        button.setVisibility(View.GONE);


        // Set the date of the birthday label
        TextView tv = (TextView) getActivity().findViewById(R.id.actualBirthday);
        tv.setVisibility(View.VISIBLE);

        String stringOfDate = displayedMonth + "/" + day  + "/" + year;
        tv.setText(" " + stringOfDate);

        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        Date date = calendar.getTime();

        // Set the date in the main form activity
        ((GenericUserInfoFormActivity)getActivity()).setBirthday(date);
    }

}
