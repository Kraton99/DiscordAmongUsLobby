import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LeaveLobbyCallReceived {
     LeaveLobbyCallReceived(GuildMessageReceivedEvent event) {

        String str = event.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = event.getChannel();
        User user = event.getAuthor();

            String time = str.split(" ")[1];
            String admin = str.split(" ")[2];
            if(SignInBot.lobbyExists(time, admin)){
                Lobby lobby = SignInBot.getLobby(time, admin);

                if(lobby.lobbyusernames.contains(user)) {

                        if (lobby.getTime().equals(time) && lobby.lobbyusernames.size() == 10) {
                            lobby.deleteUser(user);
                            textchannel.sendMessage(user.getAsMention() + " wypisał/a się z lobby na godzine " + time + " Liczba zapisanych graczy " + lobby.lobbyusernames.size() + "/10 😒").queue();

                            if(lobby.reserveusernames.size() == 0) {
                                return;
                            }

                            user = lobby.reserveusernames.get(0);
                            lobby.deleteUserReserve(user);
                            lobby.addUser(user);
                            textchannel.sendMessage(user.getAsMention() + " Zostałeś/aś przeniesiony z listy rezerwowych na listę graczy. Gratulujemy!! 😎").queue();
                        } else if (lobby.getTime().equals(time)) {

                            lobby.deleteUser(user);
                            textchannel.sendMessage(user.getAsMention() + " wypisał/a się z lobby na godzine " + time + " Liczba zapisanych graczy " + lobby.lobbyusernames.size() + "/10 😒").queue();
                        }
                    }
                    else if(lobby.reserveusernames.contains(user)) {

                        lobby.deleteUserReserve(user);
                        textchannel.sendMessage(user.getAsMention() + " wypisał/a się z rezerwy na godzine " + time + " 😒").queue();
                    }
                    else {
                        textchannel.sendMessage(user.getAsMention() + " Nie byłeś zapisany na tą godzine :poop:").queue();
                    }
            }
            else {
                textchannel.sendMessage(user.getAsMention() + " Takie lobby nie istnieje :poop:").queue();
            }


   }

}
