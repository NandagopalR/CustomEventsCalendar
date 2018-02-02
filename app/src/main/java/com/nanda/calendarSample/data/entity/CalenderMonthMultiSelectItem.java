package com.nanda.calendarSample.data.entity;

import java.util.List;

/**
 * Created by ram on 2/2/18.
 */

public class CalenderMonthMultiSelectItem {
    private int year;
    private int month;
    private String id;
    private List<MonthDayItem> dateTimeList;

    public CalenderMonthMultiSelectItem(String id, int year, int month, List<MonthDayItem> dateTimeList) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.dateTimeList = dateTimeList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<MonthDayItem> getDateTimeList() {
        return dateTimeList;
    }

    public void setDateTimeList(List<MonthDayItem> dateTimeList) {
        this.dateTimeList = dateTimeList;
    }
}
