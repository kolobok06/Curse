package com.example.yurez.hc2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import static android.support.design.widget.FloatingActionButton.*;

public class MainActivity extends AppCompatActivity {
    ArrayList<MedInfo> aMeds = new ArrayList<MedInfo>();
    TodayListAdapter todayListAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
// ОБРАБОТЧИК КЛИКА НА НАВИГАЦИЮ
            selectFragment(item);
            return true;
        }
    };
//TODO:Fragments. Selector here
    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.navigation_history:
                //
                break;
            case R.id.navigation_today:
                //
                break;
            case R.id.navigation_allMeds:
                //
                break;
        }
        changeFragment(frag);
    }

        public void changeFragment(Fragment fragment) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.containerLayout, fragment) // id вашего FrameLayout
                    .commit();
        }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FloatingActionButton medAddBtn = (FloatingActionButton) findViewById(R.id.addMedBtn);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        todayListAdapter = new TodayListAdapter(this, aMeds);
        ListView todayItemsList = (ListView) findViewById(R.id.todayItemsList);
        todayItemsList.setAdapter(todayListAdapter);

        medAddBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addMedActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0)
            if (resultCode == RESULT_OK) {
                MedInfo medInfo = new MedInfo();
                medInfo.unpackMed(data.getStringArrayExtra("med"));
                aMeds.add(medInfo);
                todayListAdapter.notifyDataSetChanged();
                //TODO: handle new data
            }
    }
}
