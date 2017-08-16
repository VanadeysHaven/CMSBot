package me.Cooltimmetje.CMSBot.Commands;

import me.Cooltimmetje.CMSBot.Enums.EmojiEnum;
import me.Cooltimmetje.CMSBot.Main;
import me.Cooltimmetje.CMSBot.Profiles.MySqlManager;
import me.Cooltimmetje.CMSBot.Profiles.ProfileManager;
import me.Cooltimmetje.CMSBot.Utilities.Constants;
import me.Cooltimmetje.CMSBot.Utilities.MessagesUtils;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Used to add and remove other bots, these accounts will be completely ignored by the bot!
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class TwitchBotManager {

    public static void run(String command, IMessage message) {
        String[] args = command.split(" ");
        if (message.getGuild().getStringID().equals(Constants.CMS_SERVER)) {
            if (message.getAuthor().getRolesForGuild(message.getGuild()).contains(message.getGuild().getRolesByName(Constants.ADMIN_ROLE).get(0))) {
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "add":
                            if (args.length > 2) {
                                if (!Constants.botAccounts.contains(args[2])) {
                                    Constants.botAccounts.add(args[2]);

                                    MySqlManager.addBot(args[2]);
                                    ProfileManager.clearProfile(args[2]);
                                    MessagesUtils.addReaction(message, "Bot added: " + args[2], EmojiEnum.WHITE_CHECK_MARK);
                                } else {
                                    MessagesUtils.addReaction(message, "Bot already added.", EmojiEnum.X);
                                }
                            } else {
                                MessagesUtils.addReaction(message, "Missing arguments, usage: cms bots add <bot name>", EmojiEnum.X);
                            }
                            break;
                        case "remove":
                            if (args.length > 2) {
                                if (Constants.botAccounts.contains(args[2])) {
                                    Constants.botAccounts.remove(args[2]);

                                    MySqlManager.removeBot(args[2]);
                                    MessagesUtils.addReaction(message, "Bot removed: " + args[2], EmojiEnum.WHITE_CHECK_MARK);
                                } else {
                                    MessagesUtils.addReaction(message, "Bot not added.", EmojiEnum.X);
                                }
                            } else {
                                MessagesUtils.addReaction(message, "Missing arguments, usage: cms bots remove <bot name>", EmojiEnum.X);
                            }
                            break;
                        case "list":
                            StringBuilder sb = new StringBuilder();
                            for (String s : Constants.botAccounts) {
                                sb.append(s).append(", ");
                            }

                            String channels = sb.toString();
                            MessagesUtils.sendPlain("There are currently " + Constants.botAccounts.size() + " bots banned from tracking.\n```\n" +
                                    channels.substring(0, channels.length() - 2) + "\n```", message.getChannel(), false);
                            break;
                    }
                } else {
                    MessagesUtils.addReaction(message, "Missing arguments, usage: cms bots <add/remove/list> <bot name>", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReaction(message, "You do not have permission do this.", EmojiEnum.X);
            }
        } else {
            MessagesUtils.addReaction(message, "This command can only be done on the main CMS server.", EmojiEnum.X);
        }
    }
    
}
