import java.io.*;
import java.net.*;

public class DateClient {
    public static void main(String[] args) {
        try {
            String serverIP = "172.16.32.102"; 
            Socket socket = new Socket(serverIP, 6013);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            String userMessage;
            while (true) {
                System.out.print("Client message: ");
                userMessage = console.readLine();
                if (userMessage.equalsIgnoreCase("exit")) break;

                out.println(userMessage);          // send to server
                String serverResponse = in.readLine();  // read server response
                System.out.println("Server: " + serverResponse);
            }

            socket.close();
            System.out.println("Disconnected from server.");

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
