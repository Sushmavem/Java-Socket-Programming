import java.io.*;
import java.net.*;

public class DateClient {

    public static void main(String[] args) {
        String serverIP = "172.16.32.102"; 
        int port = 6013;

        try {
            Socket socket = new Socket(serverIP, port);
            System.out.println("Connected to DateServer");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            socket.close();

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
