package com.example.yurez.hc2;

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
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
//            }
//            return false;
        }
    };

    private void selectFragment(MenuItem item) {
        
    }


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
            }
        });
    }



}
