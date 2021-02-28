import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ActiveLobbiesCallReceived {

    ActiveLobbiesCallReceived(GuildMessageReceivedEvent event) {


        String str = event.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = event.getChannel();
            if (SignInBot.lobbies.size() != 0) {
                String message = "";
                for (Lobby lobby : SignInBot.lobbies) {
                    message = message + "Among us lobby godzina: " + lobby.getTime() + " Twórca lobby: " + lobby.getLobbyAdmin().getName() + "\n";
                }
                textchannel.sendMessage(message + "Jeżeli chcesz więcej informacji na temat lobby wpisz \"lobbyinfo <godzina> <twórca>\" np: lobbyinfo 13:00 Kraton" ).queue();
            }
            else {
                textchannel.sendMessage("Nie ma aktywnych lobby, ale bądz rebeliantem i załóż swoje :muscle:").queue();
            }


    }
}
