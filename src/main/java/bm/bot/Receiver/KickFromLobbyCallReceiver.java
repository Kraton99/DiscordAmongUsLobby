package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.SignInBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Optional;

public class KickFromLobbyCallReceiver implements CallReceiver
{


    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent )
    {
        String str = aEvent.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = aEvent.getChannel();
        User user = aEvent.getAuthor();


        String time = str.split( " " )[ 3 ];
        String admin = str.split( " " )[ 4 ];
        String userToKick = str.split( " " )[ 1 ];

        SignInBot.getLobby( time, admin ).ifPresentOrElse( lobby ->
        {
            if( lobby.getLobbyAdmin().equals( user ) )
            {
                lobby.searchUserByNameLobby( userToKick ).ifPresentOrElse( toKick ->
                {
                    lobby.deleteUser( toKick );
                    textchannel.sendMessage( "Gracz " + toKick.getAsMention() + " został wyrzucony z lobby" ).queue();
                }, () -> lobby.searchUserByNameReserve( userToKick ).ifPresentOrElse( kick ->
                {
                    lobby.deleteUserReserve( kick );
                    textchannel.sendMessage( "Gracz " + kick.getAsMention() + " został " + "wyrzucony z listy rezerwowych" ).queue();
                }, () -> textchannel.sendMessage( "Gracz " + userToKick.substring( 0, 1 ).toUpperCase() + userToKick.substring( 1 ) + " nie " + "jest w lobby" ).queue() ) );

            }
            else
            {
                textchannel.sendMessage( "Nie jesteś twórcą tego lobby. Skontaktuj się z twórcą aby rozważyć wyrzucenie gracza z lobby. Jeżeli jest" + " to plimciake to można wyjebać odrazu ES :joy:" ).queue();
            }
        }, () -> textchannel.sendMessage( "NO LOBBY" ).queue() );
    }
}
