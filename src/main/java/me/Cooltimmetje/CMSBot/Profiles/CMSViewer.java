package me.Cooltimmetje.CMSBot.Profiles;

import lombok.Getter;
import lombok.Setter;
import me.Cooltimmetje.CMSBot.Utilities.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Instance for a viewer, every single Twitch User get's a instance of this class.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
@Getter
@Setter
public class CMSViewer {

    JSONParser parser = new JSONParser();

    /**
     * The username of the viewer.
     */
    public String username;

    /**
     * List of channels where the viewer is present.
     */
    public ArrayList<String> presentIn = new ArrayList<>();
    /**
     * List of channels where the viewer is active.
     */
    public ArrayList<String> activeIn = new ArrayList<>();

    /**
     * The active hours of the viewer in the channels.
     */
    public HashMap<String,Double> activeHours = new HashMap<>();
    /**
     * The inactive hours of the viewer in the channels.
     */
    public HashMap<String,Double> inactiveHours = new HashMap<>();

    /**
     * When true, the person has entered in the current giveaway.
     */
    public boolean entered;
    /**
     * When true, we will not track hours from this person.
     */
    public boolean optedOut;

    /**
     * Constructing the viewer instance without existing data.
     *
     * @param username The username of the viewer.
     */
    public CMSViewer(String username){
        this.username = username;
        this.entered = false;
        this.optedOut = false;

        Logger.info("[ProfileCreate] Creating new profile for: " + username);
    }

    /**
     * Constructing the viewer instance with existing data.
     *
     * @param username The username of the viewer.
     * @param data JSON with the hours.
     */
    public CMSViewer(String username, String data) throws ParseException {
        this.username = username;

        JSONObject obj = (JSONObject) parser.parse(data);
        JSONArray hours = (JSONArray) obj.get("hours");
        Iterator<JSONObject> iterator = hours.iterator();
        while (iterator.hasNext()) {
            JSONObject obj1 = iterator.next();
            String streamer = obj1.get("streamer").toString();

            activeHours.put(streamer, Double.parseDouble(obj1.get("active").toString()));
            inactiveHours.put(streamer, Double.parseDouble(obj1.get("inactive").toString()));
        }

        if(obj.get("entered") != null){
            this.entered = Boolean.parseBoolean(obj.get("entered").toString());
        } else {
            this.entered = false;
        }
        if(obj.get("optedOut") != null){
            this.optedOut = Boolean.parseBoolean(obj.get("optedOut").toString());
        } else {
            this.optedOut = false;
        }


        Logger.info("[ProfileLoad] Loading profile for: " + username);
    }

    public String getJSON(){
        JSONObject obj = new JSONObject();

        JSONArray hours = new JSONArray();
        for(String s : activeHours.keySet()){
            JSONObject obj1 = new JSONObject();
            obj1.put("streamer", s);
            obj1.put("active", activeHours.get(s));
            obj1.put("inactive", inactiveHours.get(s));

            hours.add(obj1);
        }
        obj.put("hours", hours);
        obj.put("entered", this.entered);
        obj.put("optedOut", this.optedOut);

        return obj.toJSONString();
    }
}
