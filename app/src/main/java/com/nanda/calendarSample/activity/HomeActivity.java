package com.nanda.calendarSample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nandagopal on 11/2/2017.
 */

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.month_calendar_vertical, R.id.month_calendar_horizontal, R.id.month_name_calendar_horizontal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.month_calendar_vertical:
                startActivity(VerticalMonthActivity.getCallingIntent(this));
                break;
            case R.id.month_calendar_horizontal:
                startActivity(HorizontalMonthActivity.getCallingIntent(this));
                break;
            case R.id.month_name_calendar_horizontal:
                startActivity(MonthNameActivity.getCallingIntent(this));
                break;
        }
    }
}
