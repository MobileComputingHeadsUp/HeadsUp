package group15.computing.mobile.headsup.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.SpaceDash.AnnouncementsRecyclerViewFragment;
import group15.computing.mobile.headsup.SpaceDash.HomeRecyclerViewFragment;
import group15.computing.mobile.headsup.SpaceDash.SpaceDashContent;
import group15.computing.mobile.headsup.SpaceDash.UsersRecyclerViewFragment;
import group15.computing.mobile.headsup.beacon_detection.BeaconEvent;
import group15.computing.mobile.headsup.beacon_detection.RequestedAction;
import group15.computing.mobile.headsup.utilities.APIClient;
import group15.computing.mobile.headsup.utilities.Constants;
import group15.computing.mobile.headsup.utilities.Utilities;

public class SpaceDashboard extends AppCompatActivity {

    public static String TAG = "Space Dashboard";
    public static String DATA = "space_data";

    private MaterialViewPager mViewPager;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;
    private TextView headerLogo;

    private IntentFilter beaconIntentFilter;
    private BroadcastReceiver beaconBroadcastReceiver;
    private boolean beaconsFound;

    HomeRecyclerViewFragment home;
    AnnouncementsRecyclerViewFragment announcements;
    UsersRecyclerViewFragment users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_dashboard);

        if (savedInstanceState == null) {

            // Setup the recycler fragments only if they have not already been created.
            home = new HomeRecyclerViewFragment();
            users = new UsersRecyclerViewFragment();
            announcements = new AnnouncementsRecyclerViewFragment();

            // Give it a reference to this so the refresh swipers can init a refresh
            home.setParentActivity(this);
            users.setParentActivity(this);
            announcements.setParentActivity(this);
        }

        // Get all the views.
        headerLogo = (TextView) findViewById(R.id.space_dash_header);
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Configure the toolbar.
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        // Set the Drawer Listener.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        // Set the Adapter for the View Pager
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 3) {
                    case 0:
                        return home;
                    case 1:
                        return users;
                    default:
                        return announcements;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "Home"; // Announcements, Adds, Matched Users, Sensor stuff
                    case 1:
                        return "Users"; // Every user in the space.
                    case 2:
                        return "Announcements"; // Announcements and Adds
                }
                return "";
            }
        });


        // This changes the color/ image when switching between tabs.
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
//                    case 0:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.blue,
//                                "http://www.wallpaperhi.com/thumbnails/detail/20140629/clouds%20nature%20planets%20earth%20low%20resolution_www.wallpaperhi.com_38.jpg");
//                    case 1:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.green,
//                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
//                    case 2:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.cyan,
//                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    default:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,
                                "http://apple.wallpapersfine.com/wallpapers/original/750x1334/w-41.jpg");
                }
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        // Sync the state!
        mDrawerToggle.syncState();
        setupBeaconEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the feed Data.
        initFeedRefresh();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    public void initFeedRefresh(){

        beaconsFound = false;
        home.startLoadingIcon();
        announcements.startLoadingIcon();
        users.startLoadingIcon();

        // Look for beacons real quick!
        startRanging();

        // Start a timer that will make the data request after a delay if no beacons are found. Im sure there is a more efficient way to do this.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!beaconsFound){
                    stopRanging();
                    requestFeedData("");
                }
            }
        }, 5000);
    }

    private void refreshFeed(final String feedData){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  Update the fragments
                home.refreshContent(feedData);
                users.refreshContent(feedData);
                announcements.refreshContent(feedData);

                // Convert the jsonData to a content object.
                Gson gson = new Gson();
                SpaceDashContent content = gson.fromJson(feedData, SpaceDashContent.class);

                // Set the title.
                headerLogo.setText(content.getSpace_name());
            }
        });
    }

    private void startRanging(){
        // Start ranging.
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region(Constants.ALL_BEACONS_RANGE_ID, null, null, null);
        try{
            beaconManager.startRangingBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        // Register the receiver.
        registerReceiver(beaconBroadcastReceiver, beaconIntentFilter);
    }

    private void stopRanging(){
        // Stop ranging.
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region(Constants.ALL_BEACONS_RANGE_ID, null, null, null);
        try{
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        // Unregister the receiver
        try{
            unregisterReceiver(beaconBroadcastReceiver);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void setupBeaconEventListener(){
        // Register the beacons broadcast listener
        beaconIntentFilter = new IntentFilter(Constants.BEACONS_FOUND);
        beaconBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                beaconsFound = true;
                String beaconArrayJson = intent.getStringExtra(BeaconEvent.DATA);
                Log.d(TAG, "Found beacons.");
                Log.d(TAG, beaconArrayJson);

                stopRanging();
                requestFeedData(beaconArrayJson);
            }
        };
    }

    private void requestFeedData(String beaconArray){
//
        final String feedData = Utilities.loadJSONFromAsset("dummy_space_feed.json", SpaceDashboard.this);
        refreshFeed(feedData);
//        APIClient.requestSpaceDashFeed(beaconArray, new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                refreshFeed(response.toString());
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                Log.d(TAG, "Failed to retrieve data.");
//            }
//        });
    }

    // Stop the user from going back after entering a space.
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
