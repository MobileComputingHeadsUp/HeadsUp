package group15.computing.mobile.headsup.SpaceDash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import group15.computing.mobile.headsup.R;

/**
 * Created by Bradley on 4/16/16.
 */
public class AdSpaceItem extends SpaceItem{
    private String title;
    private String description;
    private String imgUrl;
    private String link;

    public AdSpaceItem(long timestamp, String title, String description, String imgUrl, String link){
        super(SpaceItemType.AD, timestamp);
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.link = link;
    }

    @Override
    public View getInflatedLayout(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_space_item, parent, false );
    }

    @Override
    public void configView(View convertView) {

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        ImageView adImage = (ImageView) convertView.findViewById(R.id.adImage);

        final Context context = convertView.getContext();

        if(title!=null){
            title.setText(this.title);
            if(this.link.length()>0){
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uriUrl = Uri.parse(AdSpaceItem.this.link);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        context.startActivity(launchBrowser);
                    }
                });
            }
        }
        if(description!=null){
            description.setText(this.description);
        }
        if(adImage!=null && imgUrl.length()>0){
            Picasso.with(context).load(this.imgUrl).into(adImage);
        }
    }
}
