package bm.bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LobbyInitiator extends TimerTask
{
    private Lobby lobby;
    private GuildMessageReceivedEvent event;

    public LobbyInitiator( Lobby lobby, GuildMessageReceivedEvent event )
    {
        this.lobby = lobby;
        this.event = event;
    }

    @Override
    public void run()
    {
        TextChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        channel.sendMessage( "Wybiła godzina! :scream: Zaplanowane lobby przez " + lobby.getLobbyAdmin().getAsMention() + " na godzine " + lobby.getTime() + " właśnie startuje!" ).queue();
        String message = getListOfUsers();
        Role role = guild.getRoleById( "725404054930587689" );
        channel.sendMessage( message + "Liczba rezerwowych: " + lobby.getReservesUserNames().size() ).queue();

        guild.createVoiceChannel( lobby.getLobbyAdmin().getName() + " " + lobby.getTime() ).
                addPermissionOverride( role, EnumSet.of( Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK ), null ).queue();


        scheduleTask();

    }

    private void scheduleTask()
    {
        DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date taskDate = new Date( t + ( 5 * 60000 ) );
        try
        {
            taskDate = formatter.parse( formatter.format( taskDate ) );
        }
        catch( ParseException e )
        {
            e.printStackTrace();
        }
        Timer timer = new Timer();
        CheckAppearanceTask appearance = new CheckAppearanceTask( lobby, event );
        timer.schedule( appearance, taskDate );
    }

    private String getListOfUsers()
    {
        int i = 1;
        String message = "";
        for( User users : lobby.getLobbyUserNames() )
        {
            message = message + i + ". " + users.getAsMention() + "\n";
            i += 1;
        }
        return message;
    }
}
