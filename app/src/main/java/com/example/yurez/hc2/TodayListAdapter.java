package com.example.yurez.hc2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TodayListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<MedInfo> aMeds;

    TodayListAdapter(Context context, ArrayList<MedInfo> meds) {
        ctx = context;
        aMeds = meds;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return aMeds.size();
    }

    @Override
    public Object getItem(int i) {
        return aMeds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    MedInfo getMed(int i) {
        return ((MedInfo) getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rView = view;
        if (rView == null) {
            rView = lInflater.inflate(R.layout.item_today_list, viewGroup, false);
        }
        MedInfo med = getMed(i);
        ((TextView) rView.findViewById(R.id.item_medNameTitle)).setText(med.name);
        ((TextView) rView.findViewById(R.id.item_adminMethodTitle)).setText(med.adminMethod);
        ((TextView) rView.findViewById(R.id.item_whenToTakeTitle)).setText(med.whenToTake);
        ((TextView) rView.findViewById(R.id.item_dozeTitle)).setText(String.format("%.2f %s", med.dose, med.medType));
        //TODO: Add time filling
        return rView;
    }
}
