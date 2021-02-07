package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.client.AuthClient;
import bg.sofia.uni.fmi.mjt.splitwise.client.Client;
import bg.sofia.uni.fmi.mjt.splitwise.server.AbstractServer;
import bg.sofia.uni.fmi.mjt.splitwise.server.Server;
import bg.sofia.uni.fmi.mjt.splitwise.server.ServerThread;
import bg.sofia.uni.fmi.mjt.splitwise.server.auth.SplitwiseAuthenticationServer;
import bg.sofia.uni.fmi.mjt.splitwise.server.exceptions.ClientDisconnectedException;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.SplitwiseDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.server.util.finals.Formats;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SplitwiseServer extends AbstractServer {

    private final Server authenticationServer; //eventualno v dr proekt
    private final Client authenticationClient;
    private final Database splitwiseDatabase;

    public SplitwiseServer(int serverPort) {
        super(serverPort);
        this.authenticationServer = new SplitwiseAuthenticationServer(this.serverPort + 1);
        this.authenticationClient = new AuthClient(this.serverPort + 1);
        this.splitwiseDatabase = new SplitwiseDatabase();
    }

    @Override
    public void start() {
        new ServerThread(authenticationServer).start();
        authenticationClient.connect();

        super.start();

        runServer();
    }

    @Override
    public void stop() {
        authenticationServer.stop();
        super.stop();
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

                        Integer currentSessionId = (Integer) key.attachment();
                        request = String.format(Formats.AUTH_REQUEST_FORMAT, currentSessionId, request);

                        authenticationClient.sendRequest(request);
                        String authenticationResponse = authenticationClient.receiveResponse();

                        request = processRequest(splitwiseDatabase, request, authenticationResponse);
                        sendResponse(sc, request);
                    } else if (key.isAcceptable()) {
                        receiveConnection(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(CONNECTION_PROBLEM, exception);
        }
    }
}
