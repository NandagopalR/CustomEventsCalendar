package com.nanda.calendarSample.helper;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Nandagopal on 11/5/2017.
 */

public class GetDateList extends AsyncTask<String, List<Date>, List<Date>> {

    private Calendar startDate, endDate;
    private DateListResult dateListResult;

    public interface DateListResult {
        void onDateListEmitted(List<Date> dateList);
    }

    public GetDateList(Calendar startDate, Calendar endDate, DateListResult dateListResult) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateListResult = dateListResult;
    }

    @Override
    protected List<Date> doInBackground(String... params) {
        List<Date> dateList = new ArrayList<>();

        while (startDate.compareTo(endDate) < 1) {
            dateList.add(startDate.getTime());
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }


        return dateList;
    }

    @Override
    protected void onPostExecute(List<Date> dates) {
        super.onPostExecute(dates);
        if (dates != null && dates.size() > 0) {
            if (dateListResult != null) {
                dateListResult.onDateListEmitted(dates);
            }
        }
    }
}
