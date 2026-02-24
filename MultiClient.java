import java.io.*;
import java.net.*;

public class MultiClient {

    public static void main(String[] args) {

        try (Socket socket = new Socket("11.20.10.132", 6013)) {

            BufferedReader console =
                    new BufferedReader(new InputStreamReader(System.in));

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);

            // Thread to read messages from server
            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            receiveThread.start();

            // Send messages to server
            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



