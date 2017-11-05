package com.nanda.calendarSample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.adapter.HorizontalMonthListAdapter;
import com.nanda.calendarSample.base.BaseActivity;
import com.nanda.calendarSample.helper.GetDateList;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nandagopal on 11/2/2017.
 */

public class HorizontalMonthActivity extends BaseActivity implements HorizontalMonthListAdapter.ClickManager {

    @BindView(R.id.tv_month_name)
    TextView tvMonthName;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private int curMonth, curYear;
    private LinearLayoutManager layoutManager;

    private HorizontalMonthListAdapter adapter;
    private List<Date> daysList;
    private DateFormat monthFormat = new SimpleDateFormat("MMMM");

    private int positionAfterScrolled = 0;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, HorizontalMonthActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_calendar);
        ButterKnife.bind(this);

        curMonth = Calendar.getInstance().get(Calendar.MONTH);
        curYear = Calendar.getInstance().get(Calendar.YEAR);

        DateFormatSymbols symbols = new DateFormatSymbols();
        tvMonthName.setText(symbols.getMonths()[curMonth]);

        adapter = new HorizontalMonthListAdapter(this, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(null);
        recyclerview.computeHorizontalScrollOffset();
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                positionAfterScrolled = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                DateFormat dateFormat = new SimpleDateFormat("yyyy");

                if (positionAfterScrolled < 15) {
                    int year = Integer.parseInt(dateFormat.format(daysList.get(positionAfterScrolled).getTime()));
                    int prevYear = year - 1;

                    if (prevYear == year)
                        return;

                    addPreviousYearDaysList(prevYear);
                } else if (lastPosition == daysList.size() - 1) {
                    int year = Integer.parseInt(dateFormat.format(daysList.get(positionAfterScrolled).getTime()));
                    int newYear = year + 1;

                    if (newYear == year)
                        return;
                    Log.e("Selected Position", "" + lastPosition);
                    addNextYearDaysList(newYear);
                }
            }
        });
        recyclerview.setAdapter(adapter);

        Calendar startDate = Calendar.getInstance();
        startDate.set(curYear, Calendar.JANUARY, 1);

        Calendar endDate = Calendar.getInstance();
        endDate.set(curYear, Calendar.DECEMBER, 31);

        new GetDateList(startDate, endDate, new GetDateList.DateListResult() {
            @Override
            public void onDateListEmitted(List<Date> dateList) {
                daysList = dateList;
                if (daysList != null && daysList.size() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    adapter.setDateList(daysList);
                    int currentDatePosition = adapter.getItemPosition(calendar.getTime());

                    if (currentDatePosition < 0)
                        return;
                    Log.e("Position", "" + currentDatePosition);
                    adapter.setSelectCurrentDate(currentDatePosition);
                }
            }
        }).execute();
    }

    private void addPreviousYearDaysList(int yearValue) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(yearValue, Calendar.JANUARY, 1);

        Calendar endDate = Calendar.getInstance();
        endDate.set(yearValue, Calendar.DECEMBER, 31);

        new GetDateList(startDate, endDate, new GetDateList.DateListResult() {
            @Override
            public void onDateListEmitted(List<Date> dateList) {

                if (dateList == null && dateList.size() == 0) {
                    return;
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                String year = dateFormat.format(dateList.get(0));
                int newYear = Integer.parseInt(year);
                int oldYear = Integer.parseInt(dateFormat.format(daysList.get(0)));

                if (newYear == oldYear)
                    return;

                Date selectedDate = daysList.get(0);

                List<Date> prevDateList = new ArrayList<Date>();
                prevDateList.addAll(daysList);

                daysList.clear();
                daysList.addAll(dateList);
                daysList.addAll(prevDateList);
                adapter.setDateList(daysList);
                int currentDatePosition = adapter.getItemPosition(selectedDate);

                if (currentDatePosition < 0)
                    return;
                Log.e("Position", "" + currentDatePosition);
                adapter.setSelectCurrentDate(currentDatePosition);
            }
        }).execute();


    }

    private void addNextYearDaysList(int yearValue) {

        Calendar startDate = Calendar.getInstance();
        startDate.set(yearValue, Calendar.JANUARY, 1);

        Calendar endDate = Calendar.getInstance();
        endDate.set(yearValue, Calendar.DECEMBER, 31);

        new GetDateList(startDate, endDate, new GetDateList.DateListResult() {
            @Override
            public void onDateListEmitted(List<Date> dateList) {
                if (dateList == null && dateList.size() == 0)
                    return;
                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                String year = dateFormat.format(dateList.get(0));
                int newYear = Integer.parseInt(year);
                int oldYear = Integer.parseInt(dateFormat.format(daysList.get(daysList.size() - 1)));

                if (newYear == oldYear)
                    return;

                Date selectedDate = daysList.get(daysList.size() - 1);

                daysList.addAll(dateList);
                adapter.setDateList(daysList);
                int currentDatePosition = adapter.getItemPosition(selectedDate);

                if (currentDatePosition < 0)
                    return;
                Log.e("Position", "" + currentDatePosition);
                adapter.setSelectCurrentDate(currentDatePosition);

            }
        }).execute();

    }

    @Override
    public void onDateClicked(Date item, int position) {
        layoutManager.scrollToPositionWithOffset(position, 0);
        String monthName = monthFormat.format(item);
        tvMonthName.setText(monthName);
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        tvYear.setText(format.format(item));
    }
}
