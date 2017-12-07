package com.nanda.calendarSample.data.eventbus;

import com.nanda.calendarSample.data.entity.EventsItem;

import java.util.Map;

import hirondelle.date4j.DateTime;

public class CalendarEvent {

    private Map<DateTime, EventsItem> eventsItemMap;

    public CalendarEvent(Map<DateTime, EventsItem> eventsItemMap) {
        this.eventsItemMap = eventsItemMap;
    }

    public Map<DateTime, EventsItem> getEventsItemMap() {
        return eventsItemMap;
    }

    public void setEventsItemMap(Map<DateTime, EventsItem> eventsItemMap) {
        this.eventsItemMap = eventsItemMap;
    }
}
