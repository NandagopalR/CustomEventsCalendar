package com.nanda.calendarSample.activity.monthpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanda.calendarSample.R;

public class MonthFragment extends Fragment {

    private TextView tvMonth;
    private String monthValue;
    private int yearValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            monthValue = getArguments().getString("DateTime_Month");
            yearValue = getArguments().getInt("DateTime_Year");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvMonth = view.findViewById(R.id.tv_month_name);
        tvMonth.setText(monthValue);
    }
}
