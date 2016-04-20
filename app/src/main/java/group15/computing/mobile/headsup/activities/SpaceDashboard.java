package group15.computing.mobile.headsup.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.gson.Gson;

import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.SpaceDash.AnnouncementsRecyclerViewFragment;
import group15.computing.mobile.headsup.SpaceDash.HomeRecyclerViewFragment;
import group15.computing.mobile.headsup.SpaceDash.SpaceDashContent;
import group15.computing.mobile.headsup.SpaceDash.UsersRecyclerViewFragment;
import group15.computing.mobile.headsup.utilities.Utilities;

public class SpaceDashboard extends AppCompatActivity {

    public static String DATA = "space_data";

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;
    private TextView headerLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_dashboard);

        // Get the feed Data.
        final String feedData = Utilities.loadJSONFromAsset("dummy_space_feed.json", SpaceDashboard.this);

        // Convert the jsonData to a content object.
        Gson gson = new Gson();
        SpaceDashContent content = gson.fromJson(feedData, SpaceDashContent.class);

        // Set the header text
        headerLogo = (TextView) findViewById(R.id.space_dash_header);
        headerLogo.setText(content.getSpace_name());

        // Get the ViewPager
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        // Get the toolbar and DrawerLayout
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

                // Setup fragment arguments. They need the data!
                Bundle bundle = new Bundle();
                bundle.putString(DATA, feedData);

                switch (position % 3) {
                    case 0:
                        HomeRecyclerViewFragment home = new HomeRecyclerViewFragment();
                        home.setArguments(bundle);
                        return home;
                    case 1:
                        UsersRecyclerViewFragment users = new UsersRecyclerViewFragment();
                        users.setArguments(bundle);
                        return users;
                    default:
                        AnnouncementsRecyclerViewFragment announcements = new AnnouncementsRecyclerViewFragment();
                        announcements.setArguments(bundle);
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
                                "http://protiumdesign.com/wp-content/uploads/2015/04/Material-Design-Background-1024.jpg");
                }
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }
}
