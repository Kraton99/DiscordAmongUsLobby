
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class CheckAppearance extends TimerTask {
    Lobby lobby;
    GuildMessageReceivedEvent event;
    List<Member> presentMembers;
    LinkedList<User> membersToUsers = new LinkedList<>();

    CheckAppearance(Lobby lobby,  GuildMessageReceivedEvent event){
        this.lobby = lobby;
        this.event = event;
    }
    @Override
    public void run() {
        VoiceChannel vchannel = null;
        lobby.sethasStarted(true);
        List<VoiceChannel> vChannels = event.getJDA().getVoiceChannelsByName(lobby.getLobbyAdmin().getName() + " " + lobby.getTime(), true);
        String message = "<Lobby " + lobby.getLobbyAdmin().getName() + " " + lobby.getTime() + ">" + "Mineło 5 minut od startu lobby. Osoby, które nie pojawiły się na czas zostały wyrzucone. Jeżeli chcesz ponownie dołączyć do lobby wpisz \"join <godzina> <tworca>\" \n";
        for(VoiceChannel channel : vChannels){
            setPresentMembers(channel.getMembers());
            vchannel = channel;
        }
        if(membersToUsers.isEmpty() && vchannel != null){
            event.getChannel().sendMessage("Lobby " + lobby.getLobbyAdmin().getName() + " " + lobby.getTime() + " zostaje zamknięte").queue();
            vchannel.delete().queue();
            SignInBot.lobbies.remove(lobby);
        }
        LinkedList<User> toDelete = new LinkedList<>();
        for(User lobbyuser : lobby.lobbyusernames){

            if(!membersToUsers.contains(lobbyuser)){

                toDelete.add(lobbyuser);
                message = message + lobbyuser.getAsMention() + " Został/a wyrzucony z lobby" + "\n";

                if(lobby.reserveusernames.size() > 0 && lobby.lobbyusernames.size() < 10) {

                    lobby.addUser(lobby.reserveusernames.get(0));
                    message = message + lobby.reserveusernames.get(0).getAsMention() + "Zostałeś/aś przeniesiony z listy rezerwowych na listę graczy. Gratulujemy!! 😎 \n";
                    lobby.deleteUserReserve(lobby.reserveusernames.get(0));
                }
            }
        }
        for(User uDelete : toDelete){
            lobby.lobbyusernames.remove(uDelete);
        }

        TextChannel channel = event.getChannel();
        channel.sendMessage(message).queue();
        SignInBot.lobbyInitiators.remove(lobby);
    }

    public void setPresentMembers(List<Member> presentMembers) {
        this.presentMembers = presentMembers;
        for(Member member : presentMembers){
            membersToUsers.add(member.getUser());
        }
    }
}
