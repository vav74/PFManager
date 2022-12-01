import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Client {
    static final int PURCHASES_TO_GENERATE = 10;
    public static void main(String[] args) throws IOException, CsvException {
        String host = "localhost";
        int port = 8989;
        List<Purchase> purchases = generatePurchases();
        for (Purchase purchase : purchases) {
            try (Socket clientSocket = new Socket(host, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
                out.println(gson.toJson(purchase));
                String resp = in.readLine();
                System.out.println(resp);
            }
        }
    }

    public static List<Purchase> generatePurchases() throws IOException, CsvException {
        Random random = new Random();
        List<String> titles = new ArrayList<>();
        titles.add("микрофон");
        titles.add("телефон");
        titles.add("подшипник");
        titles.add("удочка");
        CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("categories.tsv")).withCSVParser(parser).build()) {
            List<String[]> allData = reader.readAll();
            allData.forEach(v -> titles.add(v[0]));
        }
        List<Purchase> purchases = new ArrayList<>();
        int minDay = (int) LocalDate.of(2022, 10, 20).toEpochDay();
        int maxDay = (int) LocalDate.of(2022, 11, 20).toEpochDay();
        for (int i = 0; i < PURCHASES_TO_GENERATE; i++) {
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            purchases.add(new Purchase(
                    titles.get(random.nextInt(titles.size())),
                    LocalDate.ofEpochDay(randomDay).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    Math.round((random.nextInt(1000)+10.0f) / 10) * 10)
            );
        }
        System.out.println("Сгенерировано "+ PURCHASES_TO_GENERATE + " запросов");
        System.out.println(purchases); //for debug
        return purchases;
    }
}