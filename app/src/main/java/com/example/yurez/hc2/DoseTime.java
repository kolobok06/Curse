package com.example.yurez.hc2;

import android.support.annotation.NonNull;

import java.util.Locale;

public class DoseTime implements Comparable<Object>
{
    private Integer hour, min;
    private Float dose;

    public DoseTime(Integer hour, Integer min, Float dose)
    {
        this.hour = hour;
        this.min = min;
        this.dose = dose;
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        DoseTime obj = (DoseTime) o;
        int res = this.hour.compareTo(obj.hour);
        if (res == 0)
        {
            res = this.min.compareTo(obj.min);
        }
        return res;
    }

    public String getTimeString()
    {
        return String.format(Locale.getDefault(), "%02d:%02d", this.hour, this.min);
    }

    public String getDoseString()
    {
        return String.format(Locale.getDefault(), "%.2f", this.dose);
    }
}
