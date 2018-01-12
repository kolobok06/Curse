package com.example.yurez.hc2;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class SimpleMedItem implements Comparable<Object>
{
    private String name;
    private String medType;
    private Long dateMs;
    private DoseTime doseTime;
    private Boolean ignored;
    private int parentID;
    private int timerID;


    public SimpleMedItem(String name, String medType, Long dateMs, DoseTime doseTime, Boolean ignored, int parentID)
    {
        this.name = name;
        this.medType = medType;
        this.dateMs = dateMs;
        this.doseTime = doseTime;
        this.ignored = ignored;
        this.parentID = parentID;
    }

    public SimpleMedItem(SimpleMedItem smi)
    {
        this.name = smi.name;
        this.medType = smi.medType;
        this.dateMs = smi.dateMs;
        this.doseTime = new DoseTime(smi.doseTime);
        this.ignored = smi.ignored;
        this.parentID = smi.parentID;
        this.timerID = smi.timerID;
    }

    public void setDoseTime(DoseTime doseTime)
    {
        this.doseTime = doseTime;
        generateDateMs();
    }

    public void setDateMs(Long dateMs)
    {
        this.dateMs = dateMs;
    }

    public void generateDateMs()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMs);
        calendar.set(Calendar.HOUR_OF_DAY, doseTime.getHour());
        calendar.set(Calendar.MINUTE, doseTime.getMin());
        dateMs = calendar.getTimeInMillis();
    }

    public Long getDateMs()
    {
        return dateMs;
    }

    public void setParentID(int parentID)
    {
        this.parentID = parentID;
    }

    public int getParentID()
    {
        return parentID;
    }

    public void setTimerID(int timerID)
    {
        this.timerID = timerID;
    }

    public int getTimerID()
    {
        return timerID;
    }

    public String getDose()
    {
        return doseTime.getDoseString();
    }

    public String getDoseTyped()
    {
        return (doseTime.getDoseString() + " " + medType);
    }

    public String getTime()
    {
        return ignored ? "--:--" : doseTime.getTimeString();
    }

    public String getName()
    {
        return name;
    }

    public void setIgnored(Boolean ignored)
    {
        this.ignored = ignored;
    }

    public Boolean isIgnored()
    {
        return ignored;
    }

    public String getDate()
    {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(new Date(dateMs));
    }

    public int getHour()
    {
       return doseTime.getHour();
    }

    public int getMin()
    {
        return doseTime.getMin();
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        SimpleMedItem obj = (SimpleMedItem) o;
        int res = this.dateMs.compareTo(obj.dateMs);
        if (res == 0)
            res = this.doseTime.compareTo(obj.doseTime);
        return res;
    }
}
