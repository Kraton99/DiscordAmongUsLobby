package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.SignInBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ActiveLobbiesCallReceiver implements CallReceiver
{

    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent )
    {
        TextChannel textchannel = aEvent.getChannel();

        if( !SignInBot.getLobbies().isEmpty() )
        {
            StringBuilder message = new StringBuilder();
            for( Lobby lobby : SignInBot.getLobbies() )
            {
                //we want to send it in one message
                message.append( "Among us lobby godzina: " ).append( lobby.getTime() ).append( " Twórca lobby: " ).append( lobby.getLobbyAdmin().getName() ).append( "\n" );
            }
            textchannel.sendMessage( message + "Jeżeli chcesz więcej informacji na " + "temat lobby wpisz \"lobbyinfo <godzina> <twórca>\" np: lobbyinfo " + "13:00 Kraton" ).queue();
        }
        else
        {
            textchannel.sendMessage( "#NO LOBBY MESSAGE#" ).queue();
        }
    }
}
