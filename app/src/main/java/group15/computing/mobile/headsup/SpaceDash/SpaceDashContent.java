package group15.computing.mobile.headsup.SpaceDash;

import java.util.ArrayList;

import group15.computing.mobile.headsup.Auth.User;

/**
 * Created by Bradley on 4/16/16.
 */
public class SpaceDashContent {

    private String space_name;

    public String getSpace_name() {
        return space_name;
    }

    public void setSpace_name(String space_name) {
        this.space_name = space_name;
    }

    private ArrayList<AdSpaceItem> ads;
    private ArrayList<AnnouncementSpaceItem> announcements;
    private ArrayList<UserSpaceItem> users;

    public ArrayList<AdSpaceItem> getAdSpaceItems() {
        return ads;
    }

    public void setAdSpaceItems(ArrayList<AdSpaceItem> ads) {
        this.ads = ads;
    }

    public ArrayList<AnnouncementSpaceItem> getAnnouncementSpaceItems() {
        return announcements;
    }

    public void setAnnouncementSpaceItems(ArrayList<AnnouncementSpaceItem> announcements) {
        this.announcements = announcements;
    }

    public ArrayList<UserSpaceItem> getUserSpaceItems() {
        return users;
    }

    public void setUserSpaceItems(ArrayList<UserSpaceItem> users) {
        this.users = users;
    }
}
