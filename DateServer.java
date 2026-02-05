import java.io.*;
import java.net.*;

public class DateServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(6013)) {
            System.out.println("Server started, waiting for client...");
            Socket client = server.accept();
            System.out.println("Client connected.");

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Client: " + clientMessage);

                System.out.print("Server reply: ");
                String serverReply = console.readLine();
                out.println(serverReply);
            }

            client.close();
            System.out.println("Client disconnected.");

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
