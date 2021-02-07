package bg.sofia.uni.fmi.mjt.splitwise.server.auth;

import bg.sofia.uni.fmi.mjt.splitwise.server.AbstractServer;
import bg.sofia.uni.fmi.mjt.splitwise.server.exceptions.ClientDisconnectedException;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.AuthDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SplitwiseAuthenticationServer extends AbstractServer {

    private final Database authenticationDatabase;

    public SplitwiseAuthenticationServer(int serverPort) {
        super(serverPort);
        this.authenticationDatabase = new AuthDatabase();
    }

    @Override
    public void start() {
        super.start();
        runServer();
    }

    private void runServer() {
        try {
            while (isRunning) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        String request;
                        try {
                            request = receiveRequest(sc);
                        } catch (ClientDisconnectedException exception) {
                            continue;
                        }
                        request = processRequest(authenticationDatabase, request);
                        sendResponse(sc, request);
                    } else if (key.isAcceptable()) {
                        receiveConnection(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception.getMessage(), exception);
        }
    }
}