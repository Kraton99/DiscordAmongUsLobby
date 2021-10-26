package bm.bot.Receiver;

import bm.bot.Lobby;
import bm.bot.SignInBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DeleteAmongLobbyCallReceiver implements CallReceiver
{

    @Override
    public void receiveCall( GuildMessageReceivedEvent aEvent )
    {
        String str = aEvent.getMessage().getContentDisplay().toLowerCase();
        User user = aEvent.getAuthor();
        String time = str.split( " " )[ 1 ];
        TextChannel textchannel = aEvent.getChannel();

        SignInBot.getLobby( time, user.getName() ).ifPresentOrElse( lobby ->
        {
            if( lobby.getLobbyAdmin().equals( user ) )
            {
                deleteLobby( lobby, aEvent, user );
                textchannel.sendMessage( "@here!! " + user.getAsMention() + " usunął swoje lobby do amonga na godzine " + time + ". " + "Oplujcie go na pw bo popsuł nam zabawe i zróbmy własne lobby :muscle:" ).queue();
            }
            else
            {
                textchannel.sendMessage( user.getAsMention() + " Nie jesteś założycielem lobby żeby je usunąć :middle_finger:" ).queue();
            }
        }, () -> textchannel.sendMessage( user.getAsMention() + " bm.bot.Lobby o tej godzinie nie istnieje :poop:" ).queue() );
    }

    private void deleteLobby( Lobby aLobby, GuildMessageReceivedEvent aEvent, User aUser )
    {
        //there might be voice channel already initialized so we have to remove it
        String lobbyName = aLobby.getLobbyAdmin().getName() + " " + aLobby.getTime();
        for( VoiceChannel channel : aEvent.getGuild().getVoiceChannelsByName( lobbyName, true ) )
        {
            if( channel != null )
            {
                channel.delete().queue();
            }
        }
        String time = aLobby.getTime();
        SignInBot.getLobbyInitiators().get( aLobby ).cancel();
        SignInBot.getLobbyInitiators().remove( SignInBot.getLobby( time, aUser.getName().toLowerCase() ) );
        SignInBot.getLobbies().remove( SignInBot.getLobby( time, aUser.getName().toLowerCase() ) );
    }
}
