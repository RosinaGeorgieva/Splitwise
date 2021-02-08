package bg.sofia.uni.fmi.mjt.splitwise.server.util.finals;

import static bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Delimiters.LINE_SEPARATOR;

public final class Messages {
    public static final String INVALID_USERNAME = "[ Username %s is invalid, select a valid one. ]%s";
    public static final String USERNAME_ALREADY_TAKEN = "[ Username %s is already taken, select another one. ]%s";
    public static final String SUCCESSFULLY_REGISTERED = "[ Username %s successfully registered. ]%s";
    public static final String SUCCESSFUL_LOGIN = "[ User %s successfully logged in. ]%s";
    public static final String ALREADY_LOGGED_IN = "[ You are already logged in. ]%s";
    public static final String INCORRECT_CREDENTIALS = "[ Invalid username/password combination. ]%s";
    public static final String SUCCESSFUL_LOGOUT = "[ ~%s~ is successfully logged out. ]%s";
    public static final String NOT_LOGGED_IN = "[ You are not logged in. ]%s";
    public static final String NO_SUCH_REGISTERED = "[ User with username %s is not registered. ]%s";
    public static final String OK = "OK";
    public static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server. ]" + LINE_SEPARATOR;
    public static final String UNKNOWN_COMMAND = "[ Unknown command. ]" + LINE_SEPARATOR;
    public static final String SUCCESSFULLY_ADDED_FRIEND = "[ User %s successfully added as a friend. ]%s";
    public static final String ALREADY_FRIENDS = "[ User %s is already your friend. ]%s";
    public static final String NOT_ENOUGH_USERS = "[ Entered users are not enough to form a group. ]%s";
    public static final String GROUP_ALREADY_EXISTS = "[ Group already exists. ]%s";
    public static final String SUCCESSFULLY_CREATED_GROUP = "[ Group %s with creator %s successfully registered. ]%s";
    public static final String ADD_YOURSELF_TO_GROUP = "[ You cannot add yourself to the group - you are automatically added. ]%s";
    public static final String OBLIGATION_ADDED_SUCCESSFULLY = "[ Successfully added %.2fLV owed by %s for %s. ]%s";
    public static final String NO_SUCH_GROUP = "[ A group with name %s does not exist. ]%s";
    public static final String CANNOT_SPLIT_WITH_YOUSELF = "[ You cannot split a sum with youself. ]%s";
    public static final String CANNOT_ADD_YOUSELF_AS_FRIEND = "[ Don't you have any other friends? ]%s";
    public static final String NO_PAYMENTS_EXECUTED = "[ No payments have been executed by you yet. ]%s";
    public static final String NO_NOTIFICATIONS = "[ No notifications to show. ]%s";
    public static final String NOTIFICATIONS = "~ Notifications ~" + LINE_SEPARATOR;
    public static final String YOU_OWE = "[ You owe %s %.2fLV for %s. ]%s"; //ne tuk??? ima pochti syhsstoto vyv formati
    public static final String CONNECTION_ERROR_MESSAGE = "[ Server connection error. Please try again later. ]";
}
