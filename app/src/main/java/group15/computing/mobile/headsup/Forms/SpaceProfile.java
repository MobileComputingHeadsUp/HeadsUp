package group15.computing.mobile.headsup.Forms;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bradley on 4/12/2016.
 */
public class SpaceProfile {

    static class Data{
        protected HashMap<String, String> dropdowns;
        protected HashMap<String, String[]> checkAllThatApplies;
        protected HashMap<String, String> freeResponses;

        public Data() {
            this.dropdowns = new HashMap<>();
            this.checkAllThatApplies = new HashMap<>();
            this.freeResponses = new HashMap<>();
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

    public void addDropdown(String key, String value){
        data.dropdowns.put(key, value);
    }

    public void addCheck(String key, String[] value){
        data.checkAllThatApplies.put(key, value);
    }

    public void addFreeResponse(String key, String value){
        data.freeResponses.put(key, value);
    }

    public void clearData(){
        this.data = new Data();
    }

    public HashMap<String, String> getDropdowns() {
        return data.dropdowns;
    }

    public HashMap<String, String[]> getCheckAllThatApplies() {
        return data.checkAllThatApplies;
    }

    public HashMap<String, String> getFreeResponses() {
        return data.freeResponses;
    }


}
