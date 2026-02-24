import java.io.*;
import java.net.*;
import java.util.*;

public class MultiClientServer {

    // Store clientName -> ClientHandler
    private static Map<String, ClientHandler> clients =
            Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(6013)) {

            System.out.println("Server started on port 6013...");
            System.out.println("Waiting for clients...");
            new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                broadcast("[SERVER]: " + message);
            }
        }).start();


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client attempting to connect...");
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start(); // MULTITHREADING
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast to all clients
    private static void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients.values()) {
                client.sendMessage(message);
            }
        }
    }

    // ================= THREAD CLASS =================
    static class ClientHandler extends Thread {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void run() {

            try {
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(
                        socket.getOutputStream(), true);

                // Ask client name
                out.println("Enter your name:");
                clientName = in.readLine();

                synchronized (clients) {

                    while (clientName == null ||
                           clientName.trim().isEmpty() ||
                           clients.containsKey(clientName)) {

                        out.println("Name invalid or already taken. Enter another name:");
                        clientName = in.readLine();
                    }

                    clients.put(clientName, this);
                }

                System.out.println(clientName + " has joined.");
                broadcast(">>> " + clientName + " joined the chat.");

                String message;

                while ((message = in.readLine()) != null) {

                    String formattedMessage =
                            "[" + clientName + "]: " + message;

                    System.out.println(formattedMessage);

                    broadcast(formattedMessage);
                }

            } catch (IOException e) {
                System.out.println("Connection lost with client.");
            } finally {

                try {
                    if (clientName != null) {
                        clients.remove(clientName);
                        broadcast(">>> " + clientName + " left the chat.");
                        System.out.println(clientName + " disconnected.");
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}