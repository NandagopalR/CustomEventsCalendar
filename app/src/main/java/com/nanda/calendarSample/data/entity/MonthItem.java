package com.nanda.calendarSample.data.entity;

/**
 * Created by Nandagopal on 11/6/2017.
 */

public class MonthItem {

    private String monthName;
    private int year;

    public MonthItem(String monthName, int year) {
        this.monthName = monthName;
        this.year = year;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
