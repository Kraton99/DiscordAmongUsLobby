package bm.bot;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

import java.util.*;

@Getter
public class Lobby
{

    private final String time;
    private final User lobbyAdmin;
    private final LinkedList< User > lobbyUserNames = new LinkedList<>();
    private final LinkedList< User > reservesUserNames = new LinkedList<>();
    private boolean hasStarted;

    public Lobby( String time, User lobbyAdmin )
    {
        this.time = time;
        this.lobbyAdmin = lobbyAdmin;
    }

    public void addUser( User username )
    {
        lobbyUserNames.add( username );
    }

    public void addUserReserve( User username )
    {
        reservesUserNames.add( username );
    }

    public void deleteUser( User username )
    {
        lobbyUserNames.remove( username );
    }

    public void deleteUserReserve( User username )
    {
        reservesUserNames.remove( username );
    }

    public Optional<User> searchUserByNameLobby( String name )
    {
        for( User user : lobbyUserNames )
        {
            if( user.getName().toLowerCase().equals( name ) )
            {
                return Optional.of( user );
            }
        }
        return Optional.empty();
    }

    public Optional<User> searchUserByNameReserve( String name )
    {
        for( User user : reservesUserNames )
        {
            if( user.getName().toLowerCase().equals( name ) )
            {
                return Optional.of( user );
            }
        }
        return Optional.empty();
    }

    public List< User > getLobbyUserNames()
    {
        return Collections.unmodifiableList(lobbyUserNames);
    }

    public List< User > getReservesUserNames()
    {
        return Collections.unmodifiableList(reservesUserNames);
    }

    @Override
    public boolean equals( Object aO )
    {
        if( this == aO )
            return true;
        if( aO == null || getClass() != aO.getClass() )
            return false;

        Lobby lobby = ( Lobby ) aO;

        if( !Objects.equals( time, lobby.time ) )
            return false;
        return Objects.equals( lobbyAdmin, lobby.lobbyAdmin );
    }

    @Override
    public int hashCode()
    {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + ( lobbyAdmin != null ? lobbyAdmin.hashCode() : 0 );
        return result;
    }
}
