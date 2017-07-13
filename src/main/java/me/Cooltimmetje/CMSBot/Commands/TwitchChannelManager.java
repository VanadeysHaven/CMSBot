package me.Cooltimmetje.CMSBot.Commands;

import me.Cooltimmetje.CMSBot.Enums.EmojiEnum;
import me.Cooltimmetje.CMSBot.Main;
import me.Cooltimmetje.CMSBot.Profiles.MySqlManager;
import me.Cooltimmetje.CMSBot.Utilities.Constants;
import me.Cooltimmetje.CMSBot.Utilities.MessagesUtils;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Used for adding and removing Twitch channels.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class TwitchChannelManager {

    public static void run(String command, IMessage message) {
        String[] args = command.split(" ");
        if (message.getGuild().getStringID().equals(Constants.CMS_SERVER)) {
            if (message.getAuthor().getRolesForGuild(message.getGuild()).contains(message.getGuild().getRolesByName(Constants.ADMIN_ROLE).get(0))) {
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "add":
                            if (args.length > 2) {
                                if (!Constants.twitchChannels.contains(args[2])) {
                                    Constants.twitchChannels.add(args[2]);
                                    Main.getCmsBotTwitch().join(args[2]);

                                    MySqlManager.addChannel(args[2]);
                                    MessagesUtils.addReaction(message, "Channel added: " + args[2], EmojiEnum.WHITE_CHECK_MARK);
                                } else {
                                    MessagesUtils.addReaction(message, "Channel already added.", EmojiEnum.X);
                                }
                            } else {
                                MessagesUtils.addReaction(message, "Missing arguments, usage: cms channels add <channel>", EmojiEnum.X);
                            }
                            break;
                        case "remove":
                            if (args.length > 2) {
                                if (Constants.twitchChannels.contains(args[2])) {
                                    Constants.twitchChannels.remove(args[2]);
                                    Main.getCmsBotTwitch().part(args[2]);

                                    MySqlManager.removeChannel(args[2]);
                                    MessagesUtils.addReaction(message, "Channel removed: " + args[2], EmojiEnum.WHITE_CHECK_MARK);
                                } else {
                                    MessagesUtils.addReaction(message, "Channel not added.", EmojiEnum.X);
                                }
                            } else {
                                MessagesUtils.addReaction(message, "Missing arguments, usage: cms channels remove <channel>", EmojiEnum.X);
                            }
                            break;
                        case "list":
                            StringBuilder sb = new StringBuilder();
                            for (String s : Constants.twitchChannels) {
                                sb.append(s).append(", ");
                            }

                            String channels = sb.toString();
                            MessagesUtils.sendPlain("The bot is currently in " + Constants.twitchChannels.size() + " channels.\n```\n" +
                                    channels.substring(0, channels.length() - 2) + "\n```", message.getChannel(), false);
                            break;
                    }
                } else {
                    MessagesUtils.addReaction(message, "Missing arguments, usage: cms channels <add/remove/list> <channel>", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReaction(message, "You do not have permission do this.", EmojiEnum.X);
            }
        } else {
            MessagesUtils.addReaction(message, "This command can only be done on the main CMS server.", EmojiEnum.X);
        }
    }

}
