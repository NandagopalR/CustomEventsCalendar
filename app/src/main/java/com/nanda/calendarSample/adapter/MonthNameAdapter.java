package com.nanda.calendarSample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanda.calendarSample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nandagopal on 11/6/2017.
 */

public class MonthNameAdapter extends RecyclerView.Adapter<MonthNameAdapter.MonthViewHolder> {

    private Context context;
    private List<String> monthList;
    private LayoutInflater inflater;

    public MonthNameAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        monthList = new ArrayList<>();
    }

    public void setMonthList(List<String> itemList) {
        if (itemList == null) {
            return;
        }
        monthList.clear();
        monthList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month_name, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        String item = monthList.get(position);
        holder.bindDataToViews(item);
    }

    @Override
    public int getItemCount() {
        return monthList.size();
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_month_name)
        TextView tvMonthName;

        public MonthViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindDataToViews(String item) {
            tvMonthName.setText(item);
        }
    }

}
