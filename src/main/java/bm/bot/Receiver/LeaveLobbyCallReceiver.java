package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.SignInBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LeaveLobbyCallReceiver implements CallReceiver
{

    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent )
    {
        String str = aEvent.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = aEvent.getChannel();
        User user = aEvent.getAuthor();

        String time = str.split( " " )[ 1 ];
        String admin = str.split( " " )[ 2 ];
        if( SignInBot.lobbyExists( time, admin ) )
        {
            Lobby lobby = SignInBot.getLobby( time, admin );

            if( lobby.getLobbyUserNames().contains( user ) )
            {

                if( lobby.getTime().equals( time ) && lobby.getLobbyUserNames().size() == 10 )
                {
                    lobby.deleteUser( user );
                    textchannel.sendMessage( user.getAsMention() + " wypisał/a się z lobby na godzine " + time + " Liczba zapisanych graczy " + lobby.getLobbyUserNames().size() + "/10 😒" ).queue();

                    if( lobby.getReservesUserNames().size() == 0 )
                    {
                        return;
                    }

                    user = lobby.getReservesUserNames().get( 0 );
                    lobby.deleteUserReserve( user );
                    lobby.addUser( user );
                    textchannel.sendMessage( user.getAsMention() + " Zostałeś/aś przeniesiony z listy rezerwowych na listę graczy. Gratulujemy!! 😎" ).queue();
                }
                else if( lobby.getTime().equals( time ) )
                {

                    lobby.deleteUser( user );
                    textchannel.sendMessage( user.getAsMention() + " wypisał/a się z lobby na godzine " + time + " Liczba zapisanych graczy " + lobby.getLobbyUserNames().size() + "/10 😒" ).queue();
                }
            }
            else if( lobby.getReservesUserNames().contains( user ) )
            {

                lobby.deleteUserReserve( user );
                textchannel.sendMessage( user.getAsMention() + " wypisał/a się z rezerwy na godzine " + time + " 😒" ).queue();
            }
            else
            {
                textchannel.sendMessage( user.getAsMention() + " Nie byłeś zapisany na tą godzine :poop:" ).queue();
            }
        }
        else
        {
            textchannel.sendMessage( user.getAsMention() + " Takie lobby nie istnieje :poop:" ).queue();
        }

    }
}
