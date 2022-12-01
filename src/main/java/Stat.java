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
        Purchase last = purchaseList.get(purchaseList.size() - 1);
        String lastYear = last.getDate().substring(0, 4);
        String lastMonth = last.getDate().substring(0, 7);
        String lastDay = last.getDate();
        return new MaxCat(maxCategory(),
                maxYearCategory(lastYear),
                maxMonthCategory(lastMonth),
                maxDayCategory(lastDay));
    }

    public MaxCategory maxCategory() {
        Map.Entry<String, Integer> entry = purchaseList.stream()
                .collect(Collectors.groupingBy(Purchase::getCat, Collectors.summingInt(Purchase::getSum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        assert entry != null;
        return new MaxCategory(entry.getKey(), entry.getValue());
    }

    public MaxYearCategory maxYearCategory(String lastYear) {
        Map.Entry<String, Integer> entry = purchaseList.stream()
                .filter(x -> x.getDate().startsWith(lastYear))
                .collect(Collectors.groupingBy(Purchase::getCat, Collectors.summingInt(Purchase::getSum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        assert entry != null;
        return new MaxYearCategory(entry.getKey(), entry.getValue());
    }

    public MaxMonthCategory maxMonthCategory(String lastMonth) {
        Map.Entry<String, Integer> entry = purchaseList.stream()
                .filter(x -> x.getDate().startsWith(lastMonth))
                .collect(Collectors.groupingBy(Purchase::getCat, Collectors.summingInt(Purchase::getSum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        assert entry != null;
        return new MaxMonthCategory(entry.getKey(), entry.getValue());
    }

    public MaxDayCategory maxDayCategory(String lastDay) {
        Map.Entry<String, Integer> maxCategoryEntryDay = purchaseList.stream()
                .filter(x -> x.getDate().equals(lastDay))
                .collect(Collectors.groupingBy(Purchase::getCat, Collectors.summingInt(Purchase::getSum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        assert maxCategoryEntryDay != null;
        return new MaxDayCategory(maxCategoryEntryDay.getKey(), maxCategoryEntryDay.getValue());
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