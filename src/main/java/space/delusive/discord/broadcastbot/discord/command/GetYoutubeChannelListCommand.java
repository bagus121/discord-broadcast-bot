package space.delusive.discord.broadcastbot.discord.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.delusive.discord.broadcastbot.discord.util.GetChannelListCommandHelper;
import space.delusive.discord.broadcastbot.domain.YoutubeChannel;
import space.delusive.discord.broadcastbot.service.YoutubeService;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class GetYoutubeChannelListCommand extends Command {
    private final YoutubeService youtubeService;
    private final GetChannelListCommandHelper helper;

    private Map<String, String> messages;

    public GetYoutubeChannelListCommand(YoutubeService youtubeService,
                                        GetChannelListCommandHelper helper,
                                        @Value("${discord.bot.command.get.youtube.channel.list.name}") String name,
                                        @Value("${discord.bot.command.get.youtube.channel.list.help}") String help,
                                        @Value("${discord.bot.command.get.youtube.channel.list.aliases}") String[] aliases) {
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.youtubeService = youtubeService;
        this.helper = helper;
        super.name = name;
        super.help = help;
        super.aliases = aliases;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] textItems = youtubeService.getAllChannels().stream()
                .map(this::getFormattedMessage)
                .toArray(String[]::new);
        if (textItems.length == 0) {
            event.reply(messages.get("get.youtube.channel.list.no.channels.found"));
        } else {
            String caption = messages.get("get.youtube.channel.list.title.message");
            helper.getPaginator(textItems, caption).display(event.getChannel());
        }
    }

    private String getFormattedMessage(YoutubeChannel channel) {
        String roleName = helper.getRoleName(channel.getMentionRoleId());
        return messages.get("get.youtube.channel.list.message.pattern")
                .replaceAll("\\{channel_name}", channel.getChannelName())
                .replaceAll("\\{channel_id}", channel.getChannelId())
                .replaceAll("\\{mention_role}", roleName)
                .replaceAll("\\{uploads_playlist_id}", channel.getUploadsPlaylistId());
    }

    @Resource(name = "messages")
    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }
}
