package com.nanda.calendarSample.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nanda.calendarSample.app.AppConstants;
import com.nanda.calendarSample.fragments.MonthFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nandagopal on 10/27/2017.
 */

public class MonthPagerAdapter extends FragmentPagerAdapter {

    private List<MonthFragment> monthFragmentList;

    public MonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setMonthFragment(List<MonthFragment> fragmentList) {
        if (fragmentList == null) {
            return;
        }
        this.monthFragmentList = fragmentList;
    }

    public List<MonthFragment> getMonthFragmentList() {

        if (monthFragmentList == null) {
            monthFragmentList = new ArrayList<MonthFragment>();
            for (int i = 0; i < getCount(); i++) {
                monthFragmentList.add(new MonthFragment());
            }
        }
        return monthFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return monthFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return AppConstants.PAGER_PAGES;
    }
}
