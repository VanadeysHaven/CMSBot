package me.Cooltimmetje.CMSBot.Timers;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;
import me.Cooltimmetje.CMSBot.Profiles.CMSViewer;
import me.Cooltimmetje.CMSBot.Profiles.MySqlManager;
import me.Cooltimmetje.CMSBot.Profiles.ProfileManager;
import me.Cooltimmetje.CMSBot.Utilities.Logger;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Used for saving and unloading of profiles.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class ProfileTimer extends TimerTask {

    @Override
    public void run(){
        Logger.info("Saving and cleaning memory...");

        ArrayList<String> clear = new ArrayList<>();
        for(String s : ProfileManager.profiles.keySet()){
            CMSViewer viewer = ProfileManager.getViewer(s, true);
            MySqlManager.saveUserData(viewer);
            if(viewer.presentIn.isEmpty()){
                clear.add(s);
            }
        }

        for(String s : clear){
            ProfileManager.clearProfile(s);
        }
    }

}
