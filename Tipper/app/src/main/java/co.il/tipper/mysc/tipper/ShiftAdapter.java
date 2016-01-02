package co.il.tipper.mysc.tipper;

import android.app.Activity;
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

/**
 * Created by Mysc on 21.12.2015.
 */
public class ShiftAdapter extends ArrayAdapter<Shift> {

    private ArrayList<Shift> shifts;
    private Activity activity;

    public ShiftAdapter(Activity context, ArrayList<Shift> shifts) {
        super(context, R.layout.item_shift, shifts);
        activity = context;
        this.shifts = shifts;
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
            LayoutInflater inflater = activity.getLayoutInflater();
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
            viewContainer.txtDate.setText(simpleDateFormat.format(new Date(shift.startTime)));
            viewContainer.txtTimes.setText(Shift.whatTimeIsIt(shift.startTime)+" - "+Shift.whatTimeIsIt(shift.endTime));
            viewContainer.txtSumOfTimes.setText(activity.getResources().getString(R.string.sum_of_hours) + ": " + shift.getSumOfHoursString());
            NumberFormat formatter = new DecimalFormat("#");
            viewContainer.txtTips.setText(activity.getResources().getString(R.string.tips) +": "+ shift.getTipsCount()+"₪");
            viewContainer.txtSalary.setText(activity.getResources().getString(R.string.salary) + ": " + formatter.format(shift.getSalary())+"₪");
            viewContainer.txtSummary.setText(activity.getResources().getString(R.string.total) + ": " + formatter.format(shift.getSummary())+"₪");
            viewContainer.txtAverageSalaryPerHour.setText(activity.getResources().getString(R.string.average_salary) + ": " + formatter.format(shift.getAverageSalaryPerHour())+"₪ " + activity.getResources().getString(R.string.per_hour));
        }
        return rowView;
    }
}
