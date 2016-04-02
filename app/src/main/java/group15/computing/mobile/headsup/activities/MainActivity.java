package group15.computing.mobile.headsup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import group15.computing.mobile.headsup.Auth.Authentication;
import group15.computing.mobile.headsup.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Used for debugging purposes.
    private Button findButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findButton = (Button) findViewById(R.id.findBeacons);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the BeaconRangingActivity
                Log.d(TAG, "Going to ranging activity");
                Intent i = new Intent(MainActivity.this, BeaconRangingActivity.class);
                startActivity(i);
//                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        if(!Authentication.getInstance().isSignedIn()){
            Log.d(TAG, "IS NOT LOGGED IN");
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
        }else{
            Log.d(TAG, "IS LOGGED IN");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
