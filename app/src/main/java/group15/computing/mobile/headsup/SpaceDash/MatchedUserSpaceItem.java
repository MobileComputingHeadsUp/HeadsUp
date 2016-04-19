package group15.computing.mobile.headsup.SpaceDash;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import group15.computing.mobile.headsup.Forms.SpaceProfile;
import group15.computing.mobile.headsup.R;

/**
 * Created by Bradley on 4/16/16.
 */
public class MatchedUserSpaceItem extends SpaceItem{
    private UserSpaceItem user;
    private SpaceProfile matchedAttributes;

    public MatchedUserSpaceItem(long timestamp, String name, String pictureUrl, String bio, String gender, String age, SpaceProfile matchedProfileAttributes){
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
    public void configView(View convertView) {

        // TODO: Show details on click.
        user.configView(convertView);
        CardView userCard = (CardView) convertView.findViewById(R.id.user_card_view);

        if(userCard!=null){
            userCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Show profile details on click.
                }
            });
        }
    }
}
