package me.Cooltimmetje.CMSBot;

import me.Cooltimmetje.CMSBot.Profiles.MySqlManager;
import me.Cooltimmetje.CMSBot.Timers.HourTimer;
import me.Cooltimmetje.CMSBot.Timers.ProfileTimer;
import me.Cooltimmetje.CMSBot.Utilities.Constants;
import org.jibble.pircbot.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.util.DiscordException;

import java.io.IOException;
import java.util.Timer;

/**
 * Class for booting the bot and loading other stuff.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class Main {

    /**
     * Discord bot instance.
     */
    private static CMSBot cmsBot;
    /**
     * Logger instance.
     */
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    /**
     * Twitch bot instance.
     */
    private static CMSBotTwitch cmsBotTwitch;
    /**
     * Timer instance for tracking hours.
     */
    private static Timer timer = new Timer();

    /**
     * Start-up code.
     *
     * @param args Arguments for logging in.
     */
    public static void main(String[] args){
        if(args.length < 5){
            throw new IllegalArgumentException("I need a Discord Token, Mysql Username, Mysql Password, Twitch Username and Twitch Token.");
        } else {
            log.info("Setting up...");
            log.info("Hello world!");
            cmsBot = new CMSBot(args[0]);

            MySqlManager.setupHikari(args[1],args[2]);
            Constants.twitchBot = args[3];
            Constants.twitchOauth = args[4];
        }
        log.info("Connecting to Twitch.");

        cmsBotTwitch = new CMSBotTwitch();

        try {
            cmsBotTwitch.connect("irc.twitch.tv", 6667, Constants.twitchOauth);
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }

        log.info("All systems operational. Ready to connect to Discord.");
        try {
            cmsBot.login();
        } catch (DiscordException e) {
            e.printStackTrace();
        }

        timer.schedule(new HourTimer(), Constants.HOUR_COUNT_DELAY, Constants.HOUR_COUNT_DELAY);
        timer.schedule(new ProfileTimer(), 600000, 600000);

    }


    /**
     * Used for obtaining the Discord bot instance.
     *
     * @return The CMSBot instance.
     */
    public static CMSBot getInstance(){
        return cmsBot;
    }

    /**
     * Used for obtaining the Twitch bot instance.
     *
     * @return The CMSBotTwitch instance.
     */
    public static CMSBotTwitch getCmsBotTwitch(){
        return cmsBotTwitch;
    }

}
