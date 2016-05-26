package co.il.tipper.mysc.tipper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import co.il.tipper.mysc.tipper.HomeActivity;
import co.il.tipper.mysc.tipper.R;
import co.il.tipper.mysc.tipper.objects.Shift;
import co.il.tipper.mysc.tipper.adapters.DBAdapter;

public class CreateManualShift extends Fragment {

    TimePicker startPicker;
    TimePicker endPicker;
    DatePicker datePicker;
    EditText txtTips;
    Button btnOk;
    Button btnCancel;
    SharedPreferences sharedPreferences;
    DBAdapter dbAdapter;
    private CreateManualShiftListener listener;

    public void setFragment(CreateManualShiftListener listener){
        this.listener = listener;
    }

    public interface CreateManualShiftListener{
        void addShift(Shift shift);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_manual_shift,container, false);
        dbAdapter = new DBAdapter(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(HomeActivity.prefName, Context.MODE_PRIVATE);
        startPicker = (TimePicker) view.findViewById(R.id.startPicker);
        endPicker = (TimePicker) view.findViewById(R.id.endPicker);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        txtTips = (EditText) view.findViewById(R.id.txtManualTips);
        endPicker.setIs24HourView(true);
        startPicker.setIs24HourView(true);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        startPicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        startPicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        endPicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        endPicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        datePicker.setFirstDayOfWeek(Calendar.DAY_OF_WEEK);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startHour = String.valueOf(startPicker.getCurrentHour());
                String startMinutes = String.valueOf(startPicker.getCurrentMinute());
                String endHour = String.valueOf(endPicker.getCurrentHour());
                String endMinutes = String.valueOf(endPicker.getCurrentMinute());
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth()+1);
                String year = String.valueOf(datePicker.getYear());
                String tips = txtTips.getText().toString();
                float salary = sharedPreferences.getFloat(HomeActivity.SALARY, 25);
                int manualTips = tips.length() == 0 ? 0 : Integer.valueOf(tips);
                long startTime = Shift.getFullDateInLong(year + "-" + month + "-" + day + ", " + startHour + ":" + startMinutes);
                long endTime = Shift.getFullDateInLong(year + "-" + month + "-" + day + ", " + endHour + ":" + endMinutes);
                Shift shift = new Shift(startTime, endTime, salary, manualTips,getShiftCountFromDb()+1);
                listener.addShift(shift);
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    void writingToDb(Shift shift){
        try {
            dbAdapter = new DBAdapter(getActivity());
            dbAdapter.open();
            dbAdapter.insertShiftToDB(shift);
            dbAdapter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getShiftCountFromDb() {
        int count = 0;
        try {
            dbAdapter.open();
            Cursor cursor = dbAdapter.getAllShifts();
            while (cursor.moveToNext()) {
                count++;
            }
            dbAdapter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}