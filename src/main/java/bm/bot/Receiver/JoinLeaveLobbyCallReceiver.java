package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.SignInBot;
import bm.bot.userToKickEvent;
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

public class JoinLeaveLobbyCallReceiver extends ListenerAdapter
{

    public void onGuildVoiceUpdate( GuildVoiceUpdateEvent aEvent )
    {

        TextChannel channel = aEvent.getJDA().getTextChannelById( "769525781264203816" );
        User userLeft = aEvent.getEntity().getUser();
        String message = userLeft.getAsMention() + " wyszedł/a z kanału. Jeżeli nie wróci w ciągu 2 minut zostanie wyrzucony z lobby";
        Lobby eventLobby = null;

        for( Lobby lobby : SignInBot.getLobbies() )
        {
            String lobbyName = lobby.getLobbyAdmin().getName() + " " + lobby.getTime();
            if( aEvent.getChannelLeft() != null )
            {
                if( aEvent.getChannelLeft().getName().equals( lobbyName ) && lobby.getLobbyUserNames().contains( userLeft ) && lobby.isHasStarted() )
                {
                    eventLobby = lobby;
                    channel.sendMessage( message ).queue();
                    DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
                    Calendar date = Calendar.getInstance();
                    long t = date.getTimeInMillis();
                    Date taskDate = new Date( t + ( 2 * 60000 ) );
                    try
                    {
                        taskDate = formatter.parse( formatter.format( taskDate ) );
                    }
                    catch( ParseException e )
                    {
                        e.printStackTrace();
                    }
                    Timer timer = new Timer();
                    timer.schedule( new userToKickEvent( eventLobby, aEvent, channel ), taskDate );
                    SignInBot.getUserToKick().put( userLeft, timer );
                }
            }
            if( aEvent.getChannelJoined() != null )
            {
                if( aEvent.getChannelJoined().getName().equals( lobby.getLobbyAdmin().getName() + " " + lobby.getTime() ) && lobby.getLobbyUserNames().contains( userLeft ) && lobby.isHasStarted() && SignInBot.getUserToKick().containsKey( userLeft ) )
                {
                    SignInBot.getUserToKick().get( userLeft ).cancel();
                    SignInBot.getUserToKick().remove( userLeft );
                }
            }
            if( aEvent.getChannelLeft() != null )
            {
                if( aEvent.getChannelLeft().getName().equals( lobby.getLobbyAdmin().getName() + " " + lobby.getTime() ) && aEvent.getChannelLeft().getMembers().isEmpty() && lobby.isHasStarted() )
                {
                    aEvent.getChannelLeft().delete().queue();
                    SignInBot.getLobbies().remove( eventLobby );
                    channel.sendMessage( "bm.bot.Lobby " + lobby.getLobbyAdmin().getName() + " " + lobby.getTime() + " zostaje zamknięte" ).queue();
                }
            }
        }
    }

}
