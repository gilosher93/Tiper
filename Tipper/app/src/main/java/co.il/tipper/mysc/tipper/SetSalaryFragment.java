package co.il.tipper.mysc.tipper;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Mysc on 27.12.2015.
 */
public class SetSalaryFragment extends DialogFragment {

    OnSetSalaryListener listener;
    EditText txtSalaryPerHour;
    Button btnSkip;
    Button btnOk;

    public void setFragment(OnSetSalaryListener listener){
        this.listener = listener;
    }

    interface OnSetSalaryListener{
        void onFinish(float salaryPerHour);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_salary,container);
        txtSalaryPerHour = (EditText) view.findViewById(R.id.txtSalaryPerHour);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnSkip = (Button) view.findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String salary = txtSalaryPerHour.getText().toString();
                if(!SettingsActivity.checkNumberString(salary)){
                    Toast.makeText(getActivity(), getString(R.string.vaild_input_number_notice), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFinish(salary.length() == 0 ? 25.0f : Float.valueOf(salary));
                dismiss();
            }
        });
        return view;
    }
}
