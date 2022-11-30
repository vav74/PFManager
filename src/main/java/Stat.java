import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stat {
    private static Map<String, String> mapFromTsvFile;
    private static List<Purchase> purchaseList;

    public Stat() throws IOException, CsvException {
        mapFromTsvFile = new HashMap<>();
        purchaseList = new ArrayList<>();
        readFileTSV();
    }

    public static void readFileTSV() throws IOException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("categories.tsv")).withCSVParser(parser).build()) {
            List<String[]> allData = reader.readAll();
            allData.forEach(v -> mapFromTsvFile.put(v[0], v[1]));
        }
    }

    public void addNewPurchase(String p) throws JsonProcessingException {
        Purchase purchase = new ObjectMapper().readValue(p, Purchase.class);
        purchase.setCat(mapFromTsvFile.getOrDefault(purchase.getTitle(), "other"));
        purchaseList.add(purchase);
    }

    public MaxCat getMaxCat() {
        Map.Entry<String, Integer> maxCategoryEntry = purchaseList.stream()
                .collect(Collectors.groupingBy(Purchase::getCat, Collectors.summingInt(Purchase::getSum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        assert maxCategoryEntry != null;
        return new MaxCat(new MaxCategory(maxCategoryEntry.getKey(), maxCategoryEntry.getValue()));
    }

}