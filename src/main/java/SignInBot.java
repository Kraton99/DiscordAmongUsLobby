import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import net.dv8tion.jda.internal.handle.GuildSetupController;
import org.w3c.dom.Text;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;


public class SignInBot {

    static LinkedList<Lobby> lobbies = new LinkedList<>();
    static HashMap<Lobby, Timer> lobbyInitiators = new HashMap();
    static HashMap<User, Timer> userTokick = new HashMap<>();

    SignInBot() throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault("#TOKEN HERE#").build();
        jda.addEventListener(new JoinLeaveLobbyCallReceived());
        jda.addEventListener(new MessageGuard());
    }


    static boolean lobbyExists(String time, String admin) {
        for (Lobby lobby : lobbies) {
            if (lobby.getTime().equals(time) && lobby.getLobbyAdmin().getName().toLowerCase().equals(admin)) {
                return true;
            }
        }
        return false;
    }

    static Lobby getLobby(String time, String admin) {
        for (Lobby lobby : lobbies) {
            if (lobby.getTime().equals(time) && lobby.getLobbyAdmin().getName().toLowerCase().equals(admin)) {
                return lobby;
            }
        }
        return null;
    }
}

