package com.nanda.calendarSample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.activity.monthpager.MonthAdapter;
import com.nanda.calendarSample.adapter.CalendarMonthPagerAdapter;
import com.nanda.calendarSample.base.BaseActivity;
import com.nanda.calendarSample.data.entity.CalendarMonthItem;
import com.nanda.calendarSample.data.entity.MonthItem;
import com.nanda.calendarSample.data.mapper.MonthMapper;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nandagopal on 11/6/2017.
 */

public class MonthNameActivity extends BaseActivity {

    private static final String TAG = MonthNameActivity.class.getCanonicalName();
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.viewpager)
    ViewPager monthPager;

    private List<MonthItem> monthList;
    private List<CalendarMonthItem> calendarMonthItemList;

    private MonthAdapter adapter;
    private CalendarMonthPagerAdapter pagerAdapter;
    private int curYear;
    private int curMonth;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MonthNameActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_name);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = (calendar.get(Calendar.MONTH) + 1);

        adapter = new MonthAdapter(getSupportFragmentManager());
        pagerAdapter = new CalendarMonthPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        monthPager.setAdapter(pagerAdapter);
        monthPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                if (position == 0) {
                    Log.e(TAG, "" + position);
                    int currentYear = calendarMonthItemList.get(position).getYear();
                    List<CalendarMonthItem> prevMonthList = MonthMapper.convertModelToMonthEntityList(currentYear - 1);

                    int prevMonthYear = prevMonthList.get(0).getYear();

                    if (prevMonthYear == currentYear)
                        return;

                    List<CalendarMonthItem> currentMonthList = new ArrayList<>();
                    currentMonthList.addAll(calendarMonthItemList);

                    calendarMonthItemList.clear();
                    calendarMonthItemList.addAll(prevMonthList);
                    calendarMonthItemList.addAll(currentMonthList);
                    pagerAdapter.setCalendarMonthItemList(calendarMonthItemList);
//
                    monthPager.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            monthPager.setCurrentItem(12, false);
                        }
                    }, 200);
                } else if (position > (calendarMonthItemList.size() - 5)) {
                    CalendarMonthItem selectedItem = calendarMonthItemList.get(position);
                    int currentYear = calendarMonthItemList.get(calendarMonthItemList.size() - 5).getYear();
                    List<CalendarMonthItem> nextMonthsList = MonthMapper.convertModelToMonthEntityList(currentYear + 1);

                    int nextMonthYear = nextMonthsList.get(0).getYear();

                    if (nextMonthYear == currentYear)
                        return;

                    calendarMonthItemList.addAll(nextMonthsList);
                    pagerAdapter.setCalendarMonthItemList(calendarMonthItemList);
                    int selectedPosition = pagerAdapter.getCalendarPosition(selectedItem.getId());
                    if (selectedPosition < 0)
                        return;
                    monthPager.setCurrentItem(selectedPosition);
                } else {
                    monthPager.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            pager.setCurrentItem(position, true);
                        }
                    }, 100);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (position == 0) {
                    Log.e(TAG, "" + position);
                    int currentYear = monthList.get(position).getYear();
                    List<MonthItem> prevMonthList = getCurrentMonths(currentYear - 1);

                    int prevMonthYear = prevMonthList.get(0).getYear();

                    if (prevMonthYear == currentYear)
                        return;

                    List<MonthItem> currentMonthList = new ArrayList<MonthItem>();
                    currentMonthList.addAll(monthList);

                    monthList.clear();
                    monthList.addAll(prevMonthList);
                    monthList.addAll(currentMonthList);
                    adapter.setDateTimeList(monthList);
//
                    pager.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            pager.setCurrentItem(12, false);
                        }
                    }, 200);

                } else if (position > monthList.size() - 5) {
                    MonthItem selectedItem = monthList.get(position);
                    int currentYear = monthList.get(monthList.size() - 5).getYear();
                    List<MonthItem> nextMonthsList = getCurrentMonths(currentYear + 1);

                    int nextMonthYear = nextMonthsList.get(0).getYear();

                    if (nextMonthYear == currentYear)
                        return;

                    monthList.addAll(nextMonthsList);
                    adapter.setDateTimeList(monthList);
                    int selectedPosition = adapter.getMonthPosition(selectedItem);
                    if (selectedPosition < 0)
                        return;
                    pager.setCurrentItem(selectedPosition);
                } else {
                    pager.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            monthPager.setCurrentItem(position, true);
                        }
                    }, 100);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        monthList = getCurrentMonths(curYear);
        calendarMonthItemList = MonthMapper.convertModelToMonthEntityList(curYear);
        adapter.setDateTimeList(monthList);
        pagerAdapter.setCalendarMonthItemList(calendarMonthItemList);

        pager.setCurrentItem(curMonth);
        monthPager.setCurrentItem(curMonth);

    }

    public static List<MonthItem> getCurrentMonths(int year) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        List<MonthItem> monthList = MonthMapper.convertModelToEntityList(symbols.getMonths(), year);
        return monthList;
    }
}
