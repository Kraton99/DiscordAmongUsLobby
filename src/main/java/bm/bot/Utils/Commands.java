package bm.bot.Utils;

public enum Commands {


    HELP("help"),
    KICK("kick"),
    JOIN("join"),
    LEAVE("leave"),
    LOBBY_INFO("lobbyinfo"),
    ACTIVE_LOBBIES("activelobbies"),
    AMONG_LOBBY("amonglobby"),
    DELETE_AMONG_LOBBY("deleteamonglobby");

    private final String name;

    private Commands(String aName) {
        name = aName;
    }
}
