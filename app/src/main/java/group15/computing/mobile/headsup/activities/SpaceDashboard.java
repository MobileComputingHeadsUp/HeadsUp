package group15.computing.mobile.headsup.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.fragments.AnnouncementsRecyclerViewFragment;
import group15.computing.mobile.headsup.fragments.HomeRecyclerViewFragment;
import group15.computing.mobile.headsup.fragments.RecyclerViewFragment;
import group15.computing.mobile.headsup.fragments.UsersRecyclerViewFragment;
import group15.computing.mobile.headsup.utilities.Utilities;

public class SpaceDashboard extends AppCompatActivity {

    public static String DATA = "space_data";

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_dashboard);

        // Get the feed Data.
        final String feedData = Utilities.loadJSONFromAsset("dummy_space_feed.json", SpaceDashboard.this);

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
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                }
                return null;
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
