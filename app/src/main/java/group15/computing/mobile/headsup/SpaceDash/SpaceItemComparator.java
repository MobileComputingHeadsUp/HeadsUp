package group15.computing.mobile.headsup.SpaceDash;

import java.util.Comparator;

/**
 * Created by Bradley on 4/18/2016.
 */
public class SpaceItemComparator implements Comparator<SpaceItem> {
    public SpaceItemComparator(){}

    @Override
    public int compare(SpaceItem lhs, SpaceItem rhs) {
        return Long.compare(rhs.getTimestamp(), lhs.getTimestamp());
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
