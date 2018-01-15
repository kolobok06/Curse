package com.example.yurez.hc2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class AllMedsListAdapter extends BaseAdapter
{
    private LayoutInflater lInflater;
    private ArrayList<MedInfo> aMeds;

    AllMedsListAdapter(Context context, ArrayList<MedInfo> meds)
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

    MedInfo getMed(int i)
    {
        return ((MedInfo) getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View rView = view;
        if (rView == null)
            rView = lInflater.inflate(R.layout.item_all_meds, viewGroup, false);
        MedInfo med = getMed(i);
        ((TextView) rView.findViewById(R.id.item_today_name)).setText(med.name);
        ((TextView) rView.findViewById(R.id.item_adminMethodTitle)).setText(med.adminMethod);
        ((TextView) rView.findViewById(R.id.item_whenToTakeTitle)).setText(med.whenToTake);
        TextView tv = (TextView) rView.findViewById(R.id.item_remAmountTitle);
        if (med.remAmount > 0)
        {
            tv.setVisibility(View.VISIBLE);
            ((TextView) rView.findViewById(R.id.item_remAmountTitle)).setText(String.format(Locale.getDefault(), "Осталось %.2f %s", med.remAmount, med.medType));
        } else
            tv.setVisibility(View.GONE);
        //TODO: change remAmount input
        return rView;
    }
}
