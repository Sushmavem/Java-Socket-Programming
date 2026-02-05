import java.net.*;
import java.io.*;
import java.util.*;

public class DateServer {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(6013);
            System.out.println("Server started... Waiting for client");

            Socket client = server.accept();

            PrintWriter out = new PrintWriter(
                    client.getOutputStream(), true);

            out.println(new Date().toString());

            client.close();
            server.close();

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
