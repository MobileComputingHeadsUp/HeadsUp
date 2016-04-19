package group15.computing.mobile.headsup.SpaceDash;

import com.google.gson.Gson;

public class HomeRecyclerViewFragment extends RecyclerViewFragment {

    @Override
    public void generateContentFromJson(String data) {

        Gson gson = new Gson();
        SpaceDashContent content = gson.fromJson(data, SpaceDashContent.class);

        for(AnnouncementSpaceItem announcement : content.getAnnouncementSpaceItems()){
            itemSorter.offer(announcement);
        }
        for(AdSpaceItem ad : content.getAdSpaceItems()){
            itemSorter.offer(ad);
        }
        for(UserSpaceItem user : content.getUserSpaceItems()){
            itemSorter.offer(user);
        }

        // Put the items into the content in order.
        while(!itemSorter.isEmpty()){
            mContentItems.add(itemSorter.poll());
        }
    }
}
