package group15.computing.mobile.headsup.SpaceDash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group15.computing.mobile.headsup.R;

/**
 * Created by Bradley on 4/16/16.
 */
public class AnnouncementSpaceItem extends SpaceItem {

    private String text;

    public AnnouncementSpaceItem(String text){
        super(SpaceItemType.ANNOUNCEMENT);
        this.text = text;
    }

    @Override
    public View getInflatedLayout(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_space_item, parent, false );
    }

    @Override
    public void configView(View convertView) {

        TextView text = (TextView) convertView.findViewById(R.id.announcementMessage);

        if(text!=null){
            text.setText(this.text);
        }
    }
}
