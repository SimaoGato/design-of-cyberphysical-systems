// Server.java
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;
    private List<ClientHandler> clients = new ArrayList<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients.size() + 1);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        private int clientNumber;

        public ClientHandler(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            try {
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received from client " + clientNumber + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Client " + clientNumber + " disconnected");
            } finally {
                clients.remove(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter client number and message (e.g., '1 Hello'): ");
                String input = scanner.nextLine();
                String[] parts = input.split(" ", 2);
                if (parts.length == 2) {
                    try {
                        int clientNumber = Integer.parseInt(parts[0]);
                        String message = parts[1];
                        server.sendToClient(clientNumber, message);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid client number. Please enter a number followed by a message.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a client number followed by a message.");
                }
            }
        }).start();
        server.start();
    }
}
