package com.example.mysc.tiper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SettingsActivity extends AppCompatActivity {

    public static final String SALARY_PER_HOURS = "SALARY_PER_HOURS";
    SharedPreferences sharedPreferences;
    EditText txtSalaryPerHour;
    Button btnSaveSettings;
    float salaryPerHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtSalaryPerHour = (EditText) findViewById(R.id.txtSalaryPerHour);
        sharedPreferences = getSharedPreferences(HomeActivity.prefName,MODE_PRIVATE);

        readSharePreferences();
        NumberFormat formatter = new DecimalFormat("#.#");
        txtSalaryPerHour.setText(formatter.format(salaryPerHours));

        btnSaveSettings = (Button) findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String salary = txtSalaryPerHour.getText().toString();
                if(!checkNumberString(salary)){
                    Toast.makeText(getBaseContext(), "אנא וודא שמילאת רק ספרות או נקודה", Toast.LENGTH_SHORT).show();
                    return;
                }
                salaryPerHours = salary.length() == 0 ? 0 : Float.valueOf(salary);
                writeToSharedPreferences();
                finish();
            }
        });
    }

    private void readSharePreferences() {
        salaryPerHours = sharedPreferences.getFloat(SALARY_PER_HOURS,25);
    }

    public void writeToSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(SALARY_PER_HOURS, salaryPerHours);
        editor.apply();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public static boolean checkNumberString (String str){
        boolean thereIsPoint = false;
        for (int i = 0; i < str.length(); i++) {
            int asciiNum = str.charAt(i);
            if(asciiNum < 48 || asciiNum > 57) {
                if(asciiNum == 46){
                    if(thereIsPoint || i==0 || i == str.length()-1)
                        return false;
                    else
                        thereIsPoint = true;
                }else
                    return false;
            }

        }
        return true;
    }
}
