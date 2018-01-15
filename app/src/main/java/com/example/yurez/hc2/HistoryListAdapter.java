package com.example.yurez.hc2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class HistoryListAdapter extends BaseAdapter
{
    private LayoutInflater lInflater;
    private ArrayList<SimpleMedItem> aHistory;

    HistoryListAdapter(Context context, ArrayList<SimpleMedItem> historyItems)
    {
        aHistory = historyItems;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return aHistory.size();
    }

    @Override
    public Object getItem(int i)
    {
        return aHistory.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    public SimpleMedItem getHistoryItem(int i)
    {
        return ((SimpleMedItem) getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View rView = view;
        if (rView == null)
            rView = lInflater.inflate(R.layout.item_history, viewGroup, false);
        SimpleMedItem hItem = getHistoryItem(i);
        ((TextView) rView.findViewById(R.id.item_hist_name)).setText(hItem.getName());
        ((TextView) rView.findViewById(R.id.item_hist_time)).setText(hItem.getTime());
        ((TextView) rView.findViewById(R.id.item_hist_date)).setText(hItem.getDate());
        ((TextView) rView.findViewById(R.id.item_hist_dose)).setText(hItem.getDoseTyped());
        return rView;
    }
}
