package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.server.exceptions.ClientDisconnectedException;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public interface Server {
    void start();

    void stop();

    void receiveConnection(SelectionKey key);

    String receiveRequest(SocketChannel socketChannel) throws ClientDisconnectedException;

    String sendResponse(SocketChannel socketChannel, String request);

    String processRequest( Database database, String... request);
}
