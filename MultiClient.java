import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MultiClient {

    public static void main(String[] args) {

        try (Socket socket = new Socket("172.16.32.102", 6013)) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            Thread listener = new Thread(() -> {
                try {
                    String serverResponse;

                    while ((serverResponse = in.readLine()) != null) {
                        if (serverResponse.startsWith("SUBMITNAME")) {
                            System.out.print("Enter unique username: ");
                            out.println(scanner.nextLine());
                        } else {
                            System.out.println(serverResponse);
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            listener.start();

            System.out.println("\n------ CHAT COMMANDS ------");
            System.out.println("Normal text -> Public message");
            System.out.println("@username message -> Private message");
            System.out.println("BROADCAST message -> Broadcast to everyone");
            System.out.println("LIST -> Show connected users");
            System.out.println("exit -> Leave chat");
            System.out.println("---------------------------\n");

            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();

                if (msg.equalsIgnoreCase("exit")) {
                    scanner.close();
                    socket.close();
                    break;
                }

                out.println(msg);
            }

        } catch (IOException e) {
            System.out.println("Could not connect to server: " + e);
        }
    }
}