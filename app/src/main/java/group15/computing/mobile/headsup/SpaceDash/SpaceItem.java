package group15.computing.mobile.headsup.SpaceDash;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bradley on 4/16/16.
 */
public abstract class SpaceItem {


    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public SpaceItem(long timestamp){
        this.timestamp = timestamp;
    }

    public abstract View getInflatedLayout(ViewGroup parent);

    public abstract void configView(View convertView);
}
