package com.nanda.calendarSample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.activity.monthpager.MonthAdapter;
import com.nanda.calendarSample.adapter.CalenderMonthMultiSelectPagerAdapter;
import com.nanda.calendarSample.base.BaseActivity;
import com.nanda.calendarSample.data.entity.CalenderMonthMultiSelectItem;
import com.nanda.calendarSample.data.entity.MonthDayItem;
import com.nanda.calendarSample.data.entity.MonthItem;
import com.nanda.calendarSample.data.mapper.MonthMapper;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP-PC on 02-02-2018.
 */

public class MonthNameMultiselectActivity extends BaseActivity {

    private static final String TAG = MonthNameMultiselectActivity.class.getCanonicalName();
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.viewpager)
    ViewPager monthPager;
    @BindView(R.id.select_date)
    Button selectDate;

    private List<MonthItem> monthList;
    private List<CalenderMonthMultiSelectItem> calendarMonthItemList;

    private MonthAdapter adapter;
    private CalenderMonthMultiSelectPagerAdapter pagerAdapter;
    private int curYear;
    private int curMonth;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MonthNameMultiselectActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_name);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = (calendar.get(Calendar.MONTH) + 1);
        selectDate.setVisibility(View.VISIBLE);
        adapter = new MonthAdapter(getSupportFragmentManager());
        pagerAdapter = new CalenderMonthMultiSelectPagerAdapter(getSupportFragmentManager());
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
                    List<CalenderMonthMultiSelectItem> prevMonthList = MonthMapper.convertModelToMonthMultiSelectEntityList
                            (currentYear - 1);

                    int prevMonthYear = prevMonthList.get(0).getYear();

                    if (prevMonthYear == currentYear)
                        return;

                    List<CalenderMonthMultiSelectItem> currentMonthList = new ArrayList<>();
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
                    CalenderMonthMultiSelectItem selectedItem = calendarMonthItemList.get(position);
                    int currentYear = calendarMonthItemList.get(calendarMonthItemList.size() - 5).getYear();
                    List<CalenderMonthMultiSelectItem> nextMonthsList = MonthMapper
                            .convertModelToMonthMultiSelectEntityList(currentYear + 1);

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
        calendarMonthItemList = MonthMapper.convertModelToMonthMultiSelectEntityList(curYear);
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

    @OnClick(R.id.select_date)
    public void onViewClicked() {
        List<String> selectedDateList = new ArrayList<>();
        for (CalenderMonthMultiSelectItem montItem : calendarMonthItemList) {
            for (MonthDayItem item : montItem.getDateTimeList()) {
                if (item.isSelected())
                    selectedDateList.add(String.format("%d-%d-%d", item.getDateTime().getDay(),
                            item.getDateTime().getMonth(), item.getDateTime().getYear()));
            }

        }
        Toast.makeText(getApplicationContext(), selectedDateList.toString().replaceAll("\\[", "")
                .replaceAll("\\]", ""), Toast.LENGTH_LONG).show();
    }
}