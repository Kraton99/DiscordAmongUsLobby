import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class HelpCallReceived  {

     HelpCallReceived(GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
            channel.sendMessage("Bot - zapisy do among us \nAutor: Kraton \nDostępne komendy: \nactivelobbies \n" +
                    "lobbyinfo <godzina> <twórca> \namonglobby <godzina> \njoin <godzina> <twórca> \nleave <godzina> <twórca> \nkick <userToKick> from <godzina> <tworca>").queue();
     }

}
