package co.il.tipper.mysc.tipper.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.il.tipper.mysc.tipper.R;

/**
 * Created by Mysc on 2.1.2016.
 */
public class ShiftOptionFragment extends DialogFragment {

    OnSelectShiftOptionListener listener;
    Button btnEdit;
    Button btnDelete;

    public void setFragment(OnSelectShiftOptionListener listener){
        this.listener = listener;
    }

    public interface OnSelectShiftOptionListener{
        void editShift();
        void deleteShift();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shift_option,container);
        btnEdit = (Button) view.findViewById(R.id.btnEditShift);
        btnDelete = (Button) view.findViewById(R.id.btnDeleteShift);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editShift();
                dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteShift();
                dismiss();
            }
        });
        return view;
    }
}
