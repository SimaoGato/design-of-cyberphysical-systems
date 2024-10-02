
// Client.java
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class Client {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> messageHandler;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to server");

        // Start a thread to read messages from the server
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (messageHandler != null) {
                        messageHandler.accept(message);
                    } else {
                        System.out.println("Received from server: " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void setMessageHandler(Consumer<String> handler) {
        this.messageHandler = handler;
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
