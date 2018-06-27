package com.nanda.calendarSample.data.entity;

import java.util.List;

import hirondelle.date4j.DateTime;

public class CalendarMonthItem {

    private int year;
    private int month;
    private String id;
    private List<DateTime> dateTimeList;
    private String monthYearName;

    public CalendarMonthItem(String id, int year, int month, List<DateTime> dateTimeList) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.dateTimeList = dateTimeList;
    }

    public CalendarMonthItem(String id, int year, int month, List<DateTime> dateTimeList, String monthYearName) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.dateTimeList = dateTimeList;
        this.monthYearName = monthYearName;
    }

    public String getMonthYearName() {
        return monthYearName;
    }

    public void setMonthYearName(String monthYearName) {
        this.monthYearName = monthYearName;
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

    public List<DateTime> getDateTimeList() {
        return dateTimeList;
    }

    public void setDateTimeList(List<DateTime> dateTimeList) {
        this.dateTimeList = dateTimeList;
    }
}
