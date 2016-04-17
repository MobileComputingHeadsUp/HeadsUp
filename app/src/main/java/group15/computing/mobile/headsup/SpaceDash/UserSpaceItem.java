package group15.computing.mobile.headsup.SpaceDash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
public class UserSpaceItem extends SpaceItem{
    private String name;
    private String pictureUrl;
    private SpaceProfile profile;

    // TODO: This View and the data shown needs more.

    public UserSpaceItem(String name, String pictureUrl, SpaceProfile profile){
        super(SpaceItem.SpaceItemType.USER);
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.profile = profile;
    }

    @Override
    public View getInflatedLayout(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_space_item, parent, false );
    }

    @Override
    public void configView(View convertView) {

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView picture = (ImageView) convertView.findViewById(R.id.picture);
        CardView userCard = (CardView) convertView.findViewById(R.id.user_card_view);

        final Context context = convertView.getContext();


        if(name!=null){
            name.setText(this.name);
        }
        if(picture!=null && this.pictureUrl.length()>0){
            Picasso.with(context).load(this.pictureUrl).into(picture);
        }

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
