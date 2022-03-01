package bm.bot;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

import java.util.TimerTask;

public class userToKickEvent extends TimerTask
{

    private GuildVoiceUpdateEvent event;
    private TextChannel channel;
    private Lobby lobby;

    public userToKickEvent( Lobby lobby, GuildVoiceUpdateEvent event, TextChannel channel )
    {
        this.event = event;
        this.channel = channel;
        this.lobby = lobby;
    }

    @Override
    public void run()
    {
        // lobby might be deleted already
        if( !SignInBot.getLobbies().contains( lobby ) )
        {
            return;
        }
        if( lobby.getLobbyUserNames().contains( event.getEntity().getUser() ) )
        {
            lobby.deleteUser( event.getEntity().getUser() );
            channel.sendMessage( event.getEntity().getUser().getAsMention() + " ze względu na brak aktywności został/a wyrzucony/a z lobby" ).queue();
        }
        if( lobby.getLobbyUserNames().size() < 10 && !lobby.getReservesUserNames().isEmpty() )
        {
            lobby.addUser( lobby.getReservesUserNames().get( 0 ) );
            channel.sendMessage( lobby.getReservesUserNames().get( 0 ).getAsMention() + " Zostałeś/aś przeniesiony z listy rezerwowych na listę graczy. Gratulujemy!! 😎" ).queue();
            lobby.deleteUserReserve( lobby.getReservesUserNames().get( 0 ) );
        }
    }
}
