package com.example.yurez.hc2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlanPickerAdapter extends BaseAdapter
{
    private LayoutInflater lInflater;
    private ArrayList<String> planList;

    PlanPickerAdapter(Context context, ArrayList<String> arrayList)
    {
        planList = arrayList;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return planList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return planList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View rView = view;
        if (rView == null)
            rView = lInflater.inflate(R.layout.item_spinner, viewGroup, false);
        String str = (String) getItem(i);
        ((TextView) rView.findViewById(R.id.item_planPickerText)).setText(str);
        return rView;
    }
}
