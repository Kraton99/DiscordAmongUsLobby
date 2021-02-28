import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class JoinLeaveLobbyCallReceived extends ListenerAdapter {

    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {

        TextChannel channel = event.getJDA().getTextChannelById("769525781264203816");
        User userleft = event.getEntity().getUser();
        String message = userleft.getAsMention() + " wyszedł/a z kanału. Jeżeli nie wróci w ciągu 2 minut zostanie wyrzucony z lobby";
        Lobby eventLobby = null;
        for(Lobby lobby : SignInBot.lobbies) {
            if (event.getChannelLeft() != null) {
                if (event.getChannelLeft().getName().equals(lobby.getLobbyAdmin().getName() + " " + lobby.getTime()) && lobby.lobbyusernames.contains(userleft) && lobby.hasStarted) {
                    eventLobby = lobby;
                    channel.sendMessage(message).queue();
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Calendar date = Calendar.getInstance();
                    long t = date.getTimeInMillis();
                    Date taskDate = new Date(t + (2 * 60000));
                    try {
                        taskDate = formatter.parse(formatter.format(taskDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Timer timer = new Timer();
                    timer.schedule(new userToKickEvent(eventLobby, event, channel), taskDate);
                    SignInBot.userTokick.put(userleft, timer);
                }
            }
            if (event.getChannelJoined() != null) {
                if (event.getChannelJoined().getName().equals(lobby.getLobbyAdmin().getName() + " " + lobby.getTime()) && lobby.lobbyusernames.contains(userleft) && lobby.hasStarted && SignInBot.userTokick.containsKey(userleft)) {
                    SignInBot.userTokick.get(userleft).cancel();
                    SignInBot.userTokick.remove(userleft);
                }
            }
            if(event.getChannelLeft() != null) {
                if (event.getChannelLeft().getName().equals(lobby.getLobbyAdmin().getName() + " " + lobby.getTime()) && event.getChannelLeft().getMembers().isEmpty() && lobby.hasStarted) {
                    event.getChannelLeft().delete().queue();
                    SignInBot.lobbies.remove(eventLobby);
                    channel.sendMessage("Lobby " + lobby.getLobbyAdmin().getName() + " " + lobby.getTime() + " zostaje zamknięte").queue();
                }
            }
        }
    }

}
