package bg.sofia.uni.fmi.mjt.splitwise.server.command.authentication;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.AuthenticationCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.AuthDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Messages;

public class ConfirmDisconnect extends AuthenticationCommand {//commandite ot tazi seria da pomislq dali da se kazvat confirm
    public ConfirmDisconnect(Command command) {
        super(command);
    }

    @Override
    public String execute(Database database) { //tuk drugo pravim li?? kak mahame vryzkata na klienta sys server-a?
        ((AuthDatabase)database).getSessionRepository().remove(command.sessionId());
        return Messages.DISCONNECTED_FROM_SERVER;
    }
}
