package group15.computing.mobile.headsup.SpaceDash;

import com.google.gson.Gson;

import group15.computing.mobile.headsup.SpaceDash.RecyclerViewFragment;
import group15.computing.mobile.headsup.SpaceDash.SpaceDashContent;
import group15.computing.mobile.headsup.SpaceDash.UserSpaceItem;

public class UsersRecyclerViewFragment extends RecyclerViewFragment {

    @Override
    public void generateContentFromJson(String data) {

        Gson gson = new Gson();
        SpaceDashContent content = gson.fromJson(data, SpaceDashContent.class);

        itemSorter.clear();
        mContentItems.clear();

        for(UserSpaceItem user : content.getUserSpaceItems()){
            itemSorter.offer(user);
        }
        for(MatchedUserSpaceItem matchedUser : content.getMatches()){
            itemSorter.offer(matchedUser);
        }

        // Put the items into the content in order.
        while(!itemSorter.isEmpty()){
            mContentItems.add(itemSorter.poll());
        }
    }
}
