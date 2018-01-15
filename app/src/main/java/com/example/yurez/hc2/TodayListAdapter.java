package com.example.yurez.hc2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TodayListAdapter extends BaseAdapter
{
    private LayoutInflater lInflater;
    private ArrayList<SimpleMedItem> aMeds;

    TodayListAdapter(Context context, ArrayList<SimpleMedItem> meds)
    {
        aMeds = meds;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return aMeds.size();
    }

    @Override
    public Object getItem(int i)
    {
        return aMeds.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    public SimpleMedItem getMed(int i)
    {
        return ((SimpleMedItem) getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View rView = view;
        if (rView == null)
            rView = lInflater.inflate(R.layout.item_today, viewGroup, false);
        SimpleMedItem med = getMed(i);
        ((TextView) rView.findViewById(R.id.item_today_name)).setText(med.getName());
        ((TextView) rView.findViewById(R.id.item_today_dose)).setText(med.getDoseTyped());
        ((TextView) rView.findViewById(R.id.item_today_time)).setText(med.getTime());
        return rView;
    }
}
