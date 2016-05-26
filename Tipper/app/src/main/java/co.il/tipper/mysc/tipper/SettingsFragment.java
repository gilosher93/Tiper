package co.il.tipper.mysc.tipper;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SettingsFragment extends Fragment {

    SharedPreferences sharedPreferences;
    EditText txtSalaryPerHour;
    Button btnSaveSettings;
    Switch switchSounds;
    float salaryPerHours;
    boolean sounds = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        switchSounds = (Switch) view.findViewById(R.id.switchSound);
        txtSalaryPerHour = (EditText) view.findViewById(R.id.txtSalaryPerHour);
        sharedPreferences = getActivity().getSharedPreferences(HomeActivity.prefName, Context.MODE_PRIVATE);

        readSharePreferences();
        NumberFormat formatter = new DecimalFormat("#.#");
        txtSalaryPerHour.setText(formatter.format(salaryPerHours));
        switchSounds.setChecked(sounds);

        btnSaveSettings = (Button) view.findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String salary = txtSalaryPerHour.getText().toString();
                if(!checkNumberString(salary)){
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.vaild_input_number_notice), Toast.LENGTH_SHORT).show();
                    return;
                }
                salaryPerHours = salary.length() == 0 ? 0 : Float.valueOf(salary);
                sounds = switchSounds.isChecked();
                writeToSharedPreferences();
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void readSharePreferences() {
        salaryPerHours = sharedPreferences.getFloat(HomeActivity.SALARY,25.0f);
        sounds = sharedPreferences.getBoolean(HomeActivity.SOUNDS,false);
    }

    public void writeToSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(HomeActivity.SALARY, salaryPerHours);
        editor.putBoolean(HomeActivity.SOUNDS,sounds);
        editor.apply();
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
