package bg.sofia.uni.fmi.mjt.splitwise.client;

public interface Client {

    void connect();

    void sendRequest(String request);

    String receiveResponse();

    void disconnect();
}
