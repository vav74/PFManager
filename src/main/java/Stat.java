import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Stat {
    private final Map<String, String> categoriesTsv;
    protected List<Purchase> purchaseList;
    static public File file;

    public Stat() throws IOException, CsvException, ClassNotFoundException {
        file = new File("data.bin");
        categoriesTsv = new HashMap<>();
        purchaseList = new ArrayList<>();
        if (file.exists()) {
            loadPurchases();
        }
        readFileTSV();
    }

    public void readFileTSV() throws IOException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("categories.tsv")).withCSVParser(parser).build()) {
            List<String[]> allData = reader.readAll();
            allData.forEach(v -> categoriesTsv.put(v[0], v[1]));
        }
    }

    public void addNewPurchase(String p) throws IOException {
        Purchase purchase = new ObjectMapper().readValue(p, Purchase.class);
        purchase.setCat(categoriesTsv.getOrDefault(purchase.getTitle(), "other"));
        purchaseList.add(purchase);
        safePurchases();
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

    public void safePurchases() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(purchaseList);
            oos.flush();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadPurchases() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis))) {
            purchaseList = (ArrayList<Purchase>) ois.readObject();
            System.out.println("Из файла " + file.getName() + " загружено " + purchaseList.size() + " записей");
        }
    }

}