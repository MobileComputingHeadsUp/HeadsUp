package group15.computing.mobile.headsup.SpaceDash;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import group15.computing.mobile.headsup.Forms.SpaceProfile;
import group15.computing.mobile.headsup.R;

/**
 * Created by Bradley on 4/16/16.
 */
public class MatchedUserSpaceItem extends SpaceItem{
    private UserSpaceItem user;
    private Attributes matchedAttributes;

    static class Attributes{
        protected HashMap<String, String> dropdowns;
        protected HashMap<String, String[]> checkAllThatApplies;
        protected HashMap<String, String> freeResponses;

        public Attributes() {
            this.dropdowns = new HashMap<>();
            this.checkAllThatApplies = new HashMap<>();
            this.freeResponses = new HashMap<>();
        }
    }

    public MatchedUserSpaceItem(long timestamp, String name, String pictureUrl, String bio, String gender, String age, Attributes matchedProfileAttributes){
        super(timestamp);
        user = new UserSpaceItem(timestamp, name, pictureUrl, bio, gender, age);
        this.matchedAttributes = matchedProfileAttributes;
    }

    @Override
    public View getInflatedLayout(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_user_space_item, parent, false );
    }

    @Override
    public void configView(final View convertView) {

        // Setup the basic space profile view stuff.
        user.configView(convertView);

        // Setup the click listener.
        final CardView userCard = (CardView) convertView.findViewById(R.id.user_card_view);
        final RelativeLayout detailedInfo = (RelativeLayout) convertView.findViewById(R.id.detailed_info);

        if(userCard!=null){
            userCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(detailedInfo.getVisibility() == View.GONE){
                        expand(detailedInfo);
                    }else{
                        collapse(detailedInfo);
                    }
                }
            });
        }

        generateMatchedAttributes(convertView);
    }

    private void generateMatchedAttributes(final View convertView){

        // Set the matches header
        TextView header = (TextView) convertView.findViewById(R.id.matches_header);
        header.setText(user.getName() + " answered these questions similarly!");

        // Add the matched attributes.
        LinearLayout matchedAttrsContainer = (LinearLayout) convertView.findViewById(R.id.matched_attrs_container);

        addDropdownResponses(convertView, matchedAttrsContainer);
        addCheckAllThatApplies(convertView, matchedAttrsContainer);
        addFreeResponses(convertView, matchedAttrsContainer);
    }

    private void addDropdownResponses(View convertView, LinearLayout matchedAttrsContainer){

        HashMap<String, String> dropdowns = matchedAttributes.dropdowns;
        for(String question : dropdowns.keySet()){

            // Create a the view for each dropdown and extract the textview from it.
            View matchItemView = LayoutInflater.from(convertView.getContext()).inflate(R.layout.matched_attributes, null);
            TextView matchItem = (TextView)matchItemView.findViewById(R.id.text);

            // Set the text and add to the view
            String matchString = "<b>" + question + "</b>&nbsp;&nbsp;" + dropdowns.get(question);
            matchItem.setText(Html.fromHtml(matchString));
            matchedAttrsContainer.addView(matchItem);
        }
    }

    private void addCheckAllThatApplies(View convertView, LinearLayout matchedAttrsContainer){

        HashMap<String, String[]> checkAllThatApplies = matchedAttributes.checkAllThatApplies;
        for(String question : checkAllThatApplies.keySet()){

            // Create a the view for each dropdown and extract the textview from it.
            View matchItemView = LayoutInflater.from(convertView.getContext()).inflate(R.layout.matched_attributes, null);
            TextView matchItem = (TextView)matchItemView.findViewById(R.id.text);

            // Set the text
            String matchString = "<b>" + question + "</b>&nbsp;&nbsp;";
            String[] responses = checkAllThatApplies.get(question);

            // Add all the responses to the string.
            for(int i = 0; i < responses.length; i++){
                matchString += responses[i];

                if(i < responses.length - 1){
                    matchString += ", ";
                }
            }

            matchItem.setText(Html.fromHtml(matchString));
            matchedAttrsContainer.addView(matchItem);
        }
    }

    private void addFreeResponses(View convertView, LinearLayout matchedAttrsContainer){

        HashMap<String, String> freeResponses = matchedAttributes.freeResponses;
        for(String question : freeResponses.keySet()){

            // Create a the view for each dropdown and extract the textview from it.
            View matchItemView = LayoutInflater.from(convertView.getContext()).inflate(R.layout.matched_attributes, null);
            TextView matchItem = (TextView)matchItemView.findViewById(R.id.text);

            // Set the text and add to the view
            String matchString = "<b>" + question + "</b>&nbsp;&nbsp;" + freeResponses.get(question);
            matchItem.setText(Html.fromHtml(matchString));
            matchedAttrsContainer.addView(matchItem);
        }
    }

    // Animation stuff.
    void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
