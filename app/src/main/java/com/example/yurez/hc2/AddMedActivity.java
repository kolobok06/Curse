package com.example.yurez.hc2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMedActivity extends AppCompatActivity implements AddDoseDialogFragment.NoticeDialogListener
{
    //region InitVariables
    //constants
    final public static String TAG_DIALOG_ADD_DOSE = "add_dose_dialog";
    final public static int STATE_EVERYDAY = 0;
    final public static int STATE_INTERVAL = 1;
    final public static int STATE_CYCLE = 2;
    final public static int STATE_HOURS = 3;
    private String[] hours;

    int index;
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
    protected void onCreate(Bundle savedInstanceState)
    {
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
        doseTimeRecView.setAdapter(doseTimeRecAdapter); //TODO: OnClick

        fragmentManager = getFragmentManager();
        addDoseDialog = new AddDoseDialogFragment();

        medTypePicker = (Spinner) findViewById(R.id.medTypePicker);

        Button addDoseBtn = (Button) findViewById(R.id.addDoseBtn);
        addDoseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                medType = medTypePicker.getSelectedItem().toString();
                addDoseDialog.show(fragmentManager, TAG_DIALOG_ADD_DOSE);
            }
        });

    }

    //change visibility by state
    private void handleState()
    {
        switch (planState)
        {
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
    View.OnClickListener datePickerListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            activeDateView = view;
            setDate(view);
        }
    };

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day)
        {
            dateCal.set(Calendar.YEAR, year);
            dateCal.set(Calendar.MONTH, month);
            dateCal.set(Calendar.DAY_OF_MONTH, day);
            dateCal.set(Calendar.HOUR_OF_DAY, 0);
            dateCal.set(Calendar.MINUTE, 0);
            dateCal.set(Calendar.SECOND, 0);
            dateCal.set(Calendar.MILLISECOND, 0);
            setInitialDate(activeDateView);
        }
    };

    private void setInitialDate(View view)
    {
        TextView tv = (TextView) view;
        tv.setText(
                DateUtils.formatDateTime(this, dateCal.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        view.setTag(dateCal.getTimeInMillis());
    }

    public void setDate(View v)
    {
        new DatePickerDialog(AddMedActivity.this, d,
                dateCal.get(Calendar.YEAR),
                dateCal.get(Calendar.MONTH),
                dateCal.get(Calendar.DAY_OF_MONTH))
                .show();
    }
//endregion

    @Override
    protected void onStart()
    {
        super.onStart();
        String[] strings = getResources().getStringArray(R.array.list_plan);
        ArrayList<String> planList = new ArrayList<>(Arrays.asList(strings));
        PlanPickerAdapter planAdapter = new PlanPickerAdapter(this, planList);

        finalDatePicker.setTag(null);
        long curTime = System.currentTimeMillis();
        startDatePicker.setTag(curTime); //set now date
        startDatePicker.setText(
                DateUtils.formatDateTime(this, curTime,
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        Spinner spinner = (Spinner) findViewById(R.id.planPicker);
        spinner.setAdapter(planAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                planState = i;
                handleState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        index = getIntent().getIntExtra(MainActivity.TAG_MED_INDEX_SIMPLE, -1);
        if (index >= 0)
            fillInfo(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.upbar, menu);
        return true;
    }

    private MedInfo grabMedInfo()
    {
        MedInfo medInfo = new MedInfo(null);
        EditText edit;
        Spinner picker;

        edit = (EditText) findViewById(R.id.medNameEdit);
        medInfo.name = edit.getText().toString();

        medInfo.medType = medType;
        medInfo.numMedType = medTypePicker.getSelectedItemPosition();

        picker = (Spinner) findViewById(R.id.planPicker);
        medInfo.plan = picker.getSelectedItem().toString();

        medInfo.startDate = (Long) startDatePicker.getTag();

        if ((planState == STATE_INTERVAL) ||
                ((planState == STATE_HOURS) && (finalDatePicker.getTag() != null)))
        {
            medInfo.finalDate = (Long) finalDatePicker.getTag();
        }

        medInfo.doseTimes = doseTimeList;

        picker = (Spinner) findViewById(R.id.whenToTakePicker);
        medInfo.whenToTake = picker.getSelectedItem().toString();
        medInfo.numMedType = picker.getSelectedItemPosition();

        picker = (Spinner) findViewById(R.id.adminMethodPicker);
        medInfo.adminMethod = picker.getSelectedItem().toString();

        edit = (EditText) findViewById(R.id.remAmountEdit);

        if (edit.getText().length() != 0)
            medInfo.remAmount = Float.parseFloat(edit.getText().toString());

        edit = (EditText) findViewById(R.id.noteEdit);
        medInfo.note = edit.getText().toString();

        medInfo.state = planState;
        return medInfo;
    }

    void fillInfo(Integer index)
    {
        MedInfo medInfo = MedDataHolder.aAllMeds.get(index);
        EditText edit;
        Spinner picker;

        edit = (EditText) findViewById(R.id.medNameEdit);
        edit.setText(medInfo.name);

        medType = medInfo.medType;
        picker = (Spinner) findViewById(R.id.medTypePicker);
        picker.setSelection(medInfo.numMedType);

        dateCal.setTimeInMillis(medInfo.startDate);
        startDatePicker.setTag(medInfo.startDate);
        startDatePicker.setText(
                DateUtils.formatDateTime(this, medInfo.startDate,
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        planState = medInfo.state;
        handleState();
        if ((planState == STATE_INTERVAL) ||
                ((planState == STATE_HOURS) || (medInfo.finalDate < Long.MAX_VALUE)))
        {
            finalDatePicker.setTag(medInfo.finalDate);
            finalDatePicker.setText(
                    DateUtils.formatDateTime(this, medInfo.finalDate,
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        }

        doseTimeList = medInfo.doseTimes;
        doseTimeRecAdapter.notifyDataSetChanged();

        picker = (Spinner) findViewById(R.id.whenToTakePicker);
        picker.setSelection(medInfo.numWhenToTake);

        picker = (Spinner) findViewById(R.id.adminMethodPicker);
        picker.setSelection(medInfo.numAdminMethod);

        if (medInfo.remAmount > 0)
        {
            edit = (EditText) findViewById(R.id.remAmountEdit);
            edit.setText(String.format(Locale.US, "%.2f", medInfo.remAmount));
        }

        edit = (EditText) findViewById(R.id.noteEdit);
        edit.setText(medInfo.note);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                this.finish();
                return true;
            case R.id.saveMed:
                if (checkEmpty())
                {
                    Intent intent = new Intent();
                    if (index < 0)
                    {
                        MedDataHolder.aAllMeds.add(grabMedInfo());
                        index = MedDataHolder.aAllMeds.size() - 1;
                    } else
                        MedDataHolder.aAllMeds.set(index, grabMedInfo());
                    intent.putExtra(MainActivity.TAG_MED_INDEX, index);
                    setResult(RESULT_OK, intent);
                    this.finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkEmpty()
    {
        int hintStrId = 0;
        boolean passed = true;
        EditText edit = (EditText) findViewById(R.id.medNameEdit);
        if ((edit.getText().length() == 0))
        {
            hintStrId = R.string.hint_empty_name;
            passed = false;
        }
        if (doseTimeList.isEmpty())
        {
            hintStrId = R.string.hint_empty_doseTimeList;
            passed = false;
        }
        if ((planState == STATE_INTERVAL) && (finalDatePicker.getTag() == null))
        {
            hintStrId = R.string.hint_empty_finalDate;
            passed = false;
        }
        if (!passed)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.empty_dialog_header)
                    .setMessage(hintStrId)
                    .setCancelable(false)
                    .setPositiveButton(R.string.title_ok,
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    dialogInterface.cancel();
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return passed;
    }

    void addHoursTime(Integer hour, Integer min, Float dose)
    {
        doseTimeList.clear();
        Integer incHour, incMin;
        //parsing
        String fromPicker = hours[hoursPicker.getValue()];
        matcher = patternInt.matcher(fromPicker);
        matcher.find();
        incHour = Integer.parseInt(fromPicker.substring(0, matcher.end()));
        matcher = patternDot.matcher(fromPicker);
        incMin = matcher.find() ? 30 : 0;

        Integer countTimes = ((24 * 60) / (incHour * 60 + incMin)) - 1; // number of calls without initial
        Integer curH = hour + incHour; //current adding time
        Integer curM = min + incMin;
        while ((curH < 24) && (countTimes != 0))
        {
            if (curM > 59)
            {
                ++curH;
                curM -= 60; //goto check 24 bound
            } else
            {
                doseTimeList.add(new DoseTime(curH, curM, dose));
                curH += incHour;
                curM += incMin;
                --countTimes;
            }
        }
        curH = hour - incHour;
        curM = min - incMin;
        while ((curH >= 0) && (countTimes != 0))
        {
            if (curM < 0)
            {
                --curH;
                curM += 60; //goto check 0 bound
            } else
            {
                doseTimeList.add(new DoseTime(curH, curM, dose));
                curH -= incHour;
                curM -= incMin;
                --countTimes;
            }
        }
    }

    @Override
    public void onDialogStarted(DialogFragment dialog)
    {
        ((TextView) dialog.getDialog().findViewById(R.id.dialog_medTypeTitle)).setText(medType);
        ((EditText) dialog.getDialog().findViewById(R.id.dialog_doseEdit)).setText(R.string.val_defaultDose);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        //parsing
        TimePicker timePicker = (TimePicker) dialog.getDialog().findViewById(R.id.dialog_timePicker);
        Integer hour = timePicker.getHour();
        Integer min = timePicker.getMinute();
        EditText editText = (EditText) dialog.getDialog().findViewById(R.id.dialog_doseEdit);
        String doseStr = editText.getText().toString();
        Float dose = Float.parseFloat(doseStr);
        //add new item
        if (planState == STATE_HOURS)
            addHoursTime(hour, min, dose);
        doseTimeList.add(new DoseTime(hour, min, dose));
        Collections.sort(doseTimeList);
        doseTimeRecAdapter.notifyDataSetChanged();
    }
}
