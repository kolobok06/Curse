package com.example.yurez.hc2;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.widget.TimePicker;

import java.util.Collections;
import java.util.Locale;

public class AcceptActivity extends AppCompatActivity implements AddDoseDialogFragment.NoticeDialogListener
{
    private TextView doseValue, timeValue, whenToTakeValue,
            adminMethodValue, datesValue, noteValue, remAmountValue;

    Intent intent;
    MedInfo medInfo;
    SimpleMedItem simpleMedItem;
    int indexTodayList;
    Locale loc;
    AddDoseDialogFragment addDoseDialog;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        //getting
        intent = getIntent();
        indexTodayList = intent.getIntExtra(MainActivity.TAG_MED_INDEX_SIMPLE, 0);
        simpleMedItem = new SimpleMedItem(MedDataHolder.aTodayMeds.get(indexTodayList));
        medInfo = MedDataHolder.aAllMeds.get(simpleMedItem.getParentID());

        setTitle(medInfo.name);
        loc = Locale.getDefault();
        doseValue = (TextView) findViewById(R.id.acc_doseValue);
        timeValue = (TextView) findViewById(R.id.acc_timeValue);
        whenToTakeValue = (TextView) findViewById(R.id.acc_whenToTakeValue);
        adminMethodValue = (TextView) findViewById(R.id.acc_adminMethodValue);
        datesValue = (TextView) findViewById(R.id.acc_datesValue);
        noteValue = (TextView) findViewById(R.id.acc_noteValue);
        remAmountValue = (TextView) findViewById(R.id.acc_remAmount);

        fragmentManager = getFragmentManager();
        addDoseDialog = new AddDoseDialogFragment();
        doseValue.setOnClickListener(changeDoseTimeClick);
        timeValue.setOnClickListener(changeDoseTimeClick);

        Button acceptBtn = (Button) findViewById(R.id.acc_acceptBtn);
        acceptBtn.setTag(1);
        acceptBtn.setOnClickListener(clickBtn);

        Button ignoreBtn = (Button) findViewById(R.id.acc_ignoreBtn);
        ignoreBtn.setTag(null);
        ignoreBtn.setOnClickListener(clickBtn);
    }

    View.OnClickListener clickBtn = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Boolean ignored = view.getTag() == null; // identification of button

            simpleMedItem.setIgnored(ignored);
            MedDataHolder.aHistory.add(simpleMedItem); //add history stamp
            Collections.sort(MedDataHolder.aHistory);

            int resCode = ignored ? MainActivity.RESULT_IGNORE : MainActivity.RESULT_ACCEPT;
            Intent outIntent = new Intent();
            outIntent.putExtra(MainActivity.TAG_MED_INDEX_SIMPLE, indexTodayList);
            setResult(resCode, outIntent);
            AcceptActivity.this.finish();
        }
    };

    View.OnClickListener changeDoseTimeClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            addDoseDialog.show(fragmentManager, "changeDoseTime");
        }
    };

    @Override
    protected void onStart()
    {
        super.onStart();
        fillInfo();
    }

    private void fillInfo()
    {
        doseValue.setText(String.format(loc, "%s", simpleMedItem.getDoseTyped()));

        timeValue.setText(String.format(loc, "%s", simpleMedItem.getTime()));

        if (medInfo.numWhenToTake != 0)
        {
            whenToTakeValue.setVisibility(View.VISIBLE);
            whenToTakeValue.setText(String.format(loc, "%s", medInfo.whenToTake));
        } else
            whenToTakeValue.setVisibility(View.GONE);

        adminMethodValue.setText(String.format(loc, "%s", medInfo.adminMethod));

        String dates = getString(R.string.pretext_from) + " " +
                DateUtils.formatDateTime(this,
                        medInfo.startDate, DateUtils.FORMAT_NUMERIC_DATE);
        if (medInfo.finalDate > 0)
        {
            dates += " " + getString(R.string.pretext_to) + " " +
                    DateUtils.formatDateTime(this,
                            medInfo.finalDate, DateUtils.FORMAT_NUMERIC_DATE);
        }
        datesValue.setText(String.format(loc, "%s", dates));

        if (!medInfo.note.isEmpty())
        {
            noteValue.setVisibility(View.VISIBLE);
            noteValue.setText(String.format(loc, "%s", medInfo.note));
        } else
            noteValue.setVisibility(View.GONE);

        if (medInfo.remAmount > 0)
        {
            remAmountValue.setVisibility(View.VISIBLE);
            remAmountValue.setText(String.format(loc, "%s: %.2f %s",
                    getString(R.string.title_remAmount), medInfo.remAmount, medInfo.medType));
        } else
            remAmountValue.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            setResult(RESULT_CANCELED);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        String doseStr = ((EditText) dialog.getDialog().findViewById(R.id.dialog_doseEdit))
                .getText().toString();

        TimePicker timePicker = (TimePicker) dialog.getDialog().
                findViewById(R.id.dialog_timePicker);

        simpleMedItem.setDoseTime(new
                DoseTime(timePicker.getHour(), timePicker.getMinute(), Float.parseFloat(doseStr)));

        doseValue.setText(String.format(loc, "%s %s", doseStr, medInfo.medType));
        timeValue.setText(String.format(loc, "%s", simpleMedItem.getTime()));
    }

    @Override
    public void onDialogStarted(DialogFragment dialog)
    {
        ((TextView) dialog.getDialog()
                .findViewById(R.id.dialog_medTypeTitle)).setText(medInfo.medType);
        ((EditText) dialog.getDialog()
                .findViewById(R.id.dialog_doseEdit)).setText(simpleMedItem.getDose());

        TimePicker timePicker = (TimePicker) dialog.getDialog()
                .findViewById(R.id.dialog_timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(simpleMedItem.getHour());
        timePicker.setMinute(simpleMedItem.getMin());
    }
}
