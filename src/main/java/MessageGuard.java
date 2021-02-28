import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MessageGuard extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        if (!event.getChannel().getName().equals("zapisy-among-us")) {
            return;
        }

        String message = event.getMessage().getContentDisplay();
        message = message.split(" ")[0].toLowerCase();

        switch(message) {
            case "help":
                new HelpCallReceived(event);
                break;
            case "activelobbies":
                new ActiveLobbiesCallReceived(event);
                break;
            case "amonglobby":
                new AmongLobbyCallReceived(event);
                break;
            case "join":
                new JoinLobbyCallReceived(event);
                break;
            case "leave":
                new LeaveLobbyCallReceived(event);
                break;
            case "deleteamonglobby":
                new DeleteAmongLobbyCallReceived(event);
                break;
            case "lobbyinfo":
                new LobbyInfoCallReceived(event);
                break;
            case "kick":
                new KickFromLobbyCallReceived(event);
                break;
            default:
                event.getMessage().delete().queue();
                event.getChannel().sendMessage("Taka komenda nie istnieje. Jeżeli nie znasz składni komend wpisz \"help\" ").queue();
        }
    }
}

