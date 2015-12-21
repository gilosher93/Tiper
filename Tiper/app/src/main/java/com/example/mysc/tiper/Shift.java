package com.example.mysc.tiper;

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
    long startTime;
    long endTime;
    float salaryPerHour;
    int tipsCount;

    public Shift(long startTime, long endTime, float salaryPerHour, int tipsCount){
        this.startTime = startTime;
        this.endTime = endTime;
        this.salaryPerHour = salaryPerHour;
        this.tipsCount = tipsCount;
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

    public static int distanceMinutes(long startTime, long endTime){
        String start = whatTimeIsIt(startTime);
        String end = whatTimeIsIt(endTime);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date d1 = null,d2 = null;
        try {
            d1 = format.parse(start);
            d2 = format.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int)(((d2.getTime() - d1.getTime())/1000)/60); //distance of minutes;
    }


    public static String whatTimeIsIt (Long time){
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return dateFormat.format(date);
    }

    public float getSalary(){
        return getSumOfHours() * salaryPerHour ;
    }

    public float getSumOfHours(){
        int distance = distanceMinutes(startTime, endTime);
        int numberOfHours = distance / 60;
        int numberOfMinutes = distance % 60;
        return (float)numberOfMinutes / 60 + numberOfHours;
    }

    public String getSumOfHoursString(){
        int distance = distanceMinutes(startTime, endTime);
        int numberOfHours = distance / 60;
        int numberOfMinutes = distance % 60;
        return HomeActivity.getHourString(numberOfHours,numberOfMinutes);
    }

    public static long getFullDateInLong(String dateInString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        Date d = null;
        try {
            d = df.parse(dateInString);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return d.getTime();
    }

    public float getSummary(){
        return getSalary() + tipsCount;
    }
}
