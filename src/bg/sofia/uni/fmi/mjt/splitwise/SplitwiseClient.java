package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.client.AbstractClient;

import java.util.Scanner;

public class SplitwiseClient extends AbstractClient {
    private static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]" + System.lineSeparator(); //da se manat ot tuk
    private static final String REQUEST_INPUT = "=> ";

    public SplitwiseClient(int serverPort) {
        super(serverPort);
    }

    public void execute() {
        Scanner sc = new Scanner(System.in);
        connect();
        while (isConnectedToServer) {
            System.out.print(REQUEST_INPUT);
            String request = sc.nextLine();
            sendRequest(request);
            String response = receiveResponse();

            if (response.equals(DISCONNECTED_FROM_SERVER)) {
                disconnect();
            }
            System.out.print(response);
        }
        sc.close();
    }
}