package me.Cooltimmetje.CMSBot.Commands;

import me.Cooltimmetje.CMSBot.Main;
import me.Cooltimmetje.CMSBot.Profiles.CMSViewer;
import me.Cooltimmetje.CMSBot.Profiles.ProfileManager;

/**
 * Used to enter into the giveaway. People will not be eligible to win unless they enter into the giveaway.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class GiveawayEnter {

    public static void run(String sender, String channel){
        CMSViewer viewer = ProfileManager.getViewer(sender, true);
        boolean hasEntered = viewer.isEntered();
        viewer.setEntered(true);

        if(!hasEntered){
            Main.getCmsBotTwitch().send(sender + " -> You have entered into the giveaway!", channel);
        } else {
            Main.getCmsBotTwitch().send(sender + " -> You were already entered into the giveaway!", channel);
        }
    }

}
