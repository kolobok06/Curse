package com.example.yurez.hc2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class addMedActivity extends AppCompatActivity {

    EditText startDateEdit;
    Calendar dateCal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        setTitle(R.string.title_addMedActivity);

        startDateEdit = (EditText) findViewById(R.id.startDateEdit);
        startDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });


    }
//region datePicker

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateCal.set(Calendar.YEAR, year);
            dateCal.set(Calendar.MONTH, month);
            dateCal.set(Calendar.DAY_OF_MONTH, day);
            setInitialDate();

        }
    };

    private void setInitialDate() {
        startDateEdit.setText(
                DateUtils.formatDateTime(this, dateCal.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR /*| DateUtils.FORMAT_SHOW_TIME*/));
    }

    public void setDate(View v) {
        new DatePickerDialog(addMedActivity.this, d,
                dateCal.get(Calendar.YEAR),
                dateCal.get(Calendar.MONTH),
                dateCal.get(Calendar.DAY_OF_MONTH))
                .show();
    }
//endregion

    @Override
    protected void onStart() {
        super.onStart();

//region adapterSetToSpinner
/*ArrayAdapter<CharSequence> medTypeAdapter = ArrayAdapter.createFromResource(
this, R.array.list_medType, android.R.layout.simple_spinner_item);
medTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
final Spinner spinner = (Spinner) findViewById(R.id.medTypePicker);
spinner.setAdapter(medTypeAdapter);

spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
@Override
public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
TextView label = (TextView) findViewById(R.id.medTypeTitle);
label.setText(spinner.getSelectedItem().toString());
}

@Override
public void onNothingSelected(AdapterView<?> adapterView) {

}
});*/
//endregion

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
