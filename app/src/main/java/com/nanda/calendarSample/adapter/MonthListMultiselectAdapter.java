package com.nanda.calendarSample.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.app.AppConstants;
import com.nanda.calendarSample.data.entity.EventsItem;
import com.nanda.calendarSample.data.entity.MonthDayItem;
import com.nanda.calendarSample.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hirondelle.date4j.DateTime;

/**
 * Created by HP-PC on 02-02-2018.
 */

public class MonthListMultiselectAdapter extends RecyclerView.Adapter<MonthListMultiselectAdapter.MonthViewHolder> {

    private Context context;
    private ArrayList<MonthDayItem> datetimeList;
    private int month;
    private int year;
    private DateTime today;
    private int mSelectedItem = -1;

    private static int SUNDAY = 1;

    private LayoutInflater inflater;
    /**
     * caldroidData belongs to Caldroid
     */
    private Map<String, Object> caldroidData;


    public MonthListMultiselectAdapter(Context context, int month, int year) {
        this.context = context;
        this.month = month;
        this.year = year;
        this.caldroidData = new HashMap<>();
        inflater = LayoutInflater.from(context);
        datetimeList = new ArrayList<>();
    }

    public void setDatetimeList(List<MonthDayItem> datetimeItemList) {
        if (datetimeItemList == null) {
            return;
        }
        datetimeList.clear();
        datetimeList.addAll(datetimeItemList);
        notifyDataSetChanged();
    }

    public String getSelectedDate() {
        List<String> selectedDates = new ArrayList<>();

        for (MonthDayItem item : datetimeList) {
            if (item.isSelected())
                selectedDates.add(String.format("%d-%d-%d", item.getDateTime().getDay(),
                        item.getDateTime().getMonth(), item.getDateTime().getYear()));
        }

        return selectedDates.toString().replaceAll("\\[", "").replaceAll("\\]", "");
    }


    public void updateToday() {
        today = CalendarUtils.convertDateToDateTime(new Date());
    }

    @Override
    public MonthListMultiselectAdapter.MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month, parent, false);
        return new MonthListMultiselectAdapter.MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthListMultiselectAdapter.MonthViewHolder holder, int position) {
        MonthDayItem item = datetimeList.get(position);
        holder.bindDataToView(item, position);
    }

    @Override
    public int getItemCount() {
        return datetimeList != null ? datetimeList.size() : 0;
    }


    class MonthViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.layout_events)
        LinearLayout layoutEvent;
        @BindView(R.id.layout_holidays)
        LinearLayout layoutHolidays;
        @BindView(R.id.layout_event_holiday)
        RelativeLayout layoutEventHoliday;
        @BindView(R.id.img_event_ptm)
        ImageView imgPtm;
        @BindView(R.id.img_event_field_trip)
        ImageView imgFieldTrip;
        @BindView(R.id.img_event_remainder)
        ImageView imgRemainder;
        @BindView(R.id.img_half_holiday)
        ImageView imgHalfHoliday;
        @BindView(R.id.img_full_holiday)
        ImageView imgFullHoliday;

        public MonthViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindDataToView(MonthDayItem item, int position) {

            tvDay.setBackgroundResource(item.isSelected() ? R.drawable.ic_days_selected : R.drawable.ic_days_unselected);

            if (item.getDateTime().equals(getToday())) {
                tvDay.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                tvDay.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }

            // Set color of the dates in previous / next month
            if (item.getDateTime().getMonth() != month) {
                tvDay.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            }

            tvDay.setText(String.valueOf(item.getDateTime().getDay()));

            if (caldroidData != null) {
                Map<DateTime, EventsItem> itemMap = (Map<DateTime, EventsItem>) caldroidData.get(AppConstants.EVENTS_ON_DATE);
                if (itemMap != null && itemMap.containsKey(item)) {

                    EventsItem eventsItem = itemMap.get(item);
                    if (eventsItem != null) {

                        if (eventsItem.hasHoliday()) {
                            layoutEventHoliday.setVisibility(View.VISIBLE);
                            layoutHolidays.setVisibility(View.VISIBLE);
                            layoutEvent.setVisibility(View.GONE);

                            imgHalfHoliday.setVisibility(eventsItem.isHalfHoliday() ? View.VISIBLE : View.GONE);
                            imgFullHoliday.setVisibility(eventsItem.isFullHoliday() ? View.VISIBLE : View.GONE);

                        } else if (eventsItem.hasEvents()) {
                            layoutEventHoliday.setVisibility(View.VISIBLE);
                            layoutEvent.setVisibility(View.VISIBLE);
                            layoutHolidays.setVisibility(View.GONE);

                            imgPtm.setVisibility(eventsItem.isPtm() ? View.VISIBLE : View.GONE);
                            imgFieldTrip.setVisibility(eventsItem.isFieldTrip() ? View.VISIBLE : View.GONE);
                            imgRemainder.setVisibility(eventsItem.isRemainder() ? View.VISIBLE : View.GONE);

                        } else {
                            layoutEventHoliday.setVisibility(View.INVISIBLE);
                        }

                    } else {
                        layoutEventHoliday.setVisibility(View.INVISIBLE);
                    }

                } else {
                    layoutEventHoliday.setVisibility(View.INVISIBLE);
                }
            } else {
                layoutEventHoliday.setVisibility(View.INVISIBLE);
            }
        }

        @OnClick(R.id.tv_day)
        public void onDateClicked() {

            int position = getAdapterPosition();
//
//            if (position < 0)
//                return;
//
//            if (dateClickListener != null) {
//                DateTime item = datetimeList.get(position);
//                dateClickListener.onDayClicked(item);
//            }
//            mSelectedItem = position;
            MonthDayItem item = datetimeList.get(position);
            if (item.getDateTime().getMonth() != month)
                return;
            if (item.isSelected())
                item.setSelected(false);
            else item.setSelected(true);

            notifyItemRangeChanged(0, datetimeList.size());
        }

    }

    private DateTime getToday() {
        if (today == null) {
            today = CalendarUtils.convertDateToDateTime(new Date());
        }
        return today;
    }

}