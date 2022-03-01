package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.SignInBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class JoinLobbyCallReceiver implements CallReceiver
{

    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent )
    {
        String str = aEvent.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = aEvent.getChannel();
        User user = aEvent.getAuthor();


        String time = str.split( " " )[ 1 ];
        String admin = str.split( " " )[ 2 ];


        SignInBot.getLobby( time, admin ).ifPresentOrElse( lobby ->
        {
            //TODO: Extract whole adding/changing/removing logic into some LobbyHandler
            if( lobby.getLobbyUserNames().contains( user ) || lobby.getReservesUserNames().contains( user ) )
            {
                textchannel.sendMessage( user.getAsMention() + " Już jesteś zapisany do tego lobby" ).queue();
            }
            else if( lobby.getTime().equals( time ) && lobby.getLobbyUserNames().size() < 10 )
            {
                lobby.addUser( user );
                textchannel.sendMessage( user.getAsMention() + " zapisał/a się do lobby na godzine " + time + " do " + lobby.getLobbyAdmin().getName() + ". Liczba zapisanych graczy " + lobby.getLobbyUserNames().size() + "/10 :point_right: :ok_hand:" ).queue();
            }
            else if( lobby.getTime().equals( time ) )
            {
                lobby.addUserReserve( user );
                textchannel.sendMessage( user.getAsMention() + " bm.bot.Lobby o tej godzinie do " + lobby.getLobbyAdmin().getName() + " jest już pełne. Zostałeś/aś dodany do kolejki rezerwowej. Twoje miejsce w rezerwie to: " + lobby.getReservesUserNames().size() + " :point_right: :ok_hand:" ).queue();
            }
        }, () -> textchannel.sendMessage( user.getAsMention() + " Takie lobby nie " + "istnieje :poop:" ).queue() );
    }
}


