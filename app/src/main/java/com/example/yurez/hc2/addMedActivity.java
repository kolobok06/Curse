package com.example.yurez.hc2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

public class addMedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        setTitle(R.string.title_addMedActivity);

//        final Spinner spinner = (Spinner) findViewById(R.id.medTypePicker);
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView label = (TextView) findViewById(R.id.medTypeTitle);
//                label.setText(spinner.getSelectedItem().toString());
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.upbar, menu);
        return true;
    }

    private MedInfo grabMedInfo() {
        MedInfo medInfo = new MedInfo();
        EditText edit;
        Spinner picker;

        edit = (EditText) this.findViewById(R.id.medNameEdit);
        medInfo.name = edit.getText().toString();
        edit = (EditText) this.findViewById(R.id.doseEdit);
        medInfo.doze = Float.parseFloat(edit.getText().toString());
        picker = (Spinner) this.findViewById(R.id.medTypePicker);
        medInfo.medType = picker.getSelectedItem().toString();
        picker = (Spinner) this.findViewById(R.id.planPicker);
        medInfo.plan = picker.getSelectedItem().toString();
        picker = (Spinner) this.findViewById(R.id.whenToTakePicker);
        medInfo.whenToTake = picker.getSelectedItem().toString();
        picker = (Spinner) this.findViewById(R.id.adminMethodPicker);
        medInfo.adminMethod = picker.getSelectedItem().toString();

        /*
        List<String> Lines = Arrays.asList(getResources().getStringArray(R.array.Lines));
        public String name;
        public String doze;
        public String medType;
        public String plan;
        public String whenToTake;
        public String adminMethod;
        */
        return medInfo;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.goBack:
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                this.finish();
                return true;

            case R.id.saveMed:
                //TODO: check empty fields
                Intent intent = new Intent();
                MedInfo medInfo = grabMedInfo();
                intent.putExtra("med", medInfo.packMed());
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
