package com.nanda.calendarSample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.app.AppConstants;
import com.nanda.calendarSample.base.BaseActivity;
import com.nanda.calendarSample.data.entity.EventsItem;
import com.nanda.calendarSample.fragments.CalendarFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import hirondelle.date4j.DateTime;

public class VerticalMonthActivity extends BaseActivity {

    private CalendarFragment caldroidFragment;

    private Map<DateTime, EventsItem> eventsItemMap = new HashMap<>();

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, VerticalMonthActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_calendar);
        ButterKnife.bind(this);

        caldroidFragment = new CalendarFragment();

        if (savedInstanceState != null) {
            caldroidFragment.saveStatesToKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }

        eventsItemMap.put(new DateTime(2017, 10, 1, 0, 0, 0, 0), new EventsItem(false, false, false, true, false));
        eventsItemMap.put(new DateTime(2017, 10, 8, 0, 0, 0, 0), new EventsItem(false, false, false, false, true));

        eventsItemMap.put(new DateTime(2017, 10, 12, 0, 0, 0, 0), new EventsItem(true, false, false, false, false));
        eventsItemMap.put(new DateTime(2017, 10, 18, 0, 0, 0, 0), new EventsItem(true, true, false, false, false));
        eventsItemMap.put(new DateTime(2017, 10, 23, 0, 0, 0, 0), new EventsItem(true, true, true, false, false));
        eventsItemMap.put(new DateTime(2017, 10, 29, 0, 0, 0, 0), new EventsItem(false, true, true, false, false));

        eventsItemMap.put(new DateTime(2017, 11, 4, 0, 0, 0, 0), new EventsItem(false, false, false, true, false));
        eventsItemMap.put(new DateTime(2017, 11, 9, 0, 0, 0, 0), new EventsItem(false, false, false, true, false));

        caldroidFragment.setEventsItemMap(eventsItemMap);
        setFragment(caldroidFragment);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                caldroidFragment.refreshEventsView();
            }
        }, 1200);

    }

    private void setFragment(Fragment fragment) {

        Bundle bundle = new Bundle();
        Calendar cal = Calendar.getInstance();
        bundle.putInt(AppConstants.MONTH, cal.get(Calendar.MONTH) + 1);
        bundle.putInt(AppConstants.YEAR, cal.get(Calendar.YEAR));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_content, fragment);
        transaction.commit();
    }

}
