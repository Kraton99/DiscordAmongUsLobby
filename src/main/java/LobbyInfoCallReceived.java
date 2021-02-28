import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.Arrays;


public class LobbyInfoCallReceived extends ListenerAdapter {
    LobbyInfoCallReceived(GuildMessageReceivedEvent event) {


        String str = event.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = event.getChannel();


            String time = str.split(" ")[1];
            String admin = str.split(" ")[2];

            if(SignInBot.lobbyExists(time, admin)) {
                Lobby lobby = SignInBot.getLobby(time, admin);
                textchannel.sendMessage("Twórca: " + lobby.getLobbyAdmin().getName() + " godzina: " + lobby.getTime() + "\n" +
                        "Lista graczy: " + lobby.lobbyusernames.size() + "/10" ).queue();
                int i = 1;
                String message = "";
                for(User user : lobby.lobbyusernames) {
                    message = message + i + ". " + user.getName() + "\n";
                    i += 1;
                }
                i = 1;
                String reserve = "";
                for(User user : lobby.reserveusernames) {
                    reserve = reserve + i + ". " + user.getName() + "\n";
                    i += 1;
                }
                textchannel.sendMessage(message + "Liczba graczy rezerwowych: " + lobby.reserveusernames.size() + "\n" + reserve).queue();
            }
            else {
                textchannel.sendMessage("Takie lobby nie istnieje :poop:").queue();
            }

    }
}
