package bg.sofia.uni.fmi.mjt.splitwise.server.command;

public abstract class SplitwiseCommand extends ExecutableCommand {
    protected String authResponse;

    public SplitwiseCommand(Command command, String authResponse) {
        super(command);
        this.authResponse = authResponse;
    }
}
