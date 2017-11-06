package com.nanda.calendarSample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.activity.monthpager.MonthAdapter;
import com.nanda.calendarSample.adapter.MonthTestAdapter;
import com.nanda.calendarSample.base.BaseActivity;
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
    ViewPager viewPager;

    private List<MonthItem> monthList;

    private MonthAdapter adapter;
    private MonthTestAdapter monthTestAdapter;
    private int curYear;

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

        adapter = new MonthAdapter(getSupportFragmentManager());
        monthTestAdapter = new MonthTestAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        viewPager.setAdapter(monthTestAdapter);

        setPageListener();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
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
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        monthList = getCurrentMonths(curYear);
        adapter.setDateTimeList(monthList);
        monthTestAdapter.setDateTimeList(monthList);

    }

    private void setPageListener() {
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.onTouchEvent(event);
                return false;
            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pager.onTouchEvent(event);
                return false;
            }
        });

    }

    public static List<MonthItem> getCurrentMonths(int year) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        List<MonthItem> monthList = MonthMapper.convertModelToEntityList(symbols.getMonths(), year);
        return monthList;
    }

}
