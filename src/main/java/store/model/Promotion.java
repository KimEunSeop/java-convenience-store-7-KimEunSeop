package store.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Promotion {
    private String name;
    private int buyCount;
    private int getCount;
    private String startDate;
    private String endDate;

    public Promotion(String name, int buyCount, int getCount, String startDate, String endDate) {
        this.name = name;
        this.buyCount = buyCount;
        this.getCount = getCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getGetCount() {
        return getCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
