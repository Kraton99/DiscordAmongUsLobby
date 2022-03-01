package bm.bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class CheckAppearanceTask extends TimerTask
{
    private Lobby lobby;
    private GuildMessageReceivedEvent event;
    private List< Member > presentMembers;
    private LinkedList< User > membersToUsers = new LinkedList<>();

    CheckAppearanceTask( Lobby lobby, GuildMessageReceivedEvent event )
    {
        this.lobby = lobby;
        this.event = event;
    }

    @Override
    public void run()
    {
        VoiceChannel vchannel = null;
       // lobby.setHasStarted( true );
        List< VoiceChannel > vChannels = event.getJDA().getVoiceChannelsByName( lobby.getLobbyAdmin().getName() + " " + lobby.getTime(), true );
        String message = "<bm.bot.Lobby " + lobby.getLobbyAdmin().getName() + " " + lobby.getTime() + ">" + "Mineło 5 minut od startu lobby. Osoby, które nie pojawiły się na czas zostały wyrzucone. Jeżeli chcesz ponownie dołączyć do lobby wpisz \"join <godzina> <tworca>\" \n";
        for( VoiceChannel channel : vChannels )
        {
            setPresentMembers( channel.getMembers() );
            vchannel = channel;
        }
        if( membersToUsers.isEmpty() && vchannel != null )
        {
            event.getChannel().sendMessage( "bm.bot.Lobby " + lobby.getLobbyAdmin().getName() + " " + lobby.getTime() + " zostaje zamknięte" ).queue();
            vchannel.delete().queue();
            SignInBot.getLobbies().remove( lobby );
        }
        LinkedList< User > toDelete = new LinkedList<>();
        for( User lobbyuser : lobby.getLobbyUserNames() )
        {

            if( !membersToUsers.contains( lobbyuser ) )
            {

                toDelete.add( lobbyuser );
                message = message + lobbyuser.getAsMention() + " Został/a wyrzucony z lobby" + "\n";

                if( lobby.getReservesUserNames().size() > 0 && lobby.getLobbyUserNames().size() < 10 )
                {

                    lobby.addUser( lobby.getReservesUserNames().get( 0 ) );
                    message = message + lobby.getReservesUserNames().get( 0 ).getAsMention() + "Zostałeś/aś przeniesiony z listy rezerwowych na listę graczy. Gratulujemy!! 😎 \n";
                    lobby.deleteUserReserve( lobby.getReservesUserNames().get( 0 ) );
                }
            }
        }
        for( User uDelete : toDelete )
        {
            lobby.deleteUser( uDelete );
        }

        TextChannel channel = event.getChannel();
        channel.sendMessage( message ).queue();
        SignInBot.getLobbyInitiators().remove( lobby );
    }

    public void setPresentMembers( List< Member > presentMembers )
    {
        this.presentMembers = presentMembers;
        for( Member member : presentMembers )
        {
            membersToUsers.add( member.getUser() );
        }
    }
}
