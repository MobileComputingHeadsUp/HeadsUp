package group15.computing.mobile.headsup.Forms;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bradley on 4/12/2016.
 */
public class SpaceProfile {

    static class Data{
        protected ArrayList<HashMap<String, String>> dropdowns;
        protected ArrayList<HashMap<String, String[]>> checkAllThatApplies;
        protected ArrayList<HashMap<String, String>> freeResponses;

        public Data() {
            this.dropdowns = new ArrayList<>();
            this.checkAllThatApplies = new ArrayList<>();
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

    public void addDropdown(String key, String value){
        HashMap<String, String> item = new HashMap<>();
        item.put(key, value);
        data.dropdowns.add(item);
    }

    public void addCheck(String key, String[] value){
        HashMap<String, String[]> item = new HashMap<>();
        item.put(key, value);
        data.checkAllThatApplies.add(item);
    }

    public void addFreeResponse(String key, String value){
        HashMap<String, String> item = new HashMap<>();
        item.put(key, value);
        data.freeResponses.add(item);
    }

    public void clearData(){
        this.data = new Data();
    }

    public ArrayList<HashMap<String, String>> getDropdowns() {
        return data.dropdowns;
    }

    public ArrayList<HashMap<String, String[]>> getCheckAllThatApplies() {
        return data.checkAllThatApplies;
    }

    public ArrayList<HashMap<String, String>> getFreeResponses() {
        return data.freeResponses;
    }


}
