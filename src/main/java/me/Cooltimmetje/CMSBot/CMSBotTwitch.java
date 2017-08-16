package me.Cooltimmetje.CMSBot;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import me.Cooltimmetje.CMSBot.Commands.GiveawayEnter;
import me.Cooltimmetje.CMSBot.Commands.GiveawayOptOut;
import me.Cooltimmetje.CMSBot.Profiles.CMSViewer;
import me.Cooltimmetje.CMSBot.Profiles.ProfileManager;
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
        this.setMessageDelay(1000);
    }

    /**
     * Connect event.
     */
    @Override
    protected void onConnect(){
        Logger.info("Connected to Twitch, Joining channels...");

        joinChannel("#" + Constants.twitchBot);
        sendRawLine("CAP REQ :twitch.tv/membership");
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
     * Message received event.
     *
     * @param channel Channel where the message was received.
     * @param sender Sender of the message.
     * @param login Our login.
     * @param hostname Hostname of the channel.
     * @param message Message content.
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if(Constants.botAccounts.contains(sender)){
            Logger.info(sender + " is on the bot list, ignoring...");
            return;
        }

        CMSViewer viewer = ProfileManager.getViewer(sender, true);
        if (!viewer.getActiveIn().contains(channel.substring(1))) {
            viewer.getActiveIn().add(channel.substring(1));
        }
        if (!viewer.getPresentIn().contains(channel.substring(1))) {
            viewer.getPresentIn().add(channel.substring(1));
        }
        if (!viewer.getActiveHours().containsKey(channel.substring(1))) {
            viewer.getActiveHours().put(channel.substring(1), 0.0);
            viewer.getInactiveHours().put(channel.substring(1), 0.0);
        }

        Logger.info(channel + " | " + sender + ": " + message);

        if(message.toLowerCase().startsWith("!giveaway enter")){
            GiveawayEnter.run(sender, channel);
        } else if (message.toLowerCase().startsWith("!giveaway optout")){
            GiveawayOptOut.run(sender, channel);
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
        Logger.info(sender + " joined channel: " + channel);

        if(Constants.botAccounts.contains(sender)){
            Logger.info(sender + " is on the bot list, ignoring...");
            return;
        }

        CMSViewer viewer = ProfileManager.getViewer(sender, true);
        if(!viewer.getPresentIn().contains(channel.substring(1))) {
            viewer.getPresentIn().add(channel.substring(1));
        }
        if (!viewer.getActiveHours().containsKey(channel.substring(1))) {
            viewer.getActiveHours().put(channel.substring(1), 0.0);
            viewer.getInactiveHours().put(channel.substring(1), 0.0);
        }
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
        Logger.info(sender + " left channel: " + channel);

        if(Constants.botAccounts.contains(sender)){
            Logger.info(sender + " is on the bot list, ignoring...");
            return;
        }

        ProfileManager.getViewer(sender, true).getPresentIn().remove(channel.substring(1));
        ProfileManager.getViewer(sender, true).getActiveIn().remove(channel.substring(1));
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
        sendMessage(channel, message);
    }

    /**
     * Join all channels.
     */
    public void joinChannels(){
        for(String string : Constants.twitchChannels){
            join(string);
        }
    }


}
