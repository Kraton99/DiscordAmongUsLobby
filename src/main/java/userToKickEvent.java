import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

import java.util.List;
import java.util.TimerTask;

public class userToKickEvent extends TimerTask {

    GuildVoiceUpdateEvent event;
    TextChannel channel;
    Lobby lobby;

    userToKickEvent(Lobby lobby, GuildVoiceUpdateEvent event, TextChannel channel) {
        this.event = event;
        this.channel = channel;
        this.lobby = lobby;
    }

    @Override
    public void run() {
        if(!SignInBot.lobbies.contains(lobby)){
            return;
        }
        if(lobby.lobbyusernames.contains(event.getEntity().getUser())) {
            lobby.deleteUser(event.getEntity().getUser());
            channel.sendMessage(event.getEntity().getUser().getAsMention() + " ze względu na brak aktywności został/a wyrzucony/a z lobby").queue();
        }
        if(lobby.lobbyusernames.size() < 10 && !lobby.reserveusernames.isEmpty()){
            lobby.addUser(lobby.reserveusernames.get(0));
            channel.sendMessage(lobby.reserveusernames.get(0).getAsMention() + " Zostałeś/aś przeniesiony z listy rezerwowych na listę graczy. Gratulujemy!! 😎").queue();
            lobby.deleteUserReserve(lobby.reserveusernames.get(0));
        }
    }
}
