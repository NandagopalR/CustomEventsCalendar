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
import com.nanda.calendarSample.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hirondelle.date4j.DateTime;

/**
 * Created by Nandagopal on 10/27/2017.
 */

public class MonthListAdapter extends RecyclerView.Adapter<MonthListAdapter.MonthViewHolder> {

    private Context context;
    private ArrayList<DateTime> datetimeList;
    private int month;
    private int year;
    private DateTime minDateTime;
    private DateTime maxDateTime;
    private DateTime today;

    public static int
            SUNDAY = 1,
            MONDAY = 2,
            TUESDAY = 3,
            WEDNESDAY = 4,
            THURSDAY = 5,
            FRIDAY = 6,
            SATURDAY = 7;

    private LayoutInflater inflater;
    private int startDayOfWeek = SUNDAY;
    private boolean sixWeeksInCalendar = true;

    /**
     * caldroidData belongs to Caldroid
     */
    private Map<String, Object> caldroidData;
    /**
     * extraData belongs to client
     */
    private Map<String, Object> extraData;

    private DateClickListener dateClickListener;


    public MonthListAdapter(Context context, int month, int year, Map<String, Object> caldroidData,
                            Map<String, Object> extraData, DateClickListener dateClickListener) {
        this.context = context;
        this.month = month;
        this.year = year;
        this.caldroidData = caldroidData;
        this.extraData = extraData;
        this.dateClickListener = dateClickListener;
        inflater = LayoutInflater.from(context);

        populateFromCaldroidData();

    }

    public interface DateClickListener {
        void onDayClicked(DateTime item);
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        DateTime item = datetimeList.get(position);
        holder.bindDataToView(item);
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

        public void bindDataToView(DateTime item) {

            if (item.equals(getToday())) {
                tvDay.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                tvDay.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }

            // Set color of the dates in previous / next month
            if (item.getMonth() != month) {
                tvDay.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            }

            tvDay.setText(String.valueOf(item.getDay()));

            if (caldroidData != null) {
                Map<DateTime, EventsItem> itemMap = (Map<DateTime, EventsItem>) caldroidData.get(AppConstants.EVENTS_ON_DATE);
                String dayString = String.format("%d-%d-%d", item.getYear(), item.getMonth(), item.getDay());
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

            if (position < 0)
                return;

            if (dateClickListener != null) {
                DateTime item = datetimeList.get(position);
                dateClickListener.onDayClicked(item);
            }
        }

    }

    public void setAdapterDateTime(DateTime dateTime) {
        this.month = dateTime.getMonth();
        this.year = dateTime.getYear();
        this.datetimeList = CalendarUtils.getFullWeeks(this.month, this.year,
                SUNDAY, true);
    }

    public Map<String, Object> getCaldroidData() {
        return caldroidData;
    }

    public void setCaldroidData(Map<String, Object> caldroidData) {
        this.caldroidData = caldroidData;

        // Reset parameters
        populateFromCaldroidData();
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, Object> extraData) {
        this.extraData = extraData;
    }

    // GETTERS AND SETTERS
    public ArrayList<DateTime> getDatetimeList() {
        return datetimeList;
    }

    public void updateToday() {
        today = CalendarUtils.convertDateToDateTime(new Date());
    }

    protected DateTime getToday() {
        if (today == null) {
            today = CalendarUtils.convertDateToDateTime(new Date());
        }
        return today;
    }

    /**
     * Retrieve internal parameters from caldroid data
     */
    @SuppressWarnings("unchecked")
    private void populateFromCaldroidData() {

//        if (caldroidData != null) {
//            minDateTime = (DateTime) caldroidData
//                    .get(AppConstants._MIN_DATE_TIME);
//            maxDateTime = (DateTime) caldroidData
//                    .get(AppConstants._MAX_DATE_TIME);
//            startDayOfWeek = (Integer) caldroidData
//                    .get(AppConstants.START_DAY_OF_WEEK);
//            sixWeeksInCalendar = (Boolean) caldroidData
//                    .get(AppConstants.SIX_WEEKS_IN_CALENDAR);
//        }

        this.datetimeList = CalendarUtils.getFullWeeks(this.month, this.year,
                SUNDAY, true);
    }

}
