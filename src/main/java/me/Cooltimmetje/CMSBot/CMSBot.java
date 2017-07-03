package me.Cooltimmetje.CMSBot;

import me.Cooltimmetje.CMSBot.Utilities.Constants;
import me.Cooltimmetje.CMSBot.Utilities.Logger;
import me.Cooltimmetje.CMSBot.Utilities.MessagesUtils;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent;
import sx.blah.discord.util.DiscordException;

/**
 * Class for holding the bot instance.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class CMSBot {

    /**
     * Discord Client instance.
     */
    private volatile IDiscordClient cmsBot;
    /**
     * Discord token.
     */
    private String token;
    /**
     * Defines if the listeners are ready that need to be ready for the "ready" event.
     */
    private boolean preReadyListenersReady = false;
    /**
     * Defines if the normal listeners are ready.
     */
    private boolean listenersReady = false;

    /**
     * Used for constructing the instance.
     *
     * @param token The Discord bot token.
     */
    public CMSBot(String token) {
        this.token = token;
    }

    /**
     * Used to build the client and logging in.
     *
     * @throws DiscordException Exception that may be thrown when something goes wrong.
     */
    public void login() throws DiscordException {
        cmsBot = new ClientBuilder().withToken(token).setMaxReconnectAttempts(3).login();
        if(!preReadyListenersReady){
            cmsBot.getDispatcher().registerListener(this);

            preReadyListenersReady = true;
        }
}

    /**
     * Event that gets called when the bot is logged in and ready.
     *
     * @param event The event instance.
     */
    @EventSubscriber
    public void onReady(ReadyEvent event){
        if(!listenersReady){
            event.getClient().changePlayingText("boop");

            listenersReady = true;
//            Runtime.getRuntime().addShutdownHook(new Thread(() -> ));
            MessagesUtils.sendPlain(":robot: Startup sequence complete!", cmsBot.getChannelByID(Constants.LOG_CHANNEL), false);
        }
    }

    /**
     * Used for logging out the bot from Discord chat.
     *
     * @param event The event instance.
     */
    @EventSubscriber
    public void onMention(MentionEvent event){
        if(event.getMessage().getContent().split(" ").length > 1) {
            if (event.getMessage().getContent().split(" ")[1].equalsIgnoreCase("logout")) {
                if (event.getMessage().getAuthor().getStringID().equals(Constants.TIMMY_OVERRIDE) || event.getMessage().getAuthor().getStringID().equals(Constants.JASCH_OVERRIDE)) {
                    MessagesUtils.sendSuccess("Well, okay then...\n`Shutting down...`", event.getMessage().getChannel());

                    terminate(false);
                } else {
                    MessagesUtils.sendError("Ur not timmy >=(", event.getMessage().getChannel());
                }
            }
        }
    }

    /**
     * Used to terminate the bot and log it out.
     *
     * @param sigterm Defines if the terminate was from SIGTERM.
     */
    public void terminate(boolean sigterm) {
        if (sigterm) {
            MessagesUtils.sendPlain(":robot: Logging out due to SIGTERM...", Main.getInstance().getCmsBot().getChannelByID(Constants.LOG_CHANNEL), false);
        } else {
            MessagesUtils.sendPlain(":robot: Logging out due to command...", Main.getInstance().getCmsBot().getChannelByID(Constants.LOG_CHANNEL), false);
        }

        try {
            Main.getCmsBotTwitch().terminate();
            cmsBot.logout();
        } catch (DiscordException e) {
            Logger.warn("Couldn't log out.", e);
        }
    }

    public IDiscordClient getCmsBot(){
        return cmsBot;
    }
}
