package com.example.mysc.tiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecentShifts extends AppCompatActivity {

    Intent intent;
    Spinner spnYear;
    Spinner spnMonth;
    ListView lstShifts;
    ArrayList<Shift> allShifts;
    ArrayList<Shift> currentShifts;
    ShiftAdapter shiftAdapter;
    int selectedMonth;
    int selectedYear;
    Calendar calendar;
    TextView txtTotalSummary;
    TextView txtTotalSalary;
    TextView txtTotalTips;
    int totalTips = 0;
    float totalSalary = 0;
    float totalSummary = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_shifts);

        intent = getIntent();
        allShifts = (ArrayList<Shift>) intent.getSerializableExtra(HomeActivity.ALL_SHIFTS);
        currentShifts = new ArrayList<Shift>();
        spnYear = (Spinner) findViewById(R.id.spnYear);
        spnMonth = (Spinner) findViewById(R.id.spnMonth);
        txtTotalSummary = (TextView) findViewById(R.id.txtTotalSummary);
        txtTotalSalary = (TextView) findViewById(R.id.txtTotalSalary);
        txtTotalTips = (TextView) findViewById(R.id.txtTotalTips);

        Date date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedYear = calendar.get(Calendar.YEAR);

        spnMonth.setSelection(selectedMonth);
        spnYear.setSelection(selectedYear-2015);
        Shift shift;
        for (int i = 0; i < allShifts.size(); i++) {
            shift = allShifts.get(i);
            calendar.setTime(new Date(shift.startTime));
            if (calendar.get(Calendar.MONTH) == selectedMonth && calendar.get(Calendar.YEAR) == selectedYear) {
                currentShifts.add(shift);
                totalTips += shift.getTipsCount();
                totalSalary += shift.getSalary();
            }
        }
        totalSummary = totalTips + totalSalary;
        NumberFormat formatter = new DecimalFormat("#.##");
        txtTotalSummary.setText(getResources().getString(R.string.summary) + ": " + formatter.format(totalSummary));
        txtTotalSalary.setText(getResources().getString(R.string.salary) + ": " + formatter.format(totalSalary));
        txtTotalTips.setText(getResources().getString(R.string.tips) + ": " + totalTips);
        lstShifts = (ListView) findViewById(R.id.lstShifts);
        shiftAdapter = new ShiftAdapter(this, currentShifts);
        lstShifts.setAdapter(shiftAdapter);
        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position;
                notifyChanges();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = position + 2015;
                notifyChanges();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void notifyChanges() {
        currentShifts.clear();
        totalSalary = 0;
        totalTips = 0;
        Shift shift;
        for (int i = 0; i < allShifts.size(); i++) {
            shift = allShifts.get(i);
            calendar.setTime(new Date(shift.startTime));
            if (calendar.get(Calendar.MONTH) == selectedMonth && calendar.get(Calendar.YEAR) == selectedYear) {
                currentShifts.add(shift);
                totalTips += shift.getTipsCount();
                totalSalary += shift.getSalary();
            }

        }
        totalSummary = totalTips + totalSalary;
        NumberFormat formatter = new DecimalFormat("#.##");
        txtTotalSummary.setText(getResources().getString(R.string.summary) + ": " + formatter.format(totalSummary));
        txtTotalSalary.setText(getResources().getString(R.string.salary) + ": " + formatter.format(totalSalary));
        txtTotalTips.setText(getResources().getString(R.string.tips) + ": " + totalTips);
        shiftAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recent_shifts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
