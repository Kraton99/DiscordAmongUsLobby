package bm.bot;

import bm.bot.Receiver.JoinLeaveLobbyCallReceiver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Timer;


public class SignInBot
{

    private static final LinkedList< Lobby > lobbies = new LinkedList<>();
    private static final HashMap< Lobby, Timer > lobbyInitiators = new HashMap();
    private static final HashMap< User, Timer > userTokick = new HashMap<>();

    SignInBot() throws LoginException
    {
        JDA jda = JDABuilder.createDefault( "#TOKEN HERE#" ).build();
        jda.addEventListener( new JoinLeaveLobbyCallReceiver() );
        jda.addEventListener( new MessageGuard() );
    }

    public static HashMap< Lobby, Timer > getLobbyInitiators()
    {
        return lobbyInitiators;
    }

    public static HashMap< User, Timer > getUserToKick()
    {
        return userTokick;
    }

    public static LinkedList< Lobby > getLobbies()
    {
        return lobbies;
    }

    //we cant search by object because in some cases e.g
    // Join action can't get Admin of lobby as a user in
    // easy way so it is better to keep it as String
    public static boolean lobbyExists( String aTime, String aAdmin )
    {
        for( Lobby lobby : lobbies )
        {
            if( lobby.getTime().equals( aTime ) && lobby.getLobbyAdmin().getName().equals( aAdmin ) )
            {
                return true;
            }
        }
        return false;
    }

    public static Optional<Lobby> getLobby( String aTime, String aAdmin )
    {
        for( Lobby lobby : lobbies )
        {
            if( lobby.getTime().equals( aTime ) && lobby.getLobbyAdmin().getName().equals( aAdmin ) )
            {
                return Optional.of( lobby );
            }
        }
        return Optional.empty();
    }
}

