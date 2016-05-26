package co.il.tipper.mysc.tipper.objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mysc on 21.12.2015.
 */
public class Shift implements Serializable {
    int id;
    long startTime;
    long endTime;
    float salaryPerHour;
    int tipsCount;

    public Shift(long startTime, long endTime, float salaryPerHour, int tipsCount, int id) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.salaryPerHour = salaryPerHour;
        this.tipsCount = tipsCount;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(float salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }

    public int getTipsCount() {
        return tipsCount;
    }

    public void setTipsCount(int tipsCount) {
        this.tipsCount = tipsCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public static int distanceMinutes(long startTime, long endTime) {
        int distance = (int) (((endTime - startTime) / 1000) / 60);
        if (distance < 0)
            return distanceMinutes(startTime, endTime + 24 * 60 * 60 * 1000);
        return distance;
    }


    public float getSalary() {
        return getSumOfHours() * salaryPerHour;
    }

    public float getSummary() {
        return getSalary() + tipsCount;
    }

    public float getAverageSalaryPerHour(){
        return getSumOfHours() > 1 ? (getSummary() == 0 ? 0 : getSummary()/getSumOfHours()): 0;
    }

    public float getSumOfHours() {
        int distance = distanceMinutes(startTime, endTime);
        int numberOfHours = distance / 60;
        int numberOfMinutes = distance % 60;
        return (float) numberOfMinutes / 60 + numberOfHours;
    }

    public String getSumOfHoursString() {
        int distance = distanceMinutes(startTime, endTime);
        int numberOfHours = distance / 60;
        int numberOfMinutes = distance % 60;
        return getHourString(numberOfHours, numberOfMinutes);
    }

    public static String getHourString(int hours, int minutes) {
        String hour = String.valueOf(hours);
        String minute = String.valueOf(minutes);
        if (hours < 10)
            hour = "0" + hours;
        if (minutes < 10)
            minute = "0" + minutes;
        return hour + ":" + minute;
    }

    public static String getHourString(long hourInLong) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(hourInLong));
        return getHourString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static long getFullDateInLong(String dateInString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        Date d = null;
        try {
            d = df.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.getTime();
    }

    public static long getLongByHour(int hour, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        String dateToParse = String.valueOf(calendar.get(Calendar.YEAR))+"-"+String.valueOf(calendar.get(Calendar.MONTH)+1)
                +"-"+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+", " + getHourString(hour, minutes);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        Date d = null;
        try {
            d = df.parse(dateToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.getTime();
    }

    public static String whatTimeIsIt(Long time) {
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return whatTimeIsIt(startTime) + "-" + whatTimeIsIt(endTime) + ", tips = " + tipsCount + ", salaryPerHour = " + salaryPerHour;
    }
}