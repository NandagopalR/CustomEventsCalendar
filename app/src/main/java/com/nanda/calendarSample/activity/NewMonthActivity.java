package com.nanda.calendarSample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.adapter.CalendarMonthPagerAdapter;
import com.nanda.calendarSample.base.BaseActivity;
import com.nanda.calendarSample.data.entity.CalendarMonthItem;
import com.nanda.calendarSample.data.mapper.MonthMapper;
import com.nanda.calendarSample.helper.SwipeListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMonthActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = MonthNameActivity.class.getCanonicalName();
    @BindView(R.id.viewpager)
    ViewPager monthPager;
    @BindView(R.id.tv_current_month_name)
    TextView tvCurrentMonthName;
    @BindView(R.id.tv_next_month_name)
    TextView tvNextMonthName;
    @BindView(R.id.layout_month_view)
    RelativeLayout layoutMonthView;

    private List<CalendarMonthItem> calendarMonthItemList;
    private CalendarMonthPagerAdapter pagerAdapter;
    private int curYear;
    private int curMonth;
    private DateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
    private DateFormat selectedDateFormat = new SimpleDateFormat("MMMM,yyyy");
    private int currentMonthPosition;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, NewMonthActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_name);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        calendarMonthItemList = MonthMapper.convertModelToMonthEntityList(curYear);
        String currentMonthId = dateFormat.format(new Date());

        currentMonthPosition = MonthMapper.getCurrentMonthPosition(currentMonthId, calendarMonthItemList);

        pagerAdapter = new CalendarMonthPagerAdapter(getSupportFragmentManager());
        monthPager.setAdapter(pagerAdapter);
        pagerAdapter.setCalendarMonthItemList(calendarMonthItemList);

        if (currentMonthPosition >= 0) {
            monthPager.setCurrentItem(currentMonthPosition);
            updateUi(monthPager.getCurrentItem());
        }

        monthPager.addOnPageChangeListener(this);
        layoutMonthView.setOnTouchListener(new SwipeListener(this) {
            @Override
            public void onSwipeToNextMonth() {
                super.onSwipeToNextMonth();
                if (monthPager != null) {
                    monthPager.setCurrentItem(monthPager.getCurrentItem() + 1);
                }
            }

            @Override
            public void onSwipeToPrevMonth() {
                super.onSwipeToPrevMonth();
                if (monthPager != null) {
                    monthPager.setCurrentItem(monthPager.getCurrentItem() - 1);
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateUi(position);
    }

    private void updateUi(int position) {
        if (calendarMonthItemList != null && calendarMonthItemList.size() > 0) {
            String currentMonth = calendarMonthItemList.get(position).getMonthYearName();
            String nextMonth;
            if (position != (calendarMonthItemList.size() - 1)) {
                nextMonth = calendarMonthItemList.get(position + 1).getMonthYearName();
                if (!TextUtils.isEmpty(nextMonth)) {
                    tvNextMonthName.setText(nextMonth);
                }
            } else tvNextMonthName.setText("");
            if (!TextUtils.isEmpty(currentMonth)) {
                tvCurrentMonthName.setText(currentMonth);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
