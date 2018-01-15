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
        TodayMedsFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        AlarmReceiver.OnNotifyReceived
{
    final public static String TAG_MED_INDEX = "med_index";
    final public static String TAG_MED_INDEX_SIMPLE = "med_index_simple";
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
                    rebuildAllSchedule();
                    allMedsFragment.refreshAllMedsList();
                    historyFragment.refreshHistoryList();
                }
                break;
            case 2: //accept or ignored or canceled
                if ((resultCode == RESULT_ACCEPT) || (resultCode == RESULT_IGNORE))
                {
                    int indexToday = data.getIntExtra(TAG_MED_INDEX_SIMPLE, 0);
                    removeTimerAndTask(indexToday);
                    historyFragment.refreshHistoryList();
                    todayMedsFragment.refreshTodayMedsList();
                }
        }
    }

    private void rebuildAllSchedule()
    {
        deleteCurrentTimers();
        MedDataHolder.aTodayMeds.clear();
        rebuildSchedule(-100);
    }

    private void rebuildSchedule(int indexMed)
    {
        Long curDateMs = System.currentTimeMillis();
        Calendar medDate = Calendar.getInstance(); //date & time for stamp in item
        medDate.setTimeInMillis(curDateMs); // new hours and minutes will be applied in a loop
        medDate.set(Calendar.MILLISECOND, 0);
        Integer curH = medDate.get(Calendar.HOUR_OF_DAY);
        Integer curM = medDate.get(Calendar.MINUTE);
        DoseTime compTime = new DoseTime(curH, curM, 0f); // contains current HH:mm for compare
        int n;
        int startIndex;
        if (indexMed < 0)
        {
            n = MedDataHolder.aAllMeds.size();
            startIndex = 0;
        } else
        {
            n = indexMed + 1;
            startIndex = indexMed;
        }
        for (int parentID = startIndex; parentID < n; ++parentID)
        {
            MedInfo item = MedDataHolder.aAllMeds.get(parentID);
            if ((item.startDate <= curDateMs) && (curDateMs <= item.finalDate)) // today is in interval
            {
                for (int i = 0; i < item.doseTimes.size(); ++i) // look at med's schedule
                {
                    DoseTime dt = item.doseTimes.get(i); // get one task
                    if (dt.compareTo(compTime) > 0) // if task after this moment
                    {
                        SimpleMedItem smi = item.packToSimple(i);
                        medDate.set(Calendar.HOUR_OF_DAY, dt.getHour());
                        medDate.set(Calendar.MINUTE, dt.getMin());
                        smi.setDateMs(medDate.getTimeInMillis()); // date is today, time from schedule
                        smi.setParentID(parentID);
                        smi.setTimerID(++countTimerID);
                        MedDataHolder.aTodayMeds.add(smi);
                        setTimer(smi.getDateMs(), smi.getTimerID());
                    }
                }
            }
        }
        Collections.sort(MedDataHolder.aTodayMeds);
        todayMedsFragment.refreshTodayMedsList();
    }

    private void removeTimerAndTask(int indexTodayList) //TODO: Change
    {
        cancelTimer(MedDataHolder.aTodayMeds.get(indexTodayList).getTimerID());
        MedDataHolder.aTodayMeds.remove(indexTodayList);
    }

    @Override
    public void onFragmentAllMedsListItemClicked(Integer indexAllMeds) // open edit of med
    {
        intentAdd.putExtra(TAG_MED_INDEX_SIMPLE, indexAllMeds);
        startActivityForResult(intentAdd, 1);
    }

    @Override
    public void onFragmentAllMedsListItemLongClicked(int index)
    {
        MedDataHolder.aAllMeds.remove(index);
        rebuildAllSchedule();
    }

    @Override
    public void onFragmentTodayMedsListItemClicked(Integer indexTodayMeds) // open accept form
    {
        startAcceptForm(indexTodayMeds);
    }

    void startAcceptForm(Integer indexTodayMeds)
    {
        Intent intent = new Intent(getApplicationContext(), AcceptActivity.class);
        intent.putExtra(TAG_MED_INDEX_SIMPLE, indexTodayMeds);
        startActivityForResult(intent, 2);
    }

    private void deleteCurrentTimers()
    {
        for (int i = 0; i < MedDataHolder.aTodayMeds.size(); ++i)
            cancelTimer(MedDataHolder.aTodayMeds.get(i).getTimerID());
        countTimerID = 0;
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
