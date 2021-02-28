import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class AmongLobbyCallReceived {


    AmongLobbyCallReceived(GuildMessageReceivedEvent event) {

        Lobby lobby = null;
        String str = event.getMessage().getContentDisplay().toLowerCase();
        TextChannel textChannel = event.getChannel();
        User user = event.getAuthor();
            String time = str.split(" ")[1];
            if(!checkTime(time)) {
                textChannel.sendMessage("Niestety nie cofniemy się do przeszłości :cry: Jeżeli chcesz załozyć lobby na jutrzejszy dzień to musisz zrobić" +
                        " to jutro (po 00:00) ze względu na ograniczone zasoby bota :cry:").queue();
                return;
            }
            if (!SignInBot.lobbyExists(time, user.getName())) {
                lobby = new Lobby(time, user);
                SignInBot.lobbies.add(lobby);
                lobby.addUser(user);
                textChannel.sendMessage("@here!! " + user.getAsMention() + " otworzył/a lobby do amonga na godzine " + time + ". " +
                        "Jeżeli jesteś pewny że będziesz i nie odjebiesz lipy wpisz \"join " + time + " " + user.getName() + "\" żeby zapisać się do gry :point_right: :ok_hand:").queue();
            } else {
                textChannel.sendMessage(user.getAsMention() + " już stworzyłeś lobby na tą godzine :poop:").queue();
            }

            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            DateFormat currentdateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            Date currentdate = new Date();
            try {
                date = dateFormatter.parse(currentdateFormatter.format(currentdate) + " " + lobby.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Timer timer = new Timer();
            LobbyInitiator initiator = new LobbyInitiator(lobby, event);
            timer.schedule(initiator, date);
            SignInBot.lobbyInitiators.put(lobby, timer);
        }

    public boolean checkTime(String time) {
        int hours = Integer.valueOf(time.split(":")[0]);
        int minutes = Integer.valueOf(time.split(":")[1]);
        Date currentdate = new Date();
        if(currentdate.getHours() > hours) {
            return false;
        }
            if(currentdate.getMinutes() > minutes && currentdate.getHours() == hours){
                return false;
            }
        return true;
    }
}