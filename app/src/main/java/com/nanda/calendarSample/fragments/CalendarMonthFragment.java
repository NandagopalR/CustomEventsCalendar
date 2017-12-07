package com.nanda.calendarSample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.adapter.SimpleMonthListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hirondelle.date4j.DateTime;

public class CalendarMonthFragment extends Fragment implements SimpleMonthListAdapter.DateClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private SimpleMonthListAdapter adapter;
    private List<DateTime> dateTimeList;
    private int currentMonth;
    private int currentYear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setDateTimeList(List<DateTime> dateTimeList, int month, int year) {
        this.dateTimeList = dateTimeList;
        currentMonth = month;
        currentYear = year;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        adapter = new SimpleMonthListAdapter(getContext(), currentMonth, currentYear, this);
        adapter.updateToday();
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerview.setItemAnimator(null);
        recyclerview.setAdapter(adapter);
        if (dateTimeList != null) {
            adapter.setDatetimeList(dateTimeList, null);
        }
    }

    @Override
    public void onDayClicked(DateTime item) {
        String dateString = String.format("%d-%d-%d", item.getDay(), item.getMonth(), item.getYear());
        Toast.makeText(getContext(), dateString, Toast.LENGTH_SHORT).show();
    }
}
