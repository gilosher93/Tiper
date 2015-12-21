package com.example.mysc.tiper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    public static final String prefName = "prefName";
    public static final String START_TIME = "start_time";
    public static final String SHIFT_SALARY = "SHIFT_SALARY";
    public static final String ALL_SHIFTS = "all_shifts";
    public static final String IS_START_WORKING = "is_start_working";
    public static final String DAILY_TIPS = "DAILY_TIPS";
    public static final String DAILY_SALARY = "DAILY_SALARY";
    public static final String DAILY_SUMMARY = "DAILY_SUMMARY";
    LinearLayout layoutTipsAndSalary;
    LinearLayout layoutSummary;
    LinearLayout target;
    ViewGroup startWorkingLayout;
    ImageButton btnShekel1;
    ImageButton btnShekel2;
    ImageButton btnShekel5;
    ImageButton btnShekel10;
    ImageButton btnShekel20;
    SharedPreferences sharedPreferences;
    int moneyToAdd = 0;
    TextView lblDailyTips;
    TextView lblDailySalary;
    TextView lblDailySummary;
    TextView lblEnterTime;
    TextView txtHiddenText;
    Button btnStartWorking;
    Button btnIncrease;
    Button btnDecrease;
    float salary;
    int dailyTips = 0;
    int dailySalary = 0;
    int dailySummary = 0;
    float salaryPerHour = 25;
    long startTime = 0;
    DBAdapter dbAdapter;
    ArrayList<Shift> allShifts;
    boolean isStartWorking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbAdapter = new DBAdapter(this);
        allShifts = new ArrayList<Shift>();
        layoutTipsAndSalary = (LinearLayout) findViewById(R.id.layoutTipsAndSalary);
        layoutSummary = (LinearLayout) findViewById(R.id.layoutSummary);
        target = (LinearLayout) findViewById(R.id.layoutTarget);
        startWorkingLayout = (ViewGroup) findViewById(R.id.startWorkingLayout);
        sharedPreferences = getSharedPreferences(prefName, MODE_PRIVATE);

        btnShekel1 = (ImageButton) findViewById(R.id.btnShekel1);
        btnShekel2 = (ImageButton) findViewById(R.id.btnShekel2);
        btnShekel5 = (ImageButton) findViewById(R.id.btnShekel5);
        btnShekel10 = (ImageButton) findViewById(R.id.btnShekel10);
        btnShekel20 = (ImageButton) findViewById(R.id.btnShekel20);

        btnStartWorking = (Button) findViewById(R.id.btnStartWorking);
        btnIncrease = (Button) findViewById(R.id.btnIncrease);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);

        txtHiddenText = (TextView) findViewById(R.id.txtHiddenText);
        lblDailyTips = (TextView) findViewById(R.id.lblDailyTips);
        lblDailySalary = (TextView) findViewById(R.id.lblDailySalary);
        lblDailySummary = (TextView) findViewById(R.id.lblDailySummary);
        lblEnterTime = (TextView) findViewById(R.id.lblEnterTime);

        btnStartWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(startWorkingLayout);
                if (!isStartWorking) {
                    dailyTips = sharedPreferences.getInt(DAILY_TIPS,0);
                    startTime = System.currentTimeMillis();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(START_TIME, startTime);
                    editor.putBoolean(IS_START_WORKING, isStartWorking);
                    editor.apply();
                    connectUser();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                            .setTitle(getString(R.string.end_shift_title))
                            .setMessage(getString(R.string.end_shift_message))
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes,
                                    new Dialog.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            long currentStartTime = sharedPreferences.getLong(START_TIME,0);
                                            long endTime = System.currentTimeMillis();
                                            Shift shiftToAdd = new Shift(currentStartTime,endTime,salary,dailyTips);
                                            allShifts.add(shiftToAdd);
                                            writingToDb(shiftToAdd);
                                            Toast.makeText(getBaseContext(), "נכנס למאגר", Toast.LENGTH_LONG).show();
                                            disconnectUser();
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
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyTips++;
                lblDailyTips.setText(String.valueOf(dailyTips) + "₪");
            }
        });
        btnDecrease.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dailyTips = 0;
                lblDailyTips.setText(String.valueOf(dailyTips) + "₪");
                return true;
            }
        });
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dailyTips > 0) {
                    dailyTips--;
                    lblDailyTips.setText(String.valueOf(dailyTips) + "₪");
                }
            }
        });
        target.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                final boolean result = event.getResult();
                if (!result && action != DragEvent.ACTION_DRAG_STARTED)
                    target.setBackground(getResources().getDrawable(R.drawable.shape_target_pressed));
                    //target.setAlpha(0.5f);
                if (!result && action == DragEvent.ACTION_DRAG_EXITED || action == DragEvent.ACTION_DRAG_ENDED)
                    target.setBackground(getResources().getDrawable(R.drawable.shape_target_normal));
                    //target.setAlpha(1f);
                if (result && action == DragEvent.ACTION_DRAG_ENDED) {
                    target.setBackground(getResources().getDrawable(R.drawable.shape_target_normal));
                    //target.setAlpha(1f);
                    dailyTips += moneyToAdd;
                    lblDailyTips.setText(dailyTips + "₪");
                }
                return true;
            }
        });
        btnShekel1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, dragShadowBuilder, null, 0);
                    moneyToAdd = 1;
                    return true;
                }
                return false;
            }
        });
        btnShekel2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, dragShadowBuilder, null, 0);
                    moneyToAdd = 2;
                    return true;
                }
                return false;
            }
        });
        btnShekel5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, dragShadowBuilder, null, 0);
                    moneyToAdd = 5;
                    return true;
                }
                return false;
            }
        });
        btnShekel10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, dragShadowBuilder, null, 0);
                    moneyToAdd = 10;
                    return true;
                }
                return false;
            }
        });
        btnShekel20.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, dragShadowBuilder, null, 0);
                    moneyToAdd = 20;
                    return true;
                }
                return false;
            }
        });
    }

    private void connectUser() {
        //set the button position and text
        isStartWorking = true;
        btnStartWorking.setText("סיים עבודה");
        RelativeLayout.LayoutParams position = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        position.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        btnStartWorking.setLayoutParams(position);

        //set the UI visible
        layoutTipsAndSalary.setVisibility(View.VISIBLE);
        layoutSummary.setVisibility(View.VISIBLE);
        target.setVisibility(View.VISIBLE);
        btnShekel1.setVisibility(View.VISIBLE);
        btnShekel2.setVisibility(View.VISIBLE);
        btnShekel5.setVisibility(View.VISIBLE);
        btnShekel10.setVisibility(View.VISIBLE);
        btnShekel20.setVisibility(View.VISIBLE);
        txtHiddenText.setVisibility(View.INVISIBLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTime));
        lblEnterTime.setText("שעת התחלה " + getHourString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        lblEnterTime.setVisibility(View.VISIBLE);
        lblDailyTips.setText(dailyTips + "₪");
    }

    private void disconnectUser() {
        //set the button position and text
        isStartWorking = false;
        btnStartWorking.setText("התחל עבודה");
        RelativeLayout.LayoutParams position = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        position.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        btnStartWorking.setLayoutParams(position);

        //set the UI Invisible
        lblEnterTime.setText("");
        lblEnterTime.setVisibility(View.INVISIBLE);
        layoutTipsAndSalary.setVisibility(View.INVISIBLE);
        layoutSummary.setVisibility(View.INVISIBLE);
        target.setVisibility(View.INVISIBLE);
        btnShekel1.setVisibility(View.INVISIBLE);
        btnShekel2.setVisibility(View.INVISIBLE);
        btnShekel5.setVisibility(View.INVISIBLE);
        btnShekel10.setVisibility(View.INVISIBLE);
        btnShekel20.setVisibility(View.INVISIBLE);
        txtHiddenText.setVisibility(View.VISIBLE);

        //reset fields
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(START_TIME, 0);
        editor.putInt(DAILY_TIPS, 0);
        editor.apply();

    }

    @Override
    protected void onStart() {
        super.onStart();
        readFromSharedPreferences();
        readFromDb();
        if (isStartWorking && startTime != 0){
            connectUser();
        }else{
            disconnectUser();
        }
        if(dailyTips != 0){
            lblDailyTips.setText(dailyTips + "₪");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        writeToSharedPreferences();
    }

    public void readFromSharedPreferences(){
        isStartWorking = sharedPreferences.getBoolean(IS_START_WORKING, false);
        startTime = sharedPreferences.getLong(START_TIME, 0);
        dailyTips = sharedPreferences.getInt(DAILY_TIPS, 0);
        salary = sharedPreferences.getFloat(SettingsActivity.SALARY_PER_HOURS,25f);
    }

    public void writeToSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DAILY_TIPS,dailyTips);
        editor.putLong(START_TIME, startTime);
        editor.putBoolean(IS_START_WORKING,isStartWorking);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent settingsActivity = new Intent(this,SettingsActivity.class);
                startActivity(settingsActivity);
                break;

            case R.id.action_table:
                Intent recentShifts = new Intent(this,RecentShifts.class);
                recentShifts.putExtra(ALL_SHIFTS, allShifts);
                startActivity(recentShifts);
                break;
            case R.id.action_add_manual_shift:
                Intent createManualShift = new Intent(this,CreateManualShift.class);
                startActivity(createManualShift);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void writingToDb(Shift shift){
        try {
            dbAdapter.open();
            dbAdapter.insertShiftToDB(shift);
            dbAdapter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void readFromDb(){
        try {
            dbAdapter.open();
            Cursor cursor = dbAdapter.getAllShifts();
            allShifts.clear();
            while(cursor.moveToNext()){
                long startTime = cursor.getLong(0);
                long endTime = cursor.getLong(1);
                float salary = cursor.getFloat(3);
                int tips = cursor.getInt(4);
                allShifts.add(new Shift(startTime,endTime,salary,tips));
            }
            dbAdapter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getHourString(int hours, int minutes){
        String hour = String.valueOf(hours);
        String minute = String.valueOf(minutes);
        if(hours<10)
            hour = "0" + hours;
        if(minutes<10)
            minute = "0" + minutes;
        return hour + ":" + minute;
    }
}
