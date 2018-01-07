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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMedActivity extends AppCompatActivity implements AddDoseDialogFragment.NoticeDialogListener {
    //region InitVariables
    //constants
    final public static String TAG_DIALOG_ADD_DOSE = "addDoseDialog";
    final public static int STATE_EVERYDAY = 0;
    final public static int STATE_INTERVAL = 1;
    final public static int STATE_CYCLE = 2;
    final public static int STATE_HOURS = 3;
    private String[] hours;

    int planState = STATE_EVERYDAY;
    public static String medType;
    //variable visibility
    LinearLayout xyLayout, hoursLayout, finalDateLayout;
    NumberPicker xPicker, yPicker, hoursPicker;
    //date select variables
    TextView startDatePicker, finalDatePicker;
    Calendar dateCal = Calendar.getInstance();
    View activeDateView; //for which the date will be selected
    //dialog variables
    FragmentManager fragmentManager;
    DialogFragment addDoseDialog;
    //Long startDateMs, finalDateMs;
    Spinner medTypePicker;
    //Dose and Time List variables
    ArrayList<DoseTime> doseTimeList = new ArrayList<>();
    RecyclerView doseTimeRecView;
    RecyclerView.Adapter doseTimeRecAdapter;
    RecyclerView.LayoutManager doseTimeRecLayoutManager;
    //parsing variables
    Pattern patternInt, patternDot;
    Matcher matcher;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        setTitle(R.string.title_addMedActivity);

        patternInt = Pattern.compile("[0-9]([0-2]?)");
        patternDot = Pattern.compile("[.]");

        xyLayout = (LinearLayout) findViewById(R.id.xyLayout);
        hoursLayout = (LinearLayout) findViewById(R.id.hoursLayout);
        xPicker = (NumberPicker) findViewById(R.id.xPicker);
        yPicker = (NumberPicker) findViewById(R.id.yPicker);
        hoursPicker = (NumberPicker) findViewById(R.id.hoursPicker);
        hours = getResources().getStringArray(R.array.list_hours);
        yPicker.setMaxValue(60);
        yPicker.setMinValue(1);
        xPicker.setMaxValue(60);
        xPicker.setMinValue(1);
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(hours.length - 1);
        hoursPicker.setDisplayedValues(hours);

        finalDateLayout = (LinearLayout) findViewById(R.id.finalDateLayout);

        startDatePicker = (TextView) findViewById(R.id.startDatePicker);
        startDatePicker.setOnClickListener(datePickerListener);
        finalDatePicker = (TextView) findViewById(R.id.finalDatePicker);
        finalDatePicker.setOnClickListener(datePickerListener);

        doseTimeRecView = (RecyclerView) findViewById(R.id.doseTimeRecView);
        doseTimeRecLayoutManager = new LinearLayoutManager(this);
        doseTimeRecView.setLayoutManager(doseTimeRecLayoutManager);
        doseTimeRecAdapter = new DoseTimeRecViewAdapter(doseTimeList);
        doseTimeRecView.setAdapter(doseTimeRecAdapter);

        fragmentManager = getFragmentManager();
        addDoseDialog = new AddDoseDialogFragment();

        medTypePicker = (Spinner) findViewById(R.id.medTypePicker);

        Button addDoseBtn = (Button) findViewById(R.id.addDoseBtn);
        addDoseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medType = medTypePicker.getSelectedItem().toString();
                addDoseDialog.show(fragmentManager, TAG_DIALOG_ADD_DOSE);
            }
        });

    }
    //change visibility by state
    private void handleState() {
        switch (planState) {
            case STATE_EVERYDAY:
                finalDateLayout.setVisibility(View.GONE);
                xyLayout.setVisibility(View.GONE);
                hoursLayout.setVisibility(View.GONE);
                break;
            case STATE_INTERVAL:
                finalDateLayout.setVisibility(View.VISIBLE);
                xyLayout.setVisibility(View.GONE);
                hoursLayout.setVisibility(View.GONE);
                break;
            case STATE_CYCLE:
                finalDateLayout.setVisibility(View.GONE);
                xyLayout.setVisibility(View.VISIBLE);
                hoursLayout.setVisibility(View.GONE);
                break;
            case STATE_HOURS:
                finalDateLayout.setVisibility(View.VISIBLE);
                xyLayout.setVisibility(View.GONE);
                hoursLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    //region datePicker
    View.OnClickListener datePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activeDateView = view;
            setDate(view);
        }
    };

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateCal.set(Calendar.YEAR, year);
            dateCal.set(Calendar.MONTH, month);
            dateCal.set(Calendar.DAY_OF_MONTH, day);
            setInitialDate(activeDateView);
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
        String[] strings = getResources().getStringArray(R.array.list_plan);
        ArrayList<String> planList = new ArrayList<>(Arrays.asList(strings));
        PlanPickerAdapter planAdapter = new PlanPickerAdapter(this, planList);

        Spinner spinner = (Spinner) findViewById(R.id.planPicker);
        spinner.setAdapter(planAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                planState = i;
                handleState();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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

        medInfo.medType = medType;

        picker = (Spinner) findViewById(R.id.planPicker);
        medInfo.plan = picker.getSelectedItem().toString();

        medInfo.startDate = (Long) startDatePicker.getTag(); //not trusted

        if ((planState == STATE_INTERVAL)||(planState == STATE_HOURS)) {
            medInfo.finalDate = (Long) finalDatePicker.getTag();
        }

        medInfo.doseTimes = doseTimeList;

        picker = (Spinner) findViewById(R.id.whenToTakePicker);
        medInfo.whenToTake = picker.getSelectedItem().toString();

        picker = (Spinner) findViewById(R.id.adminMethodPicker);
        medInfo.adminMethod = picker.getSelectedItem().toString();

        edit = (EditText) findViewById(R.id.remAmountEdit);
        medInfo.remAmount = Integer.parseInt(edit.getText().toString());

        edit = (EditText) findViewById(R.id.noteEdit);
        medInfo.note = edit.getText().toString();

        medInfo.state = planState;

        if (planState == STATE_CYCLE) {
            //TODO: Add countPos, countNeg
        }
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
                MedDataHolder.aAllMeds.add(grabMedInfo());
                setResult(RESULT_OK);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void addHoursTime(Integer hour, Integer min, Float dose) {
        doseTimeList.clear();
        Integer incHour, incMin;
        //parsing
        String fromPicker = hours[hoursPicker.getValue()];
        matcher = patternInt.matcher(fromPicker);
        matcher.find();
        incHour = Integer.parseInt(fromPicker.substring(0, matcher.end()));
        matcher = patternDot.matcher(fromPicker);
        incMin = matcher.find() ? 30: 0;

        Integer countTimes = ((24 * 60) / (incHour * 60 + incMin)) - 1; // number of calls without initial
        Integer curH = hour + incHour; //current adding time
        Integer curM = min + incMin;
        while ((curH < 24) && (countTimes != 0)) {
            if (curM > 59) {
                ++curH;
                curM -= 60;
                continue; //for checking 24 bound
            }
            doseTimeList.add(new DoseTime(curH, curM, dose));
            curH += incHour;
            curM += incMin;
            --countTimes;
        }
        curH = hour - incHour;
        curM = min - incMin;
        while ((curH >= 0) && (countTimes != 0)) {
            if (curM < 0) {
                --curH;
                curM += 60;
                continue; //for checking 0 bound
            }
            doseTimeList.add(new DoseTime(curH, curM, dose));
            curH -= incHour;
            curM -= incMin;
            --countTimes;
        }
    }

    @Override
    public void onDialogStarted(DialogFragment dialog) {
        ((TextView) dialog.getDialog().findViewById(R.id.dialog_medTypeTitle)).setText(medType);
        ((EditText) dialog.getDialog().findViewById(R.id.dialog_doseEdit)).setText("1.00");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //parsing
        TimePicker timePicker = (TimePicker) dialog.getDialog().findViewById(R.id.dialog_timePicker);
        Integer hour = timePicker.getHour();
        Integer min = timePicker.getMinute();
        EditText editText = (EditText) dialog.getDialog().findViewById(R.id.dialog_doseEdit);
        String doseStr = editText.getText().toString();
        Float dose = Float.parseFloat(doseStr);
        //add new item
        if (planState == STATE_HOURS) {
            addHoursTime(hour, min, dose);
        }
        doseTimeList.add(new DoseTime(hour, min, dose));
        Collections.sort(doseTimeList);
        doseTimeRecAdapter.notifyDataSetChanged();
    }
}
