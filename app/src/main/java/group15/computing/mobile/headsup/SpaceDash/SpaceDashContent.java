package group15.computing.mobile.headsup.SpaceDash;

import java.util.ArrayList;

import group15.computing.mobile.headsup.Auth.User;

/**
 * Created by Bradley on 4/16/16.
 */
public class SpaceDashContent {

    private Space space;
    private ArrayList<AdSpaceItem> ads;
    private ArrayList<AnnouncementSpaceItem> announcments; // Sergio made me do this
    private ArrayList<UserSpaceItem> users;
    private ArrayList<MatchedUserSpaceItem> matches;


    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public ArrayList<MatchedUserSpaceItem> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<MatchedUserSpaceItem> matches) {
        this.matches = matches;
    }

    public ArrayList<AdSpaceItem> getAdSpaceItems() {
        return ads;
    }

    public void setAdSpaceItems(ArrayList<AdSpaceItem> ads) {
        this.ads = ads;
    }

    public ArrayList<AnnouncementSpaceItem> getAnnouncementSpaceItems() {
        return announcments;
    }

    public void setAnnouncementSpaceItems(ArrayList<AnnouncementSpaceItem> announcements) {
        this.announcments = announcements;
    }

    public ArrayList<UserSpaceItem> getUserSpaceItems() {
        return users;
    }

    public void setUserSpaceItems(ArrayList<UserSpaceItem> users) {
        this.users = users;
    }
}
