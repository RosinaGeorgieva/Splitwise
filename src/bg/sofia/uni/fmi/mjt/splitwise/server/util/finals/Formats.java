package bg.sofia.uni.fmi.mjt.splitwise.server.util.finals;

public final class Formats {
    public static final String COMMAND_FORMAT = "%d %s %s %s";
    public static final String AUTH_REQUEST_FORMAT = "%d %s";
    public static final String YOU_OWE_FORMAT = "* %s: You owe %.2f LV%s";
    public static final String YOU_OWE_TO_GROUP_FORMAT = "- %s: You owe %.2f LV%s";
    public static final String FRIEND_OWES_YOU_FORMAT = "* %s: Owes you %.2f LV%s";
    public static final String FRIEND_FROM_GROUP_OWES_YOU_FORMAT = "- %s: Owes you %.2f LV%s";
    public static final String FRIEND_PAYED_FORMAT = "[ %s payed you %.2f LV ]%s";
    public static final String YOU_PAYED_FORMAT = "[ You payed %s %.2f LV ]"; //da dobavq data ako ostane vreme
    public static final String GROUP_TITLE_FORMAT = "* %s %s";
}
