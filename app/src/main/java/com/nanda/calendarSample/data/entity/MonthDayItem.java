package com.nanda.calendarSample.data.entity;

import hirondelle.date4j.DateTime;

/**
 * Created by HP-PC on 02-02-2018.
 */

public class MonthDayItem {
    private DateTime dateTime;
    private boolean isSelected;

    public MonthDayItem(DateTime dateTime, boolean isSelected) {
        this.dateTime = dateTime;
        this.isSelected = isSelected;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
