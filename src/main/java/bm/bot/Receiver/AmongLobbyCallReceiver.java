package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.LobbyInitiator;
import bm.bot.SignInBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;


public class AmongLobbyCallReceiver implements CallReceiver
{

    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent )
    {
        String str = aEvent.getMessage().getContentDisplay().toLowerCase();
        TextChannel textChannel = aEvent.getChannel();
        User user = aEvent.getAuthor();

        String time = str.split( " " )[ 1 ];
        //TODO: Fix time parsing coz it is ugly af
        if( !checkTime( time ) )
        {
            textChannel.sendMessage( "Niestety nie cofniemy się do przeszłości :cry: Jeżeli chcesz załozyć lobby na jutrzejszy dzień to musisz zrobić" + " to jutro (po 00:00) ze względu na ograniczone zasoby bota :cry:" ).queue();
            return;
        }

        if( !SignInBot.lobbyExists( time, user.getName() ) )
        {

            Lobby lobby = new Lobby( time, user );
            SignInBot.getLobbies().add( lobby );
            lobby.addUser( user );
            textChannel.sendMessage( "@here!! " + user.getAsMention() + " otworzył/a lobby do amonga na godzine " + time + ". " + "Jeżeli jesteś pewny że będziesz i nie odjebiesz lipy wpisz \"join " + time + " " + user.getName() + "\" żeby zapisać się do gry :point_right: :ok_hand:" ).queue();
            try
            {
                createLobbyStartTask( aEvent, lobby );
            }
            catch( ParseException e )
            {
                textChannel.sendMessage( "SOMETHING WENT WRONG" ).queue();
            }
        }
        else
        {
            textChannel.sendMessage( user.getAsMention() + " już stworzyłeś lobby na tą godzine :poop:" ).queue();
        }
    }

    private void createLobbyStartTask( GuildMessageReceivedEvent aEvent, Lobby lobby ) throws ParseException
    {

        Date date = getDate(lobby);

        Timer timer = new Timer();
        LobbyInitiator initiator = new LobbyInitiator( lobby, aEvent );
        timer.schedule( initiator, date );
        SignInBot.getLobbyInitiators().put( lobby, timer );
    }

    private Date getDate(Lobby aLobby) throws ParseException
    {
        DateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        DateFormat currentDateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
        Date currentDate = new Date();

        return dateFormatter.parse( currentDateFormatter.format( currentDate ) + " " + aLobby.getTime() );
    }

    // we only accept lobbies within same day due to
    // client requirements there is no need to use it
    // further
    private boolean checkTime( String time )
    {
        int hours = Integer.parseInt( time.split( ":" )[ 0 ] );
        int minutes = Integer.parseInt( time.split( ":" )[ 1 ] );
        Calendar calendar = new GregorianCalendar();
        if( calendar.get( Calendar.HOUR_OF_DAY ) > hours )
        {
            return false;
        }
        return calendar.get( Calendar.MINUTE ) <= minutes || calendar.get( Calendar.HOUR_OF_DAY ) != hours;
    }
}