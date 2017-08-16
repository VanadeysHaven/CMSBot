package me.Cooltimmetje.CMSBot.Commands;

import me.Cooltimmetje.CMSBot.Main;
import me.Cooltimmetje.CMSBot.Profiles.CMSViewer;
import me.Cooltimmetje.CMSBot.Profiles.ProfileManager;

/**
 * Used to opt-out from tracking. Required by Twitch.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class GiveawayOptOut {


    public static void run(String sender, String channel) {
        CMSViewer viewer = ProfileManager.getViewer(sender, true);
        boolean wasOptedOut = viewer.isOptedOut();
        viewer.setOptedOut(!viewer.isOptedOut());

        if(wasOptedOut){
            Main.getCmsBotTwitch().send(sender + " -> Your hour tracking has been resumed, you may disable it at any time again by typing \"!giveaway optout\".", channel);
        } else {
            Main.getCmsBotTwitch().send(sender + " -> Your hour tracking has been paused, you may re-enable it at any time again by typing \"!giveaway optout\". - WARNING: This might make you ineligible for the current and any future giveaway!", channel);
        }
    }
}
