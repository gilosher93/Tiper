package co.il.tipper.mysc.tipper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.il.tipper.mysc.tipper.R;
import co.il.tipper.mysc.tipper.objects.Shift;

/**
 * Created by Mysc on 21.12.2015.
 */
public class ShiftAdapter extends ArrayAdapter<Shift> {

    private ArrayList<Shift> shifts;
    private Context context;
    private LayoutInflater inflater;

    public ShiftAdapter(Context context, ArrayList<Shift> shifts) {
        super(context, R.layout.item_shift, shifts);
        this.context = context;
        this.shifts = shifts;
        inflater = LayoutInflater.from(context);

    }
    static class ViewContainer{
        TextView txtTimes;
        TextView txtSumOfTimes;
        TextView txtSummary;
        TextView txtSalary;
        TextView txtTips;
        TextView txtDate;
        TextView txtAverageSalaryPerHour;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewContainer viewContainer;
        View rowView = convertView;
        if (rowView == null) {
            viewContainer = new ViewContainer();
            rowView = inflater.inflate(R.layout.item_shift, null, true);
            viewContainer.txtTimes = (TextView) rowView.findViewById(R.id.txtTimes);
            viewContainer.txtSumOfTimes = (TextView) rowView.findViewById(R.id.txtSumOfTimes);
            viewContainer.txtSummary = (TextView) rowView.findViewById(R.id.txtSummary);
            viewContainer.txtSalary = (TextView) rowView.findViewById(R.id.txtSalary);
            viewContainer.txtTips = (TextView) rowView.findViewById(R.id.txtTips);
            viewContainer.txtDate = (TextView) rowView.findViewById(R.id.txtDate);
            viewContainer.txtAverageSalaryPerHour = (TextView) rowView.findViewById(R.id.txtAverageSalaryPerHour);
            rowView.setTag(viewContainer);
        } else {
            viewContainer = (ViewContainer) rowView.getTag();
        }
        if (shifts != null && position < shifts.size()){
            Shift shift = shifts.get(position);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
            viewContainer.txtDate.setText(simpleDateFormat.format(new Date(shift.getStartTime())));
            viewContainer.txtTimes.setText(Shift.whatTimeIsIt(shift.getStartTime())+" - "+Shift.whatTimeIsIt(shift.getEndTime()));
            viewContainer.txtSumOfTimes.setText(context.getResources().getString(R.string.sum_of_hours) + ": " + shift.getSumOfHoursString());
            NumberFormat formatter = new DecimalFormat("#");
            viewContainer.txtTips.setText(context.getResources().getString(R.string.tips) +": "+ shift.getTipsCount()+"₪");
            viewContainer.txtSalary.setText(context.getResources().getString(R.string.salary) + ": " + formatter.format(shift.getSalary())+"₪");
            viewContainer.txtSummary.setText(context.getResources().getString(R.string.total) + ": " + formatter.format(shift.getSummary())+"₪");
            viewContainer.txtAverageSalaryPerHour.setText(context.getResources().getString(R.string.average_salary) + ": " + formatter.format(shift.getAverageSalaryPerHour())+"₪ " + context.getResources().getString(R.string.per_hour));
        }
        return rowView;
    }
}
