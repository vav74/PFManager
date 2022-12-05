import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException, CsvException, ClassNotFoundException {
        Stat stat = new Stat();
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