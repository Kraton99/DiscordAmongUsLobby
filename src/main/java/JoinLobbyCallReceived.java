import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinLobbyCallReceived {

    JoinLobbyCallReceived(GuildMessageReceivedEvent event) {

        String str = event.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = event.getChannel();
        User user = event.getAuthor();


            String time = str.split(" ")[1];
            String admin = str.split(" ")[2];

            if(SignInBot.lobbyExists(time, admin)) {
                Lobby lobby = SignInBot.getLobby(time, admin);
                    if(lobby.lobbyusernames.contains(user) || lobby.reserveusernames.contains(user)){
                        textchannel.sendMessage(user.getAsMention() + " Już jesteś zapisany do tego lobby").queue();
                        return;
                    }
                    if(lobby.getTime().equals(time) && lobby.lobbyusernames.size() < 10){
                        lobby.addUser(user);
                        textchannel.sendMessage(user.getAsMention() + " zapisał/a się do lobby na godzine " + time + " do " + lobby.getLobbyAdmin().getName() + ". Liczba zapisanych graczy " + lobby.lobbyusernames.size()+  "/10 :point_right: :ok_hand:").queue();
                    }
                    else if(lobby.getTime().equals(time)) {
                        lobby.addUserReserve(user);
                        textchannel.sendMessage(user.getAsMention() + " Lobby o tej godzinie do " + lobby.getLobbyAdmin().getName() + " jest już pełne. Zostałeś/aś dodany do kolejki rezerwowej. Twoje miejsce w rezerwie to: " + lobby.reserveusernames.size() + " :point_right: :ok_hand:").queue();
                    }
            }
            else {
                textchannel.sendMessage(user.getAsMention() + " Takie lobby nie istnieje :poop:").queue();
            }

    }
}


