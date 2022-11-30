import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static Stat stat;
    public static void main(String[] args) throws IOException, CsvException {
        stat = new Stat();
        try (ServerSocket serverSocket = new ServerSocket(8989)) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                        BufferedReader in = new BufferedReader(inputStreamReader);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
                ) {
                    String request = in.readLine();
                    if (request.equals("ShutDown")) {
                        stat=null;
                        inputStreamReader.close();
                        in.close();
                        out.flush();
                        out.close();
                        socket.close();
                        serverSocket.close();
                        System.out.println("Stopped");
                        break;
                    }
                    System.out.println(request);
                    stat.addNewPurchase(request);
                    String maxCatJson = new ObjectMapper().writeValueAsString(stat.getMaxCat());
                    out.println(maxCatJson);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}