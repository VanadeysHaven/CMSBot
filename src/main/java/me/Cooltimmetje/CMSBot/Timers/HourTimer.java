package me.Cooltimmetje.CMSBot.Timers;

import me.Cooltimmetje.CMSBot.Profiles.CMSViewer;
import me.Cooltimmetje.CMSBot.Profiles.ProfileManager;
import me.Cooltimmetje.CMSBot.Utilities.Logger;

import java.util.TimerTask;

/**
 * A timer that runs every 6 minutes to count hours.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class HourTimer extends TimerTask {

    @Override
    public void run() {
        Logger.info("Counting hours...");

        for(CMSViewer viewer : ProfileManager.profiles.values()){
            for(String s : viewer.getPresentIn()){
                if(!viewer.isOptedOut()) {
                    if (viewer.getActiveIn().contains(s)) {
                        viewer.getActiveHours().put(s, viewer.getActiveHours().get(s) + 0.1);
                    } else {
                        viewer.getInactiveHours().put(s, viewer.getInactiveHours().get(s) + 0.1);
                    }
                }
            }
            viewer.getActiveIn().clear();
        }
    }

}
