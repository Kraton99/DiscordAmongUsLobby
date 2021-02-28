import net.dv8tion.jda.api.entities.User;

import java.util.LinkedList;

public class Lobby {
    String time;
    LinkedList<User> lobbyusernames = new LinkedList<>();
    LinkedList<User> reserveusernames = new LinkedList<>();
    User lobbyAdmin;
    boolean hasStarted;

    Lobby(String time, User lobbyAdmin){
        this.time = time;
        this.lobbyAdmin = lobbyAdmin;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public User getLobbyAdmin(){
        return lobbyAdmin;
    }

    public void addUser(User username){
            lobbyusernames.add(username);
        }

        public void addUserReserve(User username) {
            reserveusernames.add(username);
        }

    public void deleteUser(User username){
        lobbyusernames.remove(username);
    }

    public void deleteUserReserve(User username){
        reserveusernames.remove(username);
    }

    public void sethasStarted(boolean value) {
        this.hasStarted = value;
    }

    public User searchUserByNameLobby(String name) {
        for(User user : lobbyusernames) {
            if(user.getName().toLowerCase().equals(name)){
                return user;
            }
        }
        return null;
    }
    public User searchUserByNameReserve(String name) {
        for(User user : reserveusernames) {
            if(user.getName().toLowerCase().equals(name)){
                return user;
            }
        }
        return null;
    }

}
