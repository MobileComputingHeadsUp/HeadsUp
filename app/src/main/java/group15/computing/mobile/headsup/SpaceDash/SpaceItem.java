package group15.computing.mobile.headsup.SpaceDash;

import android.content.Context;
import android.view.View;

/**
 * Created by Bradley on 4/16/16.
 */
public abstract class SpaceItem {

    public enum SpaceItemType{
        USER,
        AD,
        ANNOUNCEMENT,
        SENSOR
    }

    private SpaceItemType type;

    public SpaceItem(SpaceItemType type){
        this.type = type;
    }

    public SpaceItemType getType(){
        return this.type;
    }

    public abstract View getInflatedLayout(Context context);

    public abstract void configView(View convertView);
}
