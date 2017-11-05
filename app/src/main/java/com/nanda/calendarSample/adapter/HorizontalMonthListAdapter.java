package com.nanda.calendarSample.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanda.calendarSample.R;
import com.nanda.calendarSample.utils.DeviceUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nandagopal on 11/2/2017.
 */

public class HorizontalMonthListAdapter extends RecyclerView.Adapter<HorizontalMonthListAdapter.MonthViewHolder> {

    private static final int NUMBERS_OF_ITEM_TO_DISPLAY = 7;
    private Context context;
    private LayoutInflater inflater;
    private LinkedList<Date> dateList;
    private ClickManager clickManager;
    private int mSelectedItem = -1;

    public HorizontalMonthListAdapter(Context context, ClickManager clickManager) {
        this.context = context;
        this.clickManager = clickManager;
        inflater = LayoutInflater.from(context);
        dateList = new LinkedList<>();
    }

    public interface ClickManager {
        void onDateClicked(Date item, int position);
    }

    public void setDateList(List<Date> itemList) {
        if (itemList == null) {
            return;
        }
        dateList.clear();
        dateList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month_list, parent, false);
        /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        view.getLayoutParams().width = (int) (DeviceUtils.getScreenWidth(context) / NUMBERS_OF_ITEM_TO_DISPLAY);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        Date item = dateList.get(position);
        holder.bindDataToView(item, position);
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public int getItemPosition(Date item) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (dateList != null && dateList.size() > 0) {
            for (int i = 0, monthListSize = dateList.size(); i < monthListSize; i++) {
                Date dateItem = dateList.get(i);
                if (item != null && dateItem != null && dateFormat.format(dateItem).equals(dateFormat.format(item))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setSelectCurrentDate(int position) {
        if (clickManager != null) {
            clickManager.onDateClicked(dateList.get(position), position);
        }

        mSelectedItem = position;
        notifyItemRangeChanged(0, dateList.size());
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_day)
        TextView tvDay;

        private Date item;

        public MonthViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindDataToView(Date item, int position) {
            this.item = item;
            DateFormat dateFormat = new SimpleDateFormat("dd");
            tvDay.setText(dateFormat.format(item));

            if (position == mSelectedItem) {
                tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvDay.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvDay.setTextColor(ContextCompat.getColor(context, R.color.blue));
            }

        }

        @OnClick(R.id.tv_day)
        public void onViewClicked() {

            int position = getAdapterPosition();
            if (position < 0)
                return;
            if (clickManager != null) {
                clickManager.onDateClicked(item, position);
            }

            mSelectedItem = position;
            notifyItemRangeChanged(0, dateList.size());
        }

    }

}
