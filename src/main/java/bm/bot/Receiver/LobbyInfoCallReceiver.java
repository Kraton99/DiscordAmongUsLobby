package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.SignInBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


public class LobbyInfoCallReceiver implements CallReceiver
{


    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent )
    {
        String str = aEvent.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = aEvent.getChannel();


        String time = str.split( " " )[ 1 ];
        String admin = str.split( " " )[ 2 ];


        SignInBot.getLobby( time, admin ).ifPresentOrElse( lobby ->
        {
            textchannel.sendMessage( "Twórca: " + lobby.getLobbyAdmin().getName() + " godzina: " + lobby.getTime() + "\n" + "Lista graczy: " + lobby.getLobbyUserNames().size() + "/10" ).queue();
            int i = 1;
            String message = "";
            for( User user : lobby.getLobbyUserNames() )
            {
                message = message + i + ". " + user.getName() + "\n";
                i += 1;
            }
            i = 1;
            String reserve = "";
            for( User user : lobby.getReservesUserNames() )
            {
                reserve = reserve + i + ". " + user.getName() + "\n";
                i += 1;
            }
            textchannel.sendMessage( message + "Liczba graczy rezerwowych: " + lobby.getReservesUserNames().size() + "\n" + reserve ).queue();
        }, () -> textchannel.sendMessage( "Takie lobby nie istnieje :poop:" ).queue() );

    }
}
