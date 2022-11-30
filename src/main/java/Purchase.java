import com.google.gson.annotations.Expose;

public class Purchase{
    @Expose
    private String title;
    @Expose
    private String date;
    @Expose
    private int sum;

    private String cat = "";

    public Purchase(String title, String date, int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public Purchase(String title, String date, int sum, String cat) {
        this.title = title;
        this.date = date;
        this.sum = sum;
        this.cat = cat;
    }

    public Purchase() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }

    public String getCat() {
        return cat;
    }

    @Override
    public String toString() {
        return "{ " + title + ", " + date + ", " + sum + ", " + cat + " }\n";
    }
}