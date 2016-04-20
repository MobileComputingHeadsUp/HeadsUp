package group15.computing.mobile.headsup.Forms;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;


import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import group15.computing.mobile.headsup.R;

/**
 * Created by sergiopuleri on 4/14/16.
 */
public class FreeResponseRow extends FormRow {

    private String answer;
    private int charLimit;

    public FreeResponseRow(String label, int charLimit) {
        super(label, FormRowTypes.FREE_RESPONSE);
        this.charLimit = charLimit;
    }

    @Override
    public void configView(View convertView) {

        // Lookup edit text in the passed in convert view to configure it
        MaterialEditText editText = (MaterialEditText) convertView.findViewById(R.id.editText);

        if (editText != null) {
            // Set char limit
            editText.setMaxCharacters(charLimit);

            // Set label text
            editText.setFloatingLabelText(getLabel());
            editText.setHint(getLabel());

            // Set text listener
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // dont need
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // assign text in the text box to our answer string
                    answer = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // dont need
                }
            });
        }

    }

    @Override
    public void addDataToSpaceProfile(SpaceProfile spaceProfile) {

        // Add it to the space profile
        spaceProfile.addFreeResponse(getLabel(), answer);
    }

    @Override
    public View getInflatedLayout(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.free_response_view, null);
    }
}
