package com.nanda.calendarSample.data.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nandagopal on 10/27/2017.
 */

public class MonthListMapper {

    public static List<String> convertDataToEntityList(int length) {

        List<String> monthList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            monthList.add(String.format("Test Android"));
        }

        return monthList;
    }

}
