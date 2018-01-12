package com.example.yurez.hc2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.Collections;

import static android.support.design.widget.FloatingActionButton.*;

public class MainActivity extends AppCompatActivity implements
        AllMedsFragment.OnFragmentInteractionListener,
        TodayMedsFragment.OnFragmentInteractionListener, AlarmReceiver.onNotifyReceived
{
    final public static String TAG_MED_INDEX = "med_index";
    final public static String TAG_MED_INDEX_SIMPLE = "med_index_simple";
    final public static String TAG_TIME_CODE = "med_time_code";
    final public static int RESULT_ACCEPT = 100;
    final public static int RESULT_IGNORE = 200;

    int countTimerID;
    Intent intentAdd;
    FragmentManager fragmentManager;
    AllMedsFragment allMedsFragment;
    HistoryFragment historyFragment;
    TodayMedsFragment todayMedsFragment;

    AlarmReceiver alarmReceiver;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            selectFragment(item);
            return true;
        }
    };


    //TODO:Fragments. Selector here
    private void selectFragment(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.navigation_history:
                changeFragment(historyFragment);
                historyFragment.refreshHistoryList();
                break;
            case R.id.navigation_today:
                changeFragment(todayMedsFragment);
                break;
            case R.id.navigation_allMeds:
                changeFragment(allMedsFragment);
                allMedsFragment.refreshAllMedsList();
                break;
        }
    }

    public void changeFragment(Fragment fragment)
    {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerLayout, fragment) // id вашего FrameLayout
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FloatingActionButton medAddBtn = (FloatingActionButton) findViewById(R.id.addMedBtn);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        alarmReceiver = new AlarmReceiver(this);

        fragmentManager = getFragmentManager();
        allMedsFragment = new AllMedsFragment();
        historyFragment = new HistoryFragment();
        todayMedsFragment = new TodayMedsFragment();

        intentAdd = new Intent(getApplicationContext(), AddMedActivity.class);
        medAddBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intentAdd.putExtra(TAG_MED_INDEX_SIMPLE, -1);
                startActivityForResult(intentAdd, 0);
            }
        });
    }

    public void setTimer(Long msTime, int timerID)
    {
        Context context = this.getApplicationContext();
        if (alarmReceiver != null)
        {
            alarmReceiver.setAlarm(context, msTime, timerID);
        }
    }

    public void cancelTimer(int timerID)
    {
        Context context = this.getApplicationContext();
        if (alarmReceiver != null)
        {
            alarmReceiver.cancelAlarm(context, timerID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 0:
            case 1:
                if (resultCode == RESULT_OK) //added or changed
                {
                    int indexMed = data.getIntExtra(TAG_MED_INDEX, -1);
                    if (indexMed < 0)
                        indexMed = MedDataHolder.aAllMeds.size()-1;
                    handleNewMed(indexMed);
                    allMedsFragment.refreshAllMedsList();
                    //TODO: handle new data, refresh today
                }
                break;
            case 2: //accept or ignored or canceled
                if (resultCode == RESULT_ACCEPT)
                    handleAccept();
                else if (resultCode == RESULT_IGNORE)
                    handleIgnore(data.getIntExtra(TAG_MED_INDEX_SIMPLE, 0));
                historyFragment.refreshHistoryList();
        }
    }

    private void handleNewMed(int indexMed)
    {
        Long curDateMs = System.currentTimeMillis();
        Calendar curDateStart = Calendar.getInstance(); // start of current day
        curDateStart.setTimeInMillis(curDateMs);
        curDateStart.set(Calendar.HOUR_OF_DAY, 0);
        curDateStart.set(Calendar.MINUTE, 0);
        curDateStart.set(Calendar.MILLISECOND, 1);

        Calendar curDateEnd = Calendar.getInstance(); // end of current day
        curDateEnd.setTimeInMillis(curDateMs);
        curDateEnd.set(Calendar.HOUR_OF_DAY, 23);
        curDateEnd.set(Calendar.MINUTE, 59);

        Calendar curDate = Calendar.getInstance(); //true current time
        curDate.setTimeInMillis(curDateMs);

        Integer curH = curDate.get(Calendar.HOUR_OF_DAY);
        Integer curM = curDate.get(Calendar.MINUTE);

        DoseTime compDoseTime = new DoseTime(curH, curM, 0f); // contains current HH:mm

        Calendar medDate = Calendar.getInstance(); //time from item

        MedInfo item = MedDataHolder.aAllMeds.get(indexMed);
        medDate.setTimeInMillis(item.startDate);
        if ((medDate.compareTo(curDateStart) >= 0) && (medDate.compareTo(curDateEnd) <= 0))
            for (int i = 0; i < item.doseTimes.size(); ++i)
            {
                DoseTime dt = item.doseTimes.get(i);
                if (dt.compareTo(compDoseTime) > 0)
                {
                    SimpleMedItem smi = item.packToSimple(i);
                    medDate.set(Calendar.HOUR_OF_DAY, dt.getHour());
                    medDate.set(Calendar.MINUTE, dt.getMin());
                    smi.setDateMs(medDate.getTimeInMillis());
                    smi.setParentID(indexMed);
                    smi.setTimerID(++countTimerID);
                    MedDataHolder.aTodayMeds.add(smi);
                }
            }
        Collections.sort(MedDataHolder.aTodayMeds);
        todayMedsFragment.refreshTodayMedsList();
    }

    private void handleIgnore(int indexTodayList) //TODO: Change
    {
        deleteCurrentTimers();
        MedDataHolder.aTodayMeds.remove(indexTodayList);
        setCurrentTimers();
    }

    void handleAccept()
    {
    }

    @Override
    public void onFragmentAllMedsListItemClicked(Integer indexAllMeds) // open edit of med
    {
        intentAdd.putExtra(TAG_MED_INDEX_SIMPLE, indexAllMeds);
        startActivityForResult(intentAdd, 1);
    }

    @Override
    public void onFragmentTodayMedsListItemClicked(Integer indexTodayMeds) // open accept form
    {
        startAcceptForm(indexTodayMeds);
    }

    void startAcceptForm(Integer indexTodayMeds)
    {
        //TODO: send some data
        Intent intent = new Intent(getApplicationContext(), AcceptActivity.class);
        //intent.putExtra(TAG_MED_INFO, MedDataHolder.aTodayMeds.get(index));
        intent.putExtra(TAG_MED_INDEX_SIMPLE, indexTodayMeds);
        startActivityForResult(intent, 2);
    }

    void rebuildTodayList()
    {
        MedDataHolder.aTodayMeds.clear();
        countTimerID = 0;
        Long curDateMs = System.currentTimeMillis();
        Calendar curDateStart = Calendar.getInstance(); // start of current day
        curDateStart.setTimeInMillis(curDateMs);
        curDateStart.set(Calendar.HOUR_OF_DAY, 0);
        curDateStart.set(Calendar.MINUTE, 0);
        curDateStart.set(Calendar.MILLISECOND, 1);

        Calendar curDateEnd = Calendar.getInstance(); // end of current day
        curDateEnd.setTimeInMillis(curDateMs);
        curDateEnd.set(Calendar.HOUR_OF_DAY, 23);
        curDateEnd.set(Calendar.MINUTE, 59);

        Calendar curDate = Calendar.getInstance(); //true current time
        curDate.setTimeInMillis(curDateMs);

        Integer curH = curDate.get(Calendar.HOUR_OF_DAY);
        Integer curM = curDate.get(Calendar.MINUTE);
        DoseTime compDoseTime = new DoseTime(curH, curM, 0f); // contains current HH:mm

        Calendar medDate = Calendar.getInstance(); //time from item

        for (int parentID = 0; parentID < MedDataHolder.aAllMeds.size(); ++parentID)
        {
            MedInfo item = MedDataHolder.aAllMeds.get(parentID);
            medDate.setTimeInMillis(item.startDate);
            if ((medDate.compareTo(curDateStart) >= 0) && (medDate.compareTo(curDateEnd) <= 0))
                for (int i = 0; i < item.doseTimes.size(); ++i)
                {
                    DoseTime dt = item.doseTimes.get(i);
                    if (dt.compareTo(compDoseTime) > 0)
                    {
                        SimpleMedItem smi = item.packToSimple(i);
                        medDate.set(Calendar.HOUR_OF_DAY, dt.getHour());
                        medDate.set(Calendar.MINUTE, dt.getMin());
                        smi.setDateMs(medDate.getTimeInMillis());
                        smi.setParentID(parentID);
                        smi.setTimerID(++countTimerID);
                        MedDataHolder.aTodayMeds.add(smi);
                    }
                }
        }
        Collections.sort(MedDataHolder.aTodayMeds);
        todayMedsFragment.refreshTodayMedsList();
    }

    private void deleteCurrentTimers()
    {
        for (int i = 0; i < MedDataHolder.aTodayMeds.size(); ++i)
            cancelTimer(MedDataHolder.aTodayMeds.get(i).getTimerID());
    }

    private void setCurrentTimers()
    {
        for (int i = 0; i < MedDataHolder.aTodayMeds.size(); ++i)
        {
            SimpleMedItem simpleMedItem = MedDataHolder.aTodayMeds.get(i);
            setTimer(simpleMedItem.getDateMs(), simpleMedItem.getTimerID());
        }
    }

    @Deprecated
    void resetTodayTimers()
    {
       deleteCurrentTimers();
       setCurrentTimers();
    }

    @Override
    public void onAlarm(Integer indexTodayMeds)
    {
        startAcceptForm(indexTodayMeds);
    }
}
