package com.nanda.calendarSample.data.mapper;

import com.nanda.calendarSample.data.entity.MonthItem;

import java.util.ArrayList;
import java.util.List;


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

}
