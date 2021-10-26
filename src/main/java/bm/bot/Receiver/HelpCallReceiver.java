package bm.bot.Receiver;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


public class HelpCallReceiver implements CallReceiver {

    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent ) {
        TextChannel channel = aEvent.getChannel();
        channel.sendMessage( "Bot - zapisy do among us \nAutor: Kraton \nDostępne komendy: \nactivelobbies \n" +
                "lobbyinfo <godzina> <twórca> " +
                "\namonglobby <godzina> \njoin <godzina> <twórca> \nleave <godzina> <twórca> \nkick <userToKick> from <godzina> <tworca>" ).queue();
    }
}
