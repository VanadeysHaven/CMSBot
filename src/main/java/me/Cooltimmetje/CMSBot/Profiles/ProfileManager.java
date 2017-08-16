package me.Cooltimmetje.CMSBot.Profiles;

import me.Cooltimmetje.CMSBot.Utilities.Logger;

import java.util.HashMap;

/**
 * Manager for profiles.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class ProfileManager {

    public static HashMap<String,CMSViewer> profiles = new HashMap<>();

    public static CMSViewer getViewer(String username, boolean createNew){
        CMSViewer viewer = profiles.get(username); //Get the viewer instance from memory.

        if(viewer != null){ //If there is a viewer in memory, return it.
            return viewer;
        } else { //If not, check database
            viewer = MySqlManager.getUserData(username); //Get viewer from the database.
            if(viewer == null && createNew){ //Check if it doesn't exist and if we should create a new one.
                viewer = new CMSViewer(username); //Create new instance
            }

            if(viewer == null) { //To return null when there isn't a existing user and we should NOT create a new one.
                return null;
            }

            profiles.put(username, viewer); //Add the loaded viewer to memory.
            return viewer; //Return it.
        }
    }

    public static void clearProfile(String username){
        profiles.remove(username);
        Logger.info("[ProfileClear] Cleared profile for: " + username);
    }

}
