package me.Cooltimmetje.CMSBot.Commands;

import me.Cooltimmetje.CMSBot.Utilities.Constants;
import me.Cooltimmetje.CMSBot.Utilities.MessagesUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Managing of commands.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class CommandManager {

    @EventSubscriber
    public void onMessage(MessageReceivedEvent event){
        String msg = event.getMessage().getContent();

        if(msg.startsWith(Constants.commandInvoker + " ")){
            String command = msg.substring(Constants.commandInvoker.length() + 1);

            switch (command.split(" ")[0]){
                case "ping":
                    MessagesUtils.sendSuccess("PONG!", event.getChannel());
                    break;
                case "lookup":
                    RequestRecord.run(command, event.getMessage());
                    break;
                case "channels":
                    TwitchChannelManager.run(command, event.getMessage());
                    break;
                case "bots":
                    TwitchBotManager.run(command, event.getMessage());
                    break;
            }
        }
    }

}
