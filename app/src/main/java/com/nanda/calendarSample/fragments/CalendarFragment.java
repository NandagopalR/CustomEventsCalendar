package com.nanda.calendarSample.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.adapter.MonthListAdapter;
import com.nanda.calendarSample.adapter.MonthPagerAdapter;
import com.nanda.calendarSample.app.AppConstants;
import com.nanda.calendarSample.data.entity.EventsItem;
import com.nanda.calendarSample.helper.InfinitePagerAdapter;
import com.nanda.calendarSample.helper.InfiniteViewPager;
import com.nanda.calendarSample.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import hirondelle.date4j.DateTime;

import static com.nanda.calendarSample.app.AppConstants.DISABLE_DATES;
import static com.nanda.calendarSample.app.AppConstants.MAX_DATE;
import static com.nanda.calendarSample.app.AppConstants.MIN_DATE;
import static com.nanda.calendarSample.app.AppConstants.MONTH;
import static com.nanda.calendarSample.app.AppConstants.SELECTED_DATES;
import static com.nanda.calendarSample.app.AppConstants.SIX_WEEKS_IN_CALENDAR;
import static com.nanda.calendarSample.app.AppConstants.SQUARE_TEXT_VIEW_CELL;
import static com.nanda.calendarSample.app.AppConstants.START_DAY_OF_WEEK;
import static com.nanda.calendarSample.app.AppConstants.YEAR;
import static com.nanda.calendarSample.app.AppConstants._MAX_DATE_TIME;
import static com.nanda.calendarSample.app.AppConstants._MIN_DATE_TIME;

/**
 * Created by Nandagopal on 10/27/2017.
 */

public class CalendarFragment extends DialogFragment implements MonthListAdapter.DateClickListener {

    @BindView(R.id.infinite_pager)
    InfiniteViewPager infinitePager;

    private MonthPagerAdapter monthPagerAdapter;
    private List<MonthFragment> monthFragmentList;
    private DatePageChangeListener pageChangeListener;

    private int month = -1;
    private int year = -1;

    /**
     * Weekday conventions
     */
    public static int
            SUNDAY = 1,
            MONDAY = 2,
            TUESDAY = 3,
            WEDNESDAY = 4,
            THURSDAY = 5,
            FRIDAY = 6,
            SATURDAY = 7;

    /**
     * First column of calendar is Sunday
     */
    protected int startDayOfWeek = SUNDAY;

    /**
     * A calendar height is not fixed, it may have 5 or 6 rows. Set fitAllMonths
     * to true so that the calendar will always have 6 rows
     */
    private boolean sixWeeksInCalendar = true;

    protected DateTime minDateTime;
    protected DateTime maxDateTime;

    private List<MonthListAdapter> monthListAdapterList = new ArrayList<>();

    protected ArrayList<DateTime> disableDates = new ArrayList<DateTime>();
    protected ArrayList<DateTime> selectedDates = new ArrayList<DateTime>();
    /**
     * caldroidData belongs to Caldroid
     */
    private Map<String, Object> caldroidData = new HashMap<>();
    /**
     * extraData belongs to client
     */
    protected Map<String, Object> extraData = new HashMap<>();
    public Map<DateTime, EventsItem> eventsItemMap = new ArrayMap<>();
    private ArrayList<DateTime> dateInMonthsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveInitialArgs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setupDateGridPages();

        refreshView();
    }


    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    /**
     * Meant to be subclassed. User who wants to provide custom view, need to
     * provide custom adapter here
     */
    public MonthListAdapter getNewDatesGridAdapter(int month, int year) {
        return new MonthListAdapter(getActivity(), month, year, getCaldroidData(), extraData, this);
    }

    @Override
    public void onDayClicked(DateTime item) {
        if (item == null) {
            return;
        }
        String dateString = String.format("%d-%d-%d", item.getDay(), item.getMonth(), item.getYear());
        Toast.makeText(getContext(), "" + dateString, Toast.LENGTH_SHORT).show();
    }

    public Map<DateTime, EventsItem> getEventsItemMap() {
        return eventsItemMap;
    }

    public void setEventsItemMap(Map<DateTime, EventsItem> eventsItemMap) {
        this.eventsItemMap = eventsItemMap;
    }

    /**
     * Set calendar to previous month
     */
    public void prevMonth() {
        infinitePager.setCurrentItem(pageChangeListener.getCurrentPage() - 1);
    }

    /**
     * Set calendar to next month
     */
    public void nextMonth() {
        infinitePager.setCurrentItem(pageChangeListener.getCurrentPage() + 1);
    }

    /**
     * DatePageChangeListener refresh the date grid views when user swipe the
     * calendar
     *
     * @author thomasdao
     */
    public class DatePageChangeListener implements ViewPager.OnPageChangeListener {
        private int currentPage = InfiniteViewPager.OFFSET;
        private DateTime currentDateTime;
        private ArrayList<MonthListAdapter> caldroidGridAdapters;

        /**
         * Return currentPage of the dateViewPager
         *
         * @return
         */
        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        /**
         * Return currentDateTime of the selected page
         *
         * @return
         */
        public DateTime getCurrentDateTime() {
            return currentDateTime;
        }

        public void setCurrentDateTime(DateTime dateTime) {
            this.currentDateTime = dateTime;
            setCalendarDateTime(currentDateTime);
        }

        /**
         * Return 4 adapters
         *
         * @return
         */
        public ArrayList<MonthListAdapter> getCaldroidGridAdapters() {
            return caldroidGridAdapters;
        }

        public void setCaldroidGridAdapters(
                ArrayList<MonthListAdapter> caldroidGridAdapters) {
            this.caldroidGridAdapters = caldroidGridAdapters;
        }

        /**
         * Return virtual next position
         *
         * @param position
         * @return
         */
        private int getNext(int position) {
            return (position + 1) % AppConstants.PAGER_PAGES;
        }

        /**
         * Return virtual previous position
         *
         * @param position
         * @return
         */
        private int getPrevious(int position) {
            return (position + 3) % AppConstants.PAGER_PAGES;
        }

        /**
         * Return virtual current position
         *
         * @param position
         * @return
         */
        public int getCurrent(int position) {
            return position % AppConstants.PAGER_PAGES;
        }

        @Override
        public void onPageScrollStateChanged(int position) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void refreshAdapters(int position) {
            // Get adapters to refresh
            MonthListAdapter currentAdapter = caldroidGridAdapters
                    .get(getCurrent(position));
            MonthListAdapter prevAdapter = caldroidGridAdapters
                    .get(getPrevious(position));
            MonthListAdapter nextAdapter = caldroidGridAdapters
                    .get(getNext(position));

            if (position == currentPage) {
                // Refresh current adapter

                currentAdapter.setAdapterDateTime(currentDateTime);
                currentAdapter.notifyDataSetChanged();

                // Refresh previous adapter
                prevAdapter.setAdapterDateTime(currentDateTime.minus(0, 1, 0,
                        0, 0, 0, 0, DateTime.DayOverflow.LastDay));
                prevAdapter.notifyDataSetChanged();

                // Refresh next adapter
                nextAdapter.setAdapterDateTime(currentDateTime.plus(0, 1, 0, 0,
                        0, 0, 0, DateTime.DayOverflow.LastDay));
                nextAdapter.notifyDataSetChanged();
            }
            // Detect if swipe right or swipe left
            // Swipe right
            else if (position > currentPage) {
                // Update current date time to next month
                currentDateTime = currentDateTime.plus(0, 1, 0, 0, 0, 0, 0,
                        DateTime.DayOverflow.LastDay);

                // Refresh the adapter of next gridview
                nextAdapter.setAdapterDateTime(currentDateTime.plus(0, 1, 0, 0,
                        0, 0, 0, DateTime.DayOverflow.LastDay));
                nextAdapter.notifyDataSetChanged();

            }
            // Swipe left
            else {
                // Update current date time to previous month
                currentDateTime = currentDateTime.minus(0, 1, 0, 0, 0, 0, 0,
                        DateTime.DayOverflow.LastDay);

                // Refresh the adapter of previous gridview
                prevAdapter.setAdapterDateTime(currentDateTime.minus(0, 1, 0,
                        0, 0, 0, 0, DateTime.DayOverflow.LastDay));
                prevAdapter.notifyDataSetChanged();
            }

            // Update current page
            currentPage = position;
        }

        /**
         * Refresh the fragments
         */
        @Override
        public void onPageSelected(int position) {

            // Update all the dates inside current month
            MonthListAdapter currentAdapter = caldroidGridAdapters
                    .get(position % AppConstants.PAGER_PAGES);

            // Refresh dateInMonthsList
            dateInMonthsList.clear();
            dateInMonthsList.addAll(currentAdapter.getDatetimeList());

            final int pagePosition = position;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshAdapters(pagePosition);
                    // Update current date time of the selected page
                    setCalendarDateTime(currentDateTime);
                }
            }, 200);
        }

    }

    public void setCalendarDateTime(DateTime dateTime) {
        month = dateTime.getMonth();
        year = dateTime.getYear();

//        // Notify listener
//        if (caldroidListener != null) {
//            caldroidListener.onChangeMonth(month, year);
//        }

        refreshEventsView();
    }

    public void refreshView() {
        // If month and year is not yet initialized, refreshView doesn't do
        // anything
        if (month == -1 || year == -1) {
            return;
        }

//        refreshMonthTitleTextView();

        // Refresh the date grid views
        for (MonthListAdapter adapter : monthListAdapterList) {
            // Reset caldroid data
            adapter.setCaldroidData(getCaldroidData());

            // Reset extra data
            adapter.setExtraData(extraData);

            // Update today variable
            adapter.updateToday();

            // Refresh view
            adapter.notifyDataSetChanged();
        }
    }

    public void refreshEventsView() {
        if (month == -1 || year == -1) {
            return;
        }

//        refreshMonthTitleTextView();

        // Refresh the date grid views
        for (MonthListAdapter adapter : monthListAdapterList) {
            // Reset caldroid data
            adapter.setCaldroidData(setEvents(getEventsItemMap()));

            // Reset extra data
            adapter.setExtraData(extraData);

            // Update today variable
            adapter.updateToday();

            // Refresh view
            adapter.notifyDataSetChanged();
        }
    }

    public Map<String, Object> setEvents(Map<DateTime, EventsItem> eventsMap) {
        Map<String, Object> eventsItemMap = new HashMap<>();

        eventsItemMap.put(AppConstants.EVENTS_ON_DATE, eventsMap);

        return eventsItemMap;
    }

    /**
     * caldroidData return data belong to Caldroid
     *
     * @return
     */
    public Map<String, Object> getCaldroidData() {
        caldroidData.clear();
        caldroidData.put(DISABLE_DATES, null);
        caldroidData.put(SELECTED_DATES, null);
        caldroidData.put(_MIN_DATE_TIME, minDateTime);
        caldroidData.put(_MAX_DATE_TIME, maxDateTime);
        caldroidData.put(START_DAY_OF_WEEK, startDayOfWeek);
        caldroidData.put(SIX_WEEKS_IN_CALENDAR, sixWeeksInCalendar);

        return caldroidData;
    }

    /**
     * Setup 4 pages contain date grid views. These pages are recycled to use
     * memory efficient
     */
    private void setupDateGridPages() {
        // Get current date time
        DateTime currentDateTime = new DateTime(year, month, 1, 0, 0, 0, 0);

        // Set to pageChangeListener
        pageChangeListener = new DatePageChangeListener();
        pageChangeListener.setCurrentDateTime(currentDateTime);

        // Setup adapters for the grid views
        // Current month
        MonthListAdapter adapter0 = getNewDatesGridAdapter(
                currentDateTime.getMonth(), currentDateTime.getYear());

        // Setup dateInMonthsList
        dateInMonthsList = adapter0.getDatetimeList();

        // Next month
        DateTime nextDateTime = currentDateTime.plus(0, 1, 0, 0, 0, 0, 0,
                DateTime.DayOverflow.LastDay);
        MonthListAdapter adapter1 = getNewDatesGridAdapter(
                nextDateTime.getMonth(), nextDateTime.getYear());

        // Next 2 month
        DateTime next2DateTime = nextDateTime.plus(0, 1, 0, 0, 0, 0, 0,
                DateTime.DayOverflow.LastDay);
        MonthListAdapter adapter2 = getNewDatesGridAdapter(
                next2DateTime.getMonth(), next2DateTime.getYear());

        // Previous month
        DateTime prevDateTime = currentDateTime.minus(0, 1, 0, 0, 0, 0, 0,
                DateTime.DayOverflow.LastDay);
        MonthListAdapter adapter3 = getNewDatesGridAdapter(
                prevDateTime.getMonth(), prevDateTime.getYear());

        // Add to the array of adapters
        monthListAdapterList.add(adapter0);
        monthListAdapterList.add(adapter1);
        monthListAdapterList.add(adapter2);
        monthListAdapterList.add(adapter3);

        // Set adapters to the pageChangeListener so it can refresh the adapter
        // when page change
        pageChangeListener.setCaldroidGridAdapters((ArrayList<MonthListAdapter>) monthListAdapterList);

        // Setup InfiniteViewPager and InfinitePagerAdapter. The
        // InfinitePagerAdapter is responsible
        // for reuse the fragments
//        infinitePager = (InfiniteViewPager) view
//                .findViewById(R.id.months_infinite_pager);

        // Set if viewpager wrap around particular month or all months (6 rows)
        infinitePager.setSixWeeksInCalendar(sixWeeksInCalendar);

        // Set the numberOfDaysInMonth to dateViewPager so it can calculate the
        // height correctly
        infinitePager.setDatesInMonth(dateInMonthsList);

        // MonthPagerAdapter actually provides 4 real fragments. The
        // InfinitePagerAdapter only recycles fragment provided by this
        // MonthPagerAdapter
        final MonthPagerAdapter pagerAdapter = new MonthPagerAdapter(
                getChildFragmentManager());

        // Provide initial data to the fragments, before they are attached to
        // view.
        monthFragmentList = pagerAdapter.getMonthFragmentList();

        for (int i = 0; i < AppConstants.PAGER_PAGES; i++) {
            MonthFragment dateGridFragment = monthFragmentList.get(i);
            MonthListAdapter adapter = monthListAdapterList.get(i);
//            dateGridFragment.setGridViewRes(getGridViewRes());
            dateGridFragment.setGridAdapter(adapter);
//            dateGridFragment.setOnItemClickListener(getDateItemClickListener());
//            dateGridFragment
//                    .setOnItemLongClickListener(getDateItemLongClickListener());
        }

        // Setup InfinitePagerAdapter to wrap around MonthPagerAdapter
        InfinitePagerAdapter infinitePagerAdapter = new InfinitePagerAdapter(
                pagerAdapter);

        // Use the infinitePagerAdapter to provide data for dateViewPager
        infinitePager.setAdapter(infinitePagerAdapter);

        // Setup pageChangeListener
        infinitePager.addOnPageChangeListener(pageChangeListener);
    }

    /**
     * Retrieve initial arguments to the fragment Data can include: month, year,
     * dialogTitle, showNavigationArrows,(String) disableDates, selectedDates,
     * minDate, maxDate, squareTextViewCell
     */
    protected void retrieveInitialArgs() {
        // Get arguments
        Bundle args = getArguments();

        CalendarUtils.setup();

        if (args != null) {
            // Get month, year
            month = args.getInt(MONTH, -1);
            year = args.getInt(YEAR, -1);

            // Get start day of Week. Default calendar first column is SUNDAY
            startDayOfWeek = args.getInt(START_DAY_OF_WEEK, 1);
            if (startDayOfWeek > 7) {
                startDayOfWeek = startDayOfWeek % 7;
            }

            // Get sixWeeksInCalendar
            sixWeeksInCalendar = args.getBoolean(SIX_WEEKS_IN_CALENDAR, true);

            // Get min date and max date
            String minDateTimeString = args.getString(MIN_DATE);
            if (minDateTimeString != null) {
                minDateTime = CalendarUtils.getDateTimeFromString(
                        minDateTimeString, null);
            }

            String maxDateTimeString = args.getString(MAX_DATE);
            if (maxDateTimeString != null) {
                maxDateTime = CalendarUtils.getDateTimeFromString(
                        maxDateTimeString, null);
            }

        }
        if (month == -1 || year == -1) {
            DateTime dateTime = DateTime.today(TimeZone.getDefault());
            month = dateTime.getMonth();
            year = dateTime.getYear();
        }
    }


    /**
     * Save current state to bundle outState
     *
     * @param outState
     * @param key
     */
    public void saveStatesToKey(Bundle outState, String key) {
        outState.putBundle(key, getSavedStates());
    }

    /**
     * Get current saved sates of the Caldroid. Useful for handling rotation.
     * It does not need to save state of SQUARE_TEXT_VIEW_CELL because this
     * may change on orientation change
     */
    public Bundle getSavedStates() {
        Bundle bundle = new Bundle();
        bundle.putInt(MONTH, month);
        bundle.putInt(YEAR, year);

        if (selectedDates != null && selectedDates.size() > 0) {
            bundle.putStringArrayList(SELECTED_DATES,
                    CalendarUtils.convertToStringList(selectedDates));
        }

        if (disableDates != null && disableDates.size() > 0) {
            bundle.putStringArrayList(DISABLE_DATES,
                    CalendarUtils.convertToStringList(disableDates));
        }

        if (minDateTime != null) {
            bundle.putString(MIN_DATE, minDateTime.format("YYYY-MM-DD"));
        }

        if (maxDateTime != null) {
            bundle.putString(MAX_DATE, maxDateTime.format("YYYY-MM-DD"));
        }

        bundle.putInt(START_DAY_OF_WEEK, startDayOfWeek);
        bundle.putBoolean(SIX_WEEKS_IN_CALENDAR, sixWeeksInCalendar);

        Bundle args = getArguments();
        if (args != null && args.containsKey(SQUARE_TEXT_VIEW_CELL)) {
            bundle.putBoolean(SQUARE_TEXT_VIEW_CELL, args.getBoolean(SQUARE_TEXT_VIEW_CELL));
        }

        return bundle;
    }


}
