package com.nanda.calendarSample.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nanda.calendarSample.data.entity.CalendarMonthItem;
import com.nanda.calendarSample.fragments.CalendarMonthFragment;
import com.nanda.calendarSample.fragments.CalenderMultiselectMonthFragment;

import java.util.ArrayList;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * Created by HP-PC on 02-02-2018.
 */

public class CalenderMonthMultiSelectPagerAdapter extends FragmentPagerAdapter {

    private List<CalendarMonthItem> calendarMonthItemList;

    public CalenderMonthMultiSelectPagerAdapter(FragmentManager fm) {
        super(fm);
        calendarMonthItemList = new ArrayList<>();
    }

    public void setCalendarMonthItemList(List<CalendarMonthItem> itemList) {
        if (itemList == null) {
            return;
        }
        calendarMonthItemList.clear();
        calendarMonthItemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public int getCalendarPosition(String id) {
        if (id == null || id.isEmpty()) {
            return -1;
        }

        for (int i = 0, calendarMonthItemListSize = calendarMonthItemList.size(); i < calendarMonthItemListSize; i++) {
            CalendarMonthItem item = calendarMonthItemList.get(i);
            if (item.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Fragment getItem(int position) {
        CalendarMonthItem item = calendarMonthItemList.get(position);
        List<DateTime> dateTimeList = item.getDateTimeList();
        int month = item.getMonth();
        int year = item.getYear();
        CalenderMultiselectMonthFragment fragment = new CalenderMultiselectMonthFragment();
        fragment.setDateTimeList(dateTimeList, month, year);
        return fragment;
    }

    @Override
    public int getCount() {
        return calendarMonthItemList.size();
    }
}