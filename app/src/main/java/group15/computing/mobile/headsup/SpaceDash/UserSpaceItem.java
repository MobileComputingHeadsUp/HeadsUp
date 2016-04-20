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
    private String bio;
    private String gender;
    private String age;

    public UserSpaceItem(long timestamp, String name, String pictureUrl, String bio, String gender, String age){
        super(timestamp);
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.bio = bio;
        this.gender = gender;
        this.age = age;
    }

    @Override
    public View getInflatedLayout(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_space_item, parent, false );
    }

    @Override
    public void configView(View convertView) {

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView bio = (TextView) convertView.findViewById(R.id.bio);
        TextView gender = (TextView) convertView.findViewById(R.id.gender);
        TextView age = (TextView) convertView.findViewById(R.id.age);
        ImageView picture = (ImageView) convertView.findViewById(R.id.picture);

        final Context context = convertView.getContext();


        if(name!=null){
            name.setText(this.name);
        }
        if(bio!=null){
            bio.setText("Bio: " + this.bio);
        }
        if(gender!=null){
            gender.setText("Gender: " + this.gender);
        }
        if(age!=null){
            age.setText("Age: " + this.age);
        }
        if(picture!=null && this.pictureUrl.length()>0){
            Picasso.with(context).load(this.pictureUrl).into(picture);
        }
    }

    public String getName(){
        return this.name;
    }
}
