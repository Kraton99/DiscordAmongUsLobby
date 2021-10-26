package bm.bot;

import bm.bot.Receiver.*;
import bm.bot.Utils.Commands;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static bm.bot.Utils.Commands.HELP;


public class MessageGuard extends ListenerAdapter
{

    public void onGuildMessageReceived( GuildMessageReceivedEvent aEvent )
    {
        if( aEvent.getAuthor().isBot() )
        {
            return;
        }
        if( !aEvent.getChannel().getName().equals( "zapisy-among-us" ) )
        {
            return;
        }

        String message = aEvent.getMessage().getContentDisplay();
        message = message.split( " " )[ 0 ].toLowerCase();
        Commands value;
        try
        {
            value = Commands.valueOf( message );
        }
        // In case of wrong command we want to catch
        // valueOf exception and inform user
        catch( IllegalArgumentException ignored )
        {
            aEvent.getMessage().delete().queue();
            aEvent.getChannel().sendMessage( "Taka komenda nie istnieje. Jeżeli nie znasz składni komend wpisz \"help\" " ).queue();
            return;
        }
        switch( value )
        {
            case HELP:
                new HelpCallReceiver().receiveCall( aEvent );
                break;
            case ACTIVE_LOBBIES:
                new ActiveLobbiesCallReceiver().receiveCall( aEvent );
                break;
            case AMONG_LOBBY:
                new AmongLobbyCallReceiver().receiveCall( aEvent );
                break;
            case JOIN:
                new JoinLobbyCallReceiver().receiveCall( aEvent );
                break;
            case LEAVE:
                new LeaveLobbyCallReceiver().receiveCall( aEvent );
                break;
            case DELETE_AMONG_LOBBY:
                new DeleteAmongLobbyCallReceiver().receiveCall( aEvent );
                break;
            case LOBBY_INFO:
                new LobbyInfoCallReceiver().receiveCall( aEvent );
                break;
            case KICK:
                new KickFromLobbyCallReceiver().receiveCall( aEvent );
                break;
        }
    }
}

