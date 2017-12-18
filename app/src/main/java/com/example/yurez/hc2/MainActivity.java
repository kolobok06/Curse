package com.example.yurez.hc2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.support.design.widget.FloatingActionButton.*;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
// ОБРАБОТЧИК КЛИКА НА НАВИГАЦИЮ
//            selectFragment(item);
            return true;
        }
    };

//    private void selectFragment(MenuItem item) {
//        Fragment frag = null;
//        // init corresponding fragment
//        switch (item.getItemId()) {
//            case R.id.navigation_history:
//                frag = MenuFragment.newInstance(getString(R.string.text_home),
//                        getColorFromRes(R.color.color_home));
//                break;
//            case R.id.navigation_today:
//                frag = MenuFragment.newInstance(getString(R.string.text_notifications),
//                        getColorFromRes(R.color.color_notifications));
//                break;
//            case R.id.navigation_allMeds:
//                frag = MenuFragment.newInstance(getString(R.string.text_search),
//                        getColorFromRes(R.color.color_search));
//                break;
//    }

//        public void changeFragment(Fragment fragment) {
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.container, fragment) // id вашего FrameLayout
//                    .commit();
//        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FloatingActionButton medAdd = (FloatingActionButton) findViewById(R.id.addMed);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        medAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // fab act
                Intent intent = new Intent(getApplicationContext(), addMedActivity.class);
                startActivity(intent);
            }
        });
    }



}
