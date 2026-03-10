import java.io.*;
import java.net.*;
import java.util.*;

public class MultiClientServer {

    private static final int PORT = 6013;

    // Username -> Output Stream
    private static HashMap<String, PrintWriter> userMap = new HashMap<>();

    // Username -> IP Address
    private static HashMap<String, String> clientIPMap = new HashMap<>();

    // Username -> Thread
    private static HashMap<String, Thread> clientThreadMap = new HashMap<>();


    public static void main(String[] args) {

        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }


    // Display active users on server
    private static void displayActiveUsers() {

        System.out.println("\n--- Current Active Users (" + userMap.size() + ") ---");

        for (String user : userMap.keySet()) {
            System.out.println(" > " + user + " | IP: " + clientIPMap.get(user));
        }

        System.out.println("---------------------------------\n");
    }


    // Send updated client list to all clients
    private static void sendClientList() {

        StringBuilder list = new StringBuilder("CLIENTLIST ");

        for (String user : userMap.keySet()) {
            list.append(user).append(" ");
        }

        for (PrintWriter writer : userMap.values()) {
            writer.println(list.toString());
        }
    }



    private static class ClientHandler extends Thread {

        private Socket socket;
        private String name;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }


        public void run() {

            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);


                // USERNAME REGISTRATION
                while (true) {

                    out.println("SUBMITNAME");

                    name = in.readLine();

                    if (name == null)
                        return;

                    synchronized (userMap) {

                        if (!name.isEmpty() && !userMap.containsKey(name)) {

                            userMap.put(name, out);
                            clientIPMap.put(name, socket.getInetAddress().toString());
                            clientThreadMap.put(name, Thread.currentThread());

                            break;
                        }
                    }
                }


                out.println("NAMEACCEPTED " + name);

                System.out.println(name + " has joined the chat.");

                displayActiveUsers();
                sendClientList();


                String message;

                while ((message = in.readLine()) != null) {

                    // LIST command
                    if (message.equalsIgnoreCase("LIST")) {
                        sendClientList();
                    }

                    // PRIVATE MESSAGE
                    else if (message.startsWith("@")) {

                        int spaceIndex = message.indexOf(" ");

                        if (spaceIndex != -1) {

                            String recipient = message.substring(1, spaceIndex);
                            String privateMsg = message.substring(spaceIndex + 1);

                            sendPrivateMessage(recipient, "Private from " + name + ": " + privateMsg);
                        }
                    }

                    // BROADCAST COMMAND
                    else if (message.startsWith("BROADCAST ")) {

                        String msg = message.substring(10);

                        broadcast("Broadcast from " + name + ": " + msg);
                    }

                    // KICK CLIENT
                    else if (message.startsWith("KICK ")) {

                        String user = message.substring(5);

                        Thread t = clientThreadMap.get(user);

                        if (t != null) {
                            broadcast("System: " + user + " was removed by server.");
                            t.interrupt();
                        }
                    }

                    // NORMAL PUBLIC MESSAGE
                    else {

                        broadcast(name + ": " + message);
                    }
                }

            } catch (IOException e) {

                System.out.println(name + " disconnected.");

            } finally {

                if (name != null) {

                    userMap.remove(name);
                    clientIPMap.remove(name);
                    clientThreadMap.remove(name);

                    System.out.println(name + " left the chat.");

                    displayActiveUsers();
                    sendClientList();
                }

                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }


        // BROADCAST MESSAGE
        private void broadcast(String msg) {

            for (PrintWriter writer : userMap.values()) {
                writer.println(msg);
            }
        }


        // PRIVATE MESSAGE
        private void sendPrivateMessage(String recipient, String msg) {

            PrintWriter writer = userMap.get(recipient);

            if (writer != null) {

                writer.println(msg);

            } else {

                out.println("System: User '" + recipient + "' not found.");
            }
        }
    }
}