package group15.computing.mobile.headsup.fragments;

import com.google.gson.Gson;

import group15.computing.mobile.headsup.SpaceDash.AdSpaceItem;
import group15.computing.mobile.headsup.SpaceDash.AnnouncementSpaceItem;
import group15.computing.mobile.headsup.SpaceDash.SpaceDashContent;
import group15.computing.mobile.headsup.SpaceDash.UserSpaceItem;

public class UsersRecyclerViewFragment extends RecyclerViewFragment {

    @Override
    public void generateContentFromJson(String data) {

        Gson gson = new Gson();
        SpaceDashContent content = gson.fromJson(data, SpaceDashContent.class);

        for(UserSpaceItem user : content.getUserSpaceItems()){
            mContentItems.add(user);
        }
    }
}
