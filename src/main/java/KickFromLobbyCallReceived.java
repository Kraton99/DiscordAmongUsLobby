import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class KickFromLobbyCallReceived {

    KickFromLobbyCallReceived(GuildMessageReceivedEvent event) {


        String str = event.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = event.getChannel();
        User user = event.getAuthor();


            String time = str.split(" ")[3];
            String admin = str.split(" ")[4];
            String userToKick = str.split(" ")[1];
            if(SignInBot.lobbyExists(time, admin)){
                Lobby lobby = SignInBot.getLobby(time, admin);
                if(lobby.getLobbyAdmin() == user) {
                    User toKick = lobby.searchUserByNameLobby(userToKick);
                    if(toKick != null) {
                        lobby.deleteUser(toKick);
                        textchannel.sendMessage("Gracz " + toKick.getAsMention() + " został wyrzucony z lobby").queue();
                        return;
                    }
                    toKick = lobby.searchUserByNameReserve(userToKick);
                    if(toKick != null){
                        lobby.deleteUserReserve(toKick);
                        textchannel.sendMessage("Gracz " + toKick.getAsMention() + " został wyrzucony z listy rezerwowych").queue();
                    }
                    else {
                        textchannel.sendMessage("Gracz " + userToKick.substring(0,1).toUpperCase() + userToKick.substring(1) + " nie jest w lobby").queue();
                        return;
                    }
                }
                else {
                    textchannel.sendMessage("Nie jesteś twórcą tego lobby. Skontaktuj się z twórcą aby rozważyć wyrzucenie gracza z lobby. Jeżeli jest" +
                            " to plimciake to można wyjebać odrazu ES :joy:").queue();
                }
            }


    }
}
