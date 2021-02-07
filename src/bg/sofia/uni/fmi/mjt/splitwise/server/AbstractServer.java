package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.ExecutableCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.command.ExecutableCommandCreator;
import bg.sofia.uni.fmi.mjt.splitwise.server.exceptions.ClientDisconnectedException;
import bg.sofia.uni.fmi.mjt.splitwise.server.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.splitwise.server.database.Database;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractServer implements Server {
    private static final String CLIENT_DISCONNECTED_FROM_SERVER = "Client has disconnected from server";
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 2048;
    private static final String LINE_SEPARATOR = System.lineSeparator();

    protected static final String CONNECTION_PROBLEM = "[ There is a problem with the server connection ]"
            + LINE_SEPARATOR;
    protected static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]" + LINE_SEPARATOR;

    protected final Set<SocketChannel> clientSocketChannels;
    protected final int serverPort;

    protected ServerSocketChannel serverSocketChannel;
    protected Selector selector;
    protected ByteBuffer buffer;
    protected Integer sessionId = 0;
    protected boolean isRunning;

    public AbstractServer(int serverPort) {
        this.serverPort = serverPort;
        this.isRunning = false;
        this.clientSocketChannels = new HashSet<>();
    }

    @Override
    public void start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isRunning = true;
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public void stop() {
        try {
            isRunning = false;
            for (SocketChannel socketChannel : clientSocketChannels) {
                ByteBuffer newBuffer = ByteBuffer.allocate(BUFFER_SIZE); //??
                newBuffer.clear();
                newBuffer.put(DISCONNECTED_FROM_SERVER.getBytes(StandardCharsets.UTF_8));
                socketChannel.write(newBuffer);
            }
            serverSocketChannel.close();
            selector.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public void receiveConnection(SelectionKey key) {
        try {
            ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
            SocketChannel accept = sockChannel.accept();
            clientSocketChannels.add(accept);
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ).attach(sessionId);
            sessionId++;
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public String receiveRequest(SocketChannel socketChannel) throws ClientDisconnectedException {
        try {
            buffer.clear();
            int r = socketChannel.read(buffer);
            if (r < 0) {
                socketChannel.close();
                throw new ClientDisconnectedException(CLIENT_DISCONNECTED_FROM_SERVER);
            }
            buffer.flip();
            return new String(buffer.array(), 0, buffer.limit());
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public String sendResponse(SocketChannel socketChannel, String response) {
        try {
            buffer.clear();
            buffer.put(response.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            socketChannel.write(buffer);
            return response;
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public String processRequest(Database database, String... command) {
        try {
            ExecutableCommand executableCommand = ExecutableCommandCreator.createSplitwiseCommand(command);
            return executableCommand.execute(database);
        } catch (UnknownCommandException exception) {
            return exception.getMessage();
        }
    }
}
