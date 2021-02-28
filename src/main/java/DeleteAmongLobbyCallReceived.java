import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DeleteAmongLobbyCallReceived {
    DeleteAmongLobbyCallReceived(GuildMessageReceivedEvent event) {

        
        String str = event.getMessage().getContentDisplay().toLowerCase();
        TextChannel textchannel = event.getChannel();
        User user = event.getAuthor();

            String time = str.split(" ")[1];
            if(SignInBot.lobbyExists(time, user.getName().toLowerCase())) {
                Lobby lobby = SignInBot.getLobby(time, user.getName().toLowerCase());
                if(lobby.getLobbyAdmin() == user) {
                    for(VoiceChannel channel : event.getGuild().getVoiceChannelsByName(lobby.getLobbyAdmin().getName() + " " + lobby.getTime(), true)){
                        if(channel != null){
                            channel.delete().queue();
                        }
                    }
                    SignInBot.lobbyInitiators.get(SignInBot.getLobby(time, user.getName().toLowerCase())).cancel();
                    SignInBot.lobbyInitiators.remove(SignInBot.getLobby(time, user.getName().toLowerCase()));
                    SignInBot.lobbies.remove(SignInBot.getLobby(time, user.getName().toLowerCase()));
                    textchannel.sendMessage("@here!! " + user.getAsMention() + " usunął swoje lobby do amonga na godzine " + time + ". " +
                            "Oplujcie go na pw bo popsuł nam zabawe i zróbmy własne lobby :muscle:").queue();
                }
                else {
                    textchannel.sendMessage(user.getAsMention() + " Nie jesteś założycielem lobby żeby je usunąć :middle_finger:").queue();
                }
            }
            else {
                textchannel.sendMessage(user.getAsMention() + " Lobby o tej godzinie nie istnieje :poop:").queue();
            }

    }
}
