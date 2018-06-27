package com.nanda.calendarSample.data.mapper;

import android.text.TextUtils;

import com.nanda.calendarSample.data.entity.CalendarMonthItem;
import com.nanda.calendarSample.data.entity.CalenderMonthMultiSelectItem;
import com.nanda.calendarSample.data.entity.MonthDayItem;
import com.nanda.calendarSample.data.entity.MonthItem;
import com.nanda.calendarSample.utils.CalendarUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private static List<CalendarMonthItem> getPreviousYearList(int year) {
        List<CalendarMonthItem> calendarMonthItemList = new ArrayList<>();
        String[] monthNameArray = DateFormatSymbols.getInstance().getMonths();
        for (int i = 1; i < 13; i++) {
            String monthName = monthNameArray[i - 1];
            List<DateTime> dateTimeList = CalendarUtils.getFullWeeks(i, year, 1, true);
            String id = String.format(Locale.getDefault(), "%s-%d", i < 10 ? "0" + i : "" + i, year);
            calendarMonthItemList.add(new CalendarMonthItem(id, year, i, dateTimeList, monthName + "," + year));
        }

        return calendarMonthItemList;
    }

    private static List<CalendarMonthItem> getNextYearList(int year) {
        List<CalendarMonthItem> calendarMonthItemList = new ArrayList<>();
        String[] monthNameArray = DateFormatSymbols.getInstance().getMonths();
        for (int i = 1; i < 13; i++) {
            String monthName = monthNameArray[i - 1];
            List<DateTime> dateTimeList = CalendarUtils.getFullWeeks(i, year, 1, true);
            String id = String.format(Locale.getDefault(), "%s-%d", i < 10 ? "0" + i : "" + i, year);
            calendarMonthItemList.add(new CalendarMonthItem(id, year, i, dateTimeList, monthName + "," + year));
        }

        return calendarMonthItemList;
    }

    private static List<CalendarMonthItem> getCurrentYearList(int year) {
        List<CalendarMonthItem> calendarMonthItemList = new ArrayList<>();
        String[] monthNameArray = DateFormatSymbols.getInstance().getMonths();
        for (int i = 1; i < 13; i++) {
            String monthName = monthNameArray[i - 1];
            List<DateTime> dateTimeList = CalendarUtils.getFullWeeks(i, year, 1, true);
            String id = String.format(Locale.getDefault(), "%s-%d", i < 10 ? "0" + i : "" + i, year);
            calendarMonthItemList.add(new CalendarMonthItem(id, year, i, dateTimeList, monthName + "," + year));
        }

        return calendarMonthItemList;
    }

    public static List<CalendarMonthItem> convertModelToMonthEntityList(int year) {

        List<CalendarMonthItem> prevYearList = getPreviousYearList(year - 1);
        List<CalendarMonthItem> currentYearList = getCurrentYearList(year);
        List<CalendarMonthItem> nextYearList = getNextYearList(year + 1);

        List<CalendarMonthItem> calendarMonthItemList = new ArrayList<>();
        if (prevYearList != null) {
            calendarMonthItemList.addAll(prevYearList);
        }
        if (currentYearList != null) {
            calendarMonthItemList.addAll(currentYearList);
        }
        if (nextYearList != null) {
            calendarMonthItemList.addAll(nextYearList);
        }
        return calendarMonthItemList;
    }

    public static List<CalenderMonthMultiSelectItem> convertModelToMonthMultiSelectEntityList(int year) {
        List<CalenderMonthMultiSelectItem> calendarMonthItemList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            List<MonthDayItem> dateList = new ArrayList<>();
            List<DateTime> dateTimeList = CalendarUtils.getFullWeeks(i, year, 1, true);
            for (DateTime item : dateTimeList) {
                dateList.add(new MonthDayItem(item, false));

            }
            String id = String.format("%d-%d", i, year);
            calendarMonthItemList.add(new CalenderMonthMultiSelectItem(id, year, i, dateList));
        }

        return calendarMonthItemList;
    }

    public static int getCurrentMonthPosition(String currentMonthId, List<CalendarMonthItem> calendarMonthItemList) {
        if (calendarMonthItemList == null)
            return -1;

        for (int i = 0, calendarMonthItemListSize = calendarMonthItemList.size(); i < calendarMonthItemListSize; i++) {
            CalendarMonthItem item = calendarMonthItemList.get(i);
            if (item != null && !TextUtils.isEmpty(item.getId()) && item.getId().equals(currentMonthId)) {
                return i;
            }
        }

        return -1;
    }
}
