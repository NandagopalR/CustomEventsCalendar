package com.nanda.calendarSample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.nanda.calendarSample.R;
import com.nanda.calendarSample.activity.monthpager.MonthAdapter;
import com.nanda.calendarSample.base.BaseActivity;
import com.nanda.calendarSample.data.entity.MonthItem;
import com.nanda.calendarSample.data.mapper.MonthMapper;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompactCalendarActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.compactcalendar_view)
    CompactCalendarView compactCalendarView;

    private MonthAdapter adapter;
    private List<MonthItem> monthList;
    private int curYear;
    private int curMonth;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
    private SimpleDateFormat currentMonthFormatter = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
    private int currentMonthPosition;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CompactCalendarActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compact_calendar);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = (calendar.get(Calendar.MONTH));
        monthList = getCurrentMonths(curYear);

        String curentMonthId = currentMonthFormatter.format(new Date());

        if (!TextUtils.isEmpty(curentMonthId))
            currentMonthPosition = getCurrentMonthPosition(curentMonthId, monthList);


        adapter = new MonthAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        adapter.setDateTimeList(monthList);

        if (currentMonthPosition >= 0)
            pager.setCurrentItem(currentMonthPosition);

        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(true);
        compactCalendarView.invalidate();
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date selectedDate) {

                if (selectedDate != null) {
                    String selectedStringDate = dateFormatForMonth.format(selectedDate);
                    if (!TextUtils.isEmpty(selectedStringDate)) {
                        Toast.makeText(CompactCalendarActivity.this, selectedStringDate, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });
        compactCalendarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pager.onTouchEvent(motionEvent);
                return false;
            }
        });
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                compactCalendarView.onTouchEvent(motionEvent);
                return false;
            }
        });

    }

    private int getCurrentMonthPosition(String id, List<MonthItem> monthList) {
        for (int i = 0, monthListSize = monthList.size(); i < monthListSize; i++) {
            MonthItem item = monthList.get(i);
            if (item != null && !TextUtils.isEmpty(item.getMonthName()) && item.getMonthName().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private List<MonthItem> getCurrentMonths(int year) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        List<MonthItem> prevYearList = MonthMapper.convertModelToEntityList(symbols.getMonths(), year - 1);
        List<MonthItem> currentYearList = MonthMapper.convertModelToEntityList(symbols.getMonths(), year);
        List<MonthItem> nextYearList = MonthMapper.convertModelToEntityList(symbols.getMonths(), year + 1);
        List<MonthItem> monthItemList = new ArrayList<>();
        if (prevYearList != null && prevYearList.size() > 0) {
            monthItemList.addAll(prevYearList);
        }
        if (currentYearList != null && currentYearList.size() > 0) {
            monthItemList.addAll(currentYearList);
        }
        if (nextYearList != null && nextYearList.size() > 0) {
            monthItemList.addAll(nextYearList);
        }
        return monthItemList;
    }

}
