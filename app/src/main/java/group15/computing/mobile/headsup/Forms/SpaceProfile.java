package group15.computing.mobile.headsup.Forms;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bradley on 4/12/2016.
 */
public class SpaceProfile {

    static class Data{
        protected ArrayList<HashMap<String, String>> dropdowns;
        protected ArrayList<HashMap<String, String[]>> checkAllThatApplys;
        protected ArrayList<HashMap<String, String>> freeResponses;

        public Data() {
            this.dropdowns = new ArrayList<>();
            this.checkAllThatApplys = new ArrayList<>();
            this.freeResponses = new ArrayList<>();
        }

    }

    private String spaceID;
    private int requiredUserInfoVersion;
    private Data data;

    public SpaceProfile(String spaceID, int requiredUserInfoVersion){
        this.spaceID = spaceID;
        this.requiredUserInfoVersion = requiredUserInfoVersion;
        this.data = new Data();
    }

    public void addDropdown(HashMap<String, String> dropdown){
        data.dropdowns.add(dropdown);
    }

    public void addCheck(HashMap<String, String[]> check){
        data.checkAllThatApplys.add(check);
    }

    public void addFreeResponse(HashMap<String, String> freeResponse){
        data.freeResponses.add(freeResponse);
    }

    public void clearData(){
        this.data = new Data();
    }
}
