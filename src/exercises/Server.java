
// Server.java
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int DEFAULT_PORT = 5000;
    private int port;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private boolean running = false;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("Server started on port " + port);

        new Thread(() -> {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);
                    ClientHandler clientHandler = new ClientHandler(clientSocket, clients.size() + 1);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() throws IOException {
        running = false;
        for (ClientHandler client : clients) {
            client.close();
        }
        clients.clear();
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    public void sendToClient(int clientNumber, String message) {
        if (clientNumber > 0 && clientNumber <= clients.size()) {
            clients.get(clientNumber - 1).sendMessage(message);
            System.out.println("Sent to client " + clientNumber + ": " + message);
        } else {
            System.out.println("Invalid client number. There are " + clients.size() + " clients connected.");
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private int clientNumber;

        public ClientHandler(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            try {
                this.out = new PrintWriter(socket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void close() throws IOException {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received from client " + clientNumber + ": " + message);
                    // You can add custom message handling here
                }
            } catch (IOException e) {
                System.out.println("Client " + clientNumber + " disconnected");
            } finally {
                clients.remove(this);
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
