import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ShutDownServer {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 8989;
        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("ShutDown");
            System.out.println("Shutdown server");
        }
    }
}