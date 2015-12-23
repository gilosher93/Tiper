package com.example.mysc.tiper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
    DBAdapter dbAdapter;
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
        dbAdapter = new DBAdapter(this);
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
        lstShifts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
                AlertDialog.Builder builder = new AlertDialog.Builder(RecentShifts.this)
                        .setTitle(getString(R.string.delete_shift_title))
                        .setMessage(getString(R.string.delete_shift_query)+" "+simpleDateFormat.format(new Date(currentShifts.get(position).startTime)) + " ?")
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.yes,
                                new Dialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int k) {
                                        removeShift(position);
                                        notifyChanges();
                                        Toast.makeText(getBaseContext(), "משמרת נמחקה בהצלחה!", Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new Dialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                builder.create().show();
                return true;
            }
        });
    }
    void writingToDb(ArrayList<Shift> shifts) {
        try {
            dbAdapter.open();
            for (int i = 0; i < shifts.size(); i++) {
                dbAdapter.insertShiftToDB(shifts.get(i));
            }
            dbAdapter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void printAllDB(){
        try {
            dbAdapter.open();
            Cursor cursor = dbAdapter.getAllShifts();
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                long startTime = cursor.getLong(1);
                long endTime = cursor.getLong(2);
                float salary = cursor.getFloat(4);
                int tips = cursor.getInt(5);
                Shift shift = new Shift(startTime, endTime, salary, tips,id);
                Log.d("Gil",shift.toString());
            }
            dbAdapter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean removeShift(int position) {
        for (int i = 0; i < allShifts.size(); i++) {
            if(currentShifts.get(position).getId() == allShifts.get(i).getId()){
                allShifts.remove(i);
                return true;
            }
        }
        return false;
    }

    private void notifyChanges() {
        clearDb();
        writingToDb(allShifts);
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

    public void clearDb(){
        try {
            dbAdapter.open();
            dbAdapter.clearDB();
            dbAdapter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
