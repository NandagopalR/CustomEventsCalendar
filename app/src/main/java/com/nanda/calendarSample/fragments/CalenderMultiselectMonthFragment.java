package com.nanda.calendarSample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.adapter.MonthListMultiselectAdapter;
import com.nanda.calendarSample.data.entity.MonthDayItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hirondelle.date4j.DateTime;

/**
 * Created by HP-PC on 02-02-2018.
 */

public class CalenderMultiselectMonthFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.get_dates)
    Button getDatesBtn;

    private MonthListMultiselectAdapter adapter;
    private List<MonthDayItem> dateTimeList;
    private int currentMonth;
    private int currentYear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setDateTimeList(List<DateTime> dateTimeList, int month, int year) {
        this.dateTimeList = new ArrayList<>(dateTimeList.size());
//        this.dateTimeList = dateTimeList;
        for (DateTime item : dateTimeList) {
            this.dateTimeList.add(new MonthDayItem(item, false));
        }
        currentMonth = month;
        currentYear = year;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getDatesBtn.setVisibility(View.VISIBLE);
        adapter = new MonthListMultiselectAdapter(getContext(), currentMonth, currentYear);
        adapter.updateToday();
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerview.setItemAnimator(null);
        recyclerview.setAdapter(adapter);
        if (dateTimeList != null) {
            adapter.setDatetimeList(dateTimeList);
        }
    }


    @OnClick(R.id.get_dates)
    public void onViewClicked() {
        Toast.makeText(getContext(), adapter.getSelectedDate(), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onDayClicked(DateTime item) {
//        String dateString = String.format("%d-%d-%d", item.getDay(), item.getMonth(), item.getYear());
//        Toast.makeText(getContext(), dateString, Toast.LENGTH_SHORT).show();
//    }
}