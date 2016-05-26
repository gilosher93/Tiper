package co.il.tipper.mysc.tipper.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import co.il.tipper.mysc.tipper.R;

/**
 * Created by Mysc on 2.1.2016.
 */
public class EditShiftFragment extends DialogFragment {

    TimePicker endPicker;
    TimePicker startPicker;
    DatePicker datePicker;
    Button btnSaveChanges;
    Button btnCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_shift,container);
        startPicker = (TimePicker) view.findViewById(R.id.startPicker);
        endPicker = (TimePicker) view.findViewById(R.id.endPicker);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSaveChanges = (Button) view.findViewById(R.id.btnSaveChanges);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        return view;
    }
}
