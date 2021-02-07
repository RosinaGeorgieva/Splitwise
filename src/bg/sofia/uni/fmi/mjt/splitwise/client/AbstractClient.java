package bg.sofia.uni.fmi.mjt.splitwise.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public abstract class AbstractClient implements Client {
    protected static final String SERVER_HOST = "localhost";
    protected static final int DEFAULT_SERVER_PORT = 7778;
    protected static final int BUFFER_SIZE = 1024;

    protected int serverPort;
    protected boolean isConnectedToServer;
    protected SocketChannel socketChannel;
    protected ByteBuffer buffer;

    public AbstractClient() {
        this.serverPort = DEFAULT_SERVER_PORT;
    }

    public AbstractClient(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void connect() {
        try {
            socketChannel = SocketChannel.open();
            buffer = ByteBuffer.allocate(BUFFER_SIZE);

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, serverPort));
            isConnectedToServer = true;
        } catch (IOException exception) {
            throw new UncheckedIOException(exception.getMessage(), exception);
        }
    }

    @Override
    public String receiveResponse() {
        try {

            buffer.clear();
            socketChannel.read(buffer);
            buffer.flip();
            return new String(buffer.array(), 0, buffer.limit());

        } catch (IOException exception) {
            throw new UncheckedIOException(exception.getMessage(), exception);
        }
    }

    @Override
    public void disconnect() {
        try {
            isConnectedToServer = false;
            socketChannel.close();
        } catch (IOException exception) {
            throw new UncheckedIOException(exception.getMessage(), exception);
        }
    }

    @Override
    public void sendRequest(String request) {
        try {
            buffer.clear();
            buffer.put(request.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            socketChannel.write(buffer);
        } catch (IOException exception) {
            //todo
        }
    }
}
