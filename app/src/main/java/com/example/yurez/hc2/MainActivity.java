package com.example.yurez.hc2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import static android.support.design.widget.FloatingActionButton.*;

public class MainActivity extends AppCompatActivity implements AllMedsFragment.OnFragmentInteractionListener, TodayMedsFragment.OnFragmentInteractionListener
{
    final public static String TAG_MED_INFO = "med_info";
    final public static String TAG_MED_INDEX = "med_index";

    Intent intent;

    FragmentManager fragmentManager;
    AllMedsFragment allMedsFragment;
    TodayMedsFragment todayMedsFragment;

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
                //
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

        fragmentManager = getFragmentManager();
        allMedsFragment = new AllMedsFragment();
        todayMedsFragment = new TodayMedsFragment();

        intent = new Intent(getApplicationContext(), AddMedActivity.class);
        medAddBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent.putExtra(TAG_MED_INDEX, -1);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            allMedsFragment.refreshAllMedsList();
            //TODO: handle new data, refresh today
        }
    }

    @Override
    public void onFragmentAllMedsListItemClicked(Integer index)
    {
        intent.putExtra(TAG_MED_INDEX, index);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onFragmentTodayMedsListItemClicked(Integer index)
    {

        //TODO: send some data
        startActivityForResult(intent, 2);
    }
}
