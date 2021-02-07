package bg.sofia.uni.fmi.mjt.splitwise.server.util.finals;

import java.util.Set;

public final class Commands {
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String ADD_FRIEND = "add-friend";
    public static final String CREATE_GROUP = "create-group";
    public static final String SPLIT = "split";
    public static final String SPLIT_GROUP = "split-group";
    public static final String PAYED = "payed";
    public static final String GET_STATUS = "get-status";
    public static final String GET_HISTORY = "get-history";
    public static final String DISCONNECT = "disconnect";
    public static final String HELP = "help";

    public static Set<String> getAll() {
        return Set.of(REGISTER, LOGIN, LOGOUT, ADD_FRIEND, CREATE_GROUP, SPLIT, SPLIT_GROUP, PAYED, GET_STATUS, DISCONNECT, HELP, GET_HISTORY);
    }
}
