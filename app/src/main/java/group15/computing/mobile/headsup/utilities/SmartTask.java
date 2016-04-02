package group15.computing.mobile.headsup.utilities;

import java.util.TimerTask;

/**
 * Created by Bradley on 4/2/2016.
 */
public class SmartTask extends TimerTask {
    private boolean isRunning;
    
    public SmartTask(){
        isRunning = false;
    }

    @Override
    public boolean cancel() {
        // Will only cancel itself if it is running.
        if(isRunning){
            isRunning = false;
            return super.cancel();
        }
        return false;
    }

    @Override
    public void run() {
        isRunning = true;
    }

    public boolean isRunning(){
        return isRunning;
    }
}
