package co.il.tipper.mysc.tipper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import co.il.tipper.mysc.tipper.R;
import co.il.tipper.mysc.tipper.objects.Shift;
import co.il.tipper.mysc.tipper.adapters.DBAdapter;
import co.il.tipper.mysc.tipper.adapters.ShiftAdapter;

public class RecentShiftsFragment extends Fragment {

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
    TextView txtTotalAverage;
    int totalTips = 0;
    float totalSalary = 0;
    float totalSummary = 0;
    float totalAverage = 0;

    public void setFragment(ArrayList<Shift> shifts){
        this.allShifts = shifts;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_shifts, container, false);
        dbAdapter = new DBAdapter(getActivity().getApplicationContext());
        currentShifts = new ArrayList<Shift>();
        spnYear = (Spinner) view.findViewById(R.id.spnYear);
        spnMonth = (Spinner) view.findViewById(R.id.spnMonth);
        txtTotalSummary = (TextView) view.findViewById(R.id.txtTotalSummary);
        txtTotalSalary = (TextView) view.findViewById(R.id.txtTotalSalary);
        txtTotalTips = (TextView) view.findViewById(R.id.txtTotalTips);
        txtTotalAverage = (TextView) view.findViewById(R.id.txtTotalAverage);

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
            calendar.setTime(new Date(shift.getStartTime()));
            if (calendar.get(Calendar.MONTH) == selectedMonth && calendar.get(Calendar.YEAR) == selectedYear) {
                currentShifts.add(shift);
                totalTips += shift.getTipsCount();
                totalSalary += shift.getSalary();
                totalAverage += shift.getAverageSalaryPerHour();
            }
        }
        totalSummary = totalTips + totalSalary;
        NumberFormat formatter = new DecimalFormat("#.##");
        totalAverage /= currentShifts.size();
        txtTotalAverage.setText(getResources().getString(R.string.average_salary) + ": " + formatter.format(totalAverage)+"₪"+getResources().getString(R.string.per_hour));
        txtTotalSummary.setText(formatter.format(totalSummary)+"₪");
        txtTotalSalary.setText(formatter.format(totalSalary)+"₪");
        txtTotalTips.setText(totalTips+"₪");
        lstShifts = (ListView) view.findViewById(R.id.lstShifts);
        shiftAdapter = new ShiftAdapter(getActivity().getApplicationContext(), currentShifts);
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
                ShiftOptionFragment shiftOptionFragment = new ShiftOptionFragment();
                shiftOptionFragment.setFragment(new ShiftOptionFragment.OnSelectShiftOptionListener() {

                    @Override
                    public void editShift() {

                    }

                    @Override
                    public void deleteShift() {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.delete_shift_title))
                                .setMessage(getString(R.string.delete_shift_query) + " " + simpleDateFormat.format(new Date(currentShifts.get(position).getStartTime())) + " ?")
                                .setCancelable(true)
                                .setPositiveButton(android.R.string.yes,
                                        new Dialog.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int k) {
                                                removeShift(position);
                                                notifyChanges();
                                                Toast.makeText(getActivity().getApplicationContext(), "משמרת נמחקה בהצלחה!", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                .setNegativeButton(android.R.string.no,
                                        new Dialog.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                        builder.create().show();
                    }
                });
                shiftOptionFragment.show(getFragmentManager(),"shift option fragment");
                return true;
            }
        });
        return view;
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
        totalAverage = 0;
        Shift shift;
        for (int i = 0; i < allShifts.size(); i++) {
            shift = allShifts.get(i);
            calendar.setTime(new Date(shift.getStartTime()));
            if (calendar.get(Calendar.MONTH) == selectedMonth && calendar.get(Calendar.YEAR) == selectedYear) {
                currentShifts.add(shift);
                totalTips += shift.getTipsCount();
                totalSalary += shift.getSalary();
                totalAverage += shift.getAverageSalaryPerHour();
            }

        }
        totalSummary = totalTips + totalSalary;
        totalAverage = currentShifts.size() > 0 ? totalAverage / currentShifts.size() : 0;
        NumberFormat formatter = new DecimalFormat("#.##");
        txtTotalAverage.setText(getResources().getString(R.string.average_salary) + ": " + formatter.format(totalAverage)+"₪ "+getResources().getString(R.string.per_hour));
        txtTotalSummary.setText(formatter.format(totalSummary)+"₪");
        txtTotalSalary.setText(formatter.format(totalSalary)+"₪");
        txtTotalTips.setText(totalTips+"₪");
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
}
