package com.nanda.calendarSample.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nanda.calendarSample.activity.monthpager.MonthFragment;
import com.nanda.calendarSample.data.entity.MonthItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nandagopal on 11/6/2017.
 */

public class MonthTestAdapter extends FragmentStatePagerAdapter {

    private List<MonthItem> dateTimeList;

    public MonthTestAdapter(FragmentManager fm) {
        super(fm);
        dateTimeList = new ArrayList<>();
    }

    @Override
    public float getPageWidth(int position) {
        return position > dateTimeList.size() - 2 ? 0.9f : 0.85f;
    }

    public void setDateTimeList(List<MonthItem> itemList) {
        if (itemList == null) {
            return;
        }
        dateTimeList.clear();
        dateTimeList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public int getMonthPosition(MonthItem item) {
        for (int i = 0, dateTimeListSize = dateTimeList.size(); i < dateTimeListSize; i++) {
            MonthItem monthItem = dateTimeList.get(i);
            if (monthItem.getMonthName().equals(item.getMonthName())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Fragment getItem(int position) {
        MonthItem item = dateTimeList.get(position);
        MonthFragment monthFragment = new MonthFragment();
        Bundle bundle = new Bundle();
        bundle.putString("DateTime_Month", item.getMonthName());
        bundle.putInt("DateTime_Year", item.getYear());
        monthFragment.setArguments(bundle);
        return monthFragment;
    }

    @Override
    public int getCount() {
        return dateTimeList.size();
    }
}

