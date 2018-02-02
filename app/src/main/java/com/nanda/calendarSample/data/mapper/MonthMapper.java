package com.nanda.calendarSample.data.mapper;

import com.nanda.calendarSample.data.entity.CalendarMonthItem;
import com.nanda.calendarSample.data.entity.CalenderMonthMultiSelectItem;
import com.nanda.calendarSample.data.entity.MonthDayItem;
import com.nanda.calendarSample.data.entity.MonthItem;
import com.nanda.calendarSample.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

import hirondelle.date4j.DateTime;


public class MonthMapper {

    public static List<MonthItem> convertModelToEntityList(String[] monthArray, int year) {
        if (monthArray == null) {
            return null;
        }
        List<MonthItem> monthList = new ArrayList<>();
        for (int i = 0, monthArrayLength = monthArray.length; i < monthArrayLength; i++) {
            String item = monthArray[i];
            monthList.add(new MonthItem(String.format("%s, %d", item, year), year));
        }
        return monthList;
    }

    public static List<CalendarMonthItem> convertModelToMonthEntityList(int year) {
        List<CalendarMonthItem> calendarMonthItemList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            List<DateTime> dateTimeList = CalendarUtils.getFullWeeks(i, year, 1, true);
            String id = String.format("%d-%d", i, year);
            calendarMonthItemList.add(new CalendarMonthItem(id, year, i, dateTimeList));
        }

        return calendarMonthItemList;
    }

    public static List<CalenderMonthMultiSelectItem> convertModelToMonthMultiSelectEntityList(int year) {
        List<CalenderMonthMultiSelectItem> calendarMonthItemList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            List<MonthDayItem> dateList=new ArrayList<>();
            List<DateTime> dateTimeList = CalendarUtils.getFullWeeks(i, year, 1, true);
            for (DateTime item :dateTimeList) {
                dateList.add(new MonthDayItem(item,false));

            }
            String id = String.format("%d-%d", i, year);
            calendarMonthItemList.add(new CalenderMonthMultiSelectItem(id, year, i, dateList));
        }

        return calendarMonthItemList;
    }

}
