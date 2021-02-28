import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LobbyInitiator extends TimerTask {
    Lobby lobby;
    GuildMessageReceivedEvent event;

    LobbyInitiator(Lobby lobby, GuildMessageReceivedEvent event) {
        this.lobby = lobby;
        this.event = event;
    }

    @Override
    public void run() {
        TextChannel channel = event.getChannel();
        channel.sendMessage("Wybiła godzina! :scream: Zaplanowane lobby przez " + lobby.getLobbyAdmin().getAsMention() + " na godzine " + lobby.getTime() +
                " właśnie startuje!").queue();
        int i = 1;
        String message = "";
        for (User users : lobby.lobbyusernames) {
            message = message + i + ". " + users.getAsMention() + "\n";
            i += 1;
        }
        Role role = event.getGuild().getRoleById("725404054930587689");
        channel.sendMessage(message + "Liczba rezerwowych: " + lobby.reserveusernames.size()).queue();
        Guild guild = event.getGuild();

        guild.createVoiceChannel(lobby.getLobbyAdmin().getName() + " " + lobby.getTime()).
                addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK), null).queue();


        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date taskDate = new Date(t + (5 * 60000));
        try {
            taskDate = formatter.parse(formatter.format(taskDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timer timer = new Timer();
        CheckAppearance appearance = new CheckAppearance(lobby, event);
        timer.schedule(appearance, taskDate);

    }
}
