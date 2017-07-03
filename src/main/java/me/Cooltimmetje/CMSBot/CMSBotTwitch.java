package me.Cooltimmetje.CMSBot;

import me.Cooltimmetje.CMSBot.Utilities.Constants;
import me.Cooltimmetje.CMSBot.Utilities.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

/**
 * Class for everything regarding Twitch IRC Chat.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class CMSBotTwitch extends PircBot {

    /**
     * Boolean that will prevent the bot from reconnecting upon shutdown.
     */
    private boolean terminated = false;

    /**
     * Creating the Twitch bot instance.
     */
    public CMSBotTwitch() {
        this.setName(Constants.twitchBot);
        this.setLogin(Constants.twitchBot);
    }

    /**
     * Connect event.
     */
    @Override
    protected void onConnect(){
        Logger.info("Connected to Twitch, Joining channels...");

        joinChannel("#" + Constants.twitchBot);
    }

    /**
     * Disconnect event, will attempt to reconnect when not terminated.
     */
    @Override
    protected void onDisconnect(){
        if(!terminated){
            try {
                reconnect();
            } catch (IOException | IrcException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Channel join event.
     *
     * @param channel IRC channel that was joined.
     * @param sender The user that joined.
     * @param login The login that joined.
     * @param hostname The hostname of the channel.
     */
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname){
        Logger.info("Joined channel: " + channel);
    }

    /**
     * Channel part event.
     *
     * @param channel IRC channel that was joined.
     * @param sender The user that joined.
     * @param login The login that joined.
     * @param hostname The hostname of the channel.
     */
    @Override
    protected void onPart(String channel, String sender, String login, String hostname){
        Logger.info("Left channel: " + channel);
    }

    /**
     * This will terminate the IRC bot.
     */
    public void terminate(){
        terminated = true;
        disconnect();
    }

    /**
     * Used to join a channel.
     *
     * @param channel The channel to join. (Without #)
     */
    public void join(String channel){
        this.joinChannel("#" + channel);
    }

    /**
     * Used to part a channel.
     *
     * @param channel The channel to part. (Without #)
     */
    public void part(String channel){
        this.partChannel("#" + channel);
    }

    /**
     * Used to send a message.
     *
     * @param message The message that we want to send.
     * @param channel The channel where we want to send.
     */
    public void send(String message, String channel){
        sendMessage("#" + channel, message);
    }

//    public void joinChannels(){
//        for(String string : ServerManager.twitchServers.keySet()){
//            Main.getSkuddbotTwitch().joinChannel("#" + string);
//        }
//    }


}
