package com.example.yurez.hc2;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class AddMedActivity extends AppCompatActivity implements AddDoseDialogFragment.NoticeDialogListener {
    final private static String TAG_DIALOG_ADD_DOSE = "addDoseDialog";
    public static String medType;
    TextView startDatePicker, finalDatePicker;
    Calendar dateCal = Calendar.getInstance();
    View activeView;
    FragmentManager fragmentManager;
    DialogFragment addDoseDialog;
    ArrayList<DoseTime> doseTimeList = new ArrayList<>();
    //Long startDateMs, finalDateMs;
    Spinner medTypePicker;

    RecyclerView doseTimeRecView;
    RecyclerView.Adapter doseTimeRecAdapter;
    RecyclerView.LayoutManager doseTimeRecLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        setTitle(R.string.title_addMedActivity);

        startDatePicker = (TextView) findViewById(R.id.startDatePicker);
        startDatePicker.setOnClickListener(datePickerListener);
        finalDatePicker = (TextView) findViewById(R.id.finalDatePicker);
        finalDatePicker.setOnClickListener(datePickerListener);

        doseTimeRecView = (RecyclerView) findViewById(R.id.doseTimeRecView);
        //doseTimeRecView.setHasFixedSize(true);

        doseTimeRecLayoutManager = new LinearLayoutManager(this);
        doseTimeRecView.setLayoutManager(doseTimeRecLayoutManager);
        doseTimeRecAdapter = new DoseTimeRecViewAdapter(doseTimeList);
        doseTimeRecView.setAdapter(doseTimeRecAdapter);

        fragmentManager = getFragmentManager();
        addDoseDialog = new AddDoseDialogFragment();

        medTypePicker  = (Spinner) findViewById(R.id.medTypePicker);

        Button addDoseBtn = (Button) findViewById(R.id.addDoseBtn);
        addDoseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medType = medTypePicker.getSelectedItem().toString();
                addDoseDialog.show(fragmentManager, TAG_DIALOG_ADD_DOSE);
            }
        });


    }

    View.OnClickListener datePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activeView = view;
            setDate(view);
        }
    };

//region datePicker

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateCal.set(Calendar.YEAR, year);
            dateCal.set(Calendar.MONTH, month);
            dateCal.set(Calendar.DAY_OF_MONTH, day);
            setInitialDate(activeView);
        }
    };

    private void setInitialDate(View view) {
        TextView tv = (TextView) view;
        tv.setText(
                DateUtils.formatDateTime(this, dateCal.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        view.setTag(dateCal.getTimeInMillis());
    }

    public void setDate(View v) {
        new DatePickerDialog(AddMedActivity.this, d,
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

        edit = (EditText) findViewById(R.id.medNameEdit);
        medInfo.name = edit.getText().toString();

        //edit = (EditText) findViewById(R.id.doseEdit);
        //medInfo.dose = Float.parseFloat(edit.getText().toString());

        picker = (Spinner) findViewById(R.id.medTypePicker);
        medInfo.medType = picker.getSelectedItem().toString();

        picker = (Spinner) findViewById(R.id.planPicker);
        medInfo.plan = picker.getSelectedItem().toString();

        medInfo.startDate = (Long) startDatePicker.getTag(); //not trusted

        medInfo.finalDate = (Long) finalDatePicker.getTag();

        picker = (Spinner) findViewById(R.id.whenToTakePicker);
        medInfo.whenToTake = picker.getSelectedItem().toString();

        picker = (Spinner) findViewById(R.id.adminMethodPicker);
        medInfo.adminMethod = picker.getSelectedItem().toString();

        edit = (EditText) findViewById(R.id.remAmountEdit);
        medInfo.remAmount = Integer.parseInt(edit.getText().toString());

        return medInfo;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                this.finish();
                return true;
            case R.id.saveMed:
                //TODO: check empty fields
                //TODO: Add medInfo to holder
                MedDataHolder.aAllMeds.add(grabMedInfo());
                setResult(RESULT_OK);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogStarted(DialogFragment dialog) {
        TextView tv = (TextView) dialog.getDialog().findViewById(R.id.dialog_medTypeTitle);
        tv.setText(medType);
        EditText et = (EditText) dialog.getDialog().findViewById(R.id.dialog_doseEdit);
        et.setText("1.00");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //parsing
        TimePicker timePicker = (TimePicker) dialog.getDialog().findViewById(R.id.dialog_timePicker);
        Integer hours = timePicker.getHour();
        Integer mins = timePicker.getMinute();
        EditText editText = (EditText) dialog.getDialog().findViewById(R.id.dialog_doseEdit);
        String doseStr = editText.getText().toString();
        Float dose = Float.parseFloat(doseStr);
        //add new item
        doseTimeList.add(new DoseTime(hours, mins, dose));
        doseTimeRecAdapter.notifyDataSetChanged();
        //TODO: Add to list and base
    }
}