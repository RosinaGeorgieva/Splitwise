import bg.sofia.uni.fmi.mjt.splitwise.SplitwiseServer;
import bg.sofia.uni.fmi.mjt.splitwise.server.Server;

public class App {
    public static void main(String... args) {
        Server server = new SplitwiseServer(7778);
        server.start();
    }
}
