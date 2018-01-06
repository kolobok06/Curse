package com.example.yurez.hc2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class DoseTimeRecViewAdapter extends RecyclerView.Adapter<DoseTimeRecViewAdapter.ViewHolder> {
    private ArrayList<DoseTime> doseTimeList;

    public DoseTimeRecViewAdapter(ArrayList<DoseTime> doseTimes) {
        doseTimeList = doseTimes;
        //lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dose_time_recview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DoseTime doseTime = doseTimeList.get(position);
        Locale loc = Locale.getDefault();

        holder.doseValue.setText(String.format(loc, "%.2f %s", doseTime.dose, AddMedActivity.medType));
        holder.timeValue.setText(String.format(loc, "%02d:%02d", doseTime.hour, doseTime.min));
    }

    @Override
    public int getItemCount() {
        return doseTimeList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView doseValue;
        TextView timeValue;

        public ViewHolder(View itemView) {
            super(itemView);
            doseValue = (TextView) itemView.findViewById(R.id.item_doseValue);
            timeValue = (TextView) itemView.findViewById(R.id.item_timeValue);
        }
    }
}
