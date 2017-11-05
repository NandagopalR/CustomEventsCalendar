package com.nanda.calendarSample.data.events;

import hirondelle.date4j.DateTime;

/**
 * Created by Nandagopal on 10/28/2017.
 */

public class DateClickListener {

    private DateTime dateTime;

    public DateClickListener(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
