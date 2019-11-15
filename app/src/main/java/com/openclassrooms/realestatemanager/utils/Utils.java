package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**************************************************************************************************
 * Utilities
 *************************************************************************************************/

public class Utils {

    /**Converts dollars to euros
     * @param dollars : dollars
     * @return euros
     */

    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**Converts euros to dollars
     * @param euros : euros
     * @return dollars
     */

    public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros / 0.812);
    }

    /**Gets the current date (format 'dd/MM/YYYY')
     * @return the date as a String
     */

    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        return dateFormat.format(new Date());
    }

    /**Checks Internet connection
     * @param context : context
     * @return true if Internet is available, false otherwise
     */

    public static Boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager!=null?connectivityManager.getActiveNetworkInfo():null;
        return networkInfo!=null&&networkInfo.isAvailable();
    }

    /**Gets a Date from a String
     * @param inputDate : the input date (String)
     * @return the output date (Date)
     */

    public static Date getDateFromString(String inputDate){
        DateFormat dateFormat=SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        Date outputDate=new Date();
        try {
            outputDate = dateFormat.parse(inputDate);
        }
        catch(ParseException e){
            Log.d("TAG_DATE", e.getMessage());
        }
        return outputDate;
    }

    /**Gets a String from a Date
     * @param inputDate : the input date (Date)
     * @return the output date (String)
     */

    public static String getStringFromDate(Date inputDate){
        DateFormat dateFormat=SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return dateFormat.format(inputDate);
    }

    /**Gets a date as a String from given year, month and day
     * @param year : the year
     * @param month : the month
     * @param day : the day
     * @return the date (String)
     */

    public static String getDate(int year, int month, int day){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        DateFormat dateFormat=SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return dateFormat.format(calendar.getTime());
    }

    /**Offsets a date with the given year, month and day
     * @param inputDate : the input date
     * @param yearOffset : the number of years to offset (+ : future, - : past)
     * @param monthOffset: the number of months to offset (+ : future, - : past)
     * @param dayOffset: the number of days to offset (+ : future, - : past)
     * @return the resulted date
     */

    public static Date offsetDate(Date inputDate, int yearOffset, int monthOffset, int dayOffset){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.YEAR, yearOffset);
        calendar.add(Calendar.MONTH, monthOffset);
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);
        return calendar.getTime();
    }

    /**Calculates the difference between two dates in months
     * @param firstDate : the first date
     * @param secondDate : the second date
     * @return the difference in months
     */

    public static int calculateDifferenceBetweenDates(Date firstDate, Date secondDate){
        long diffMilliseconds=firstDate.getTime()-secondDate.getTime();
        int diffDays=(int)(diffMilliseconds/86400000);
        int diffMonths=diffDays/30;
        return diffMonths;
    }

    /**Gets a formated figure with thousands separators and currency ($)
     * @param figure : the figure (int)
     * @return the formated figure (String)
     */

    public static String getFormatedFigure(long figure){
        NumberFormat numberFormat=DecimalFormat.getCurrencyInstance();
        numberFormat.setCurrency(Currency.getInstance(Locale.US));
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(figure);
    }

    /**Gets a formated figure with thousands, decimal separators and currency ($)
     * @param figure : the figure (double)
     * @return the formated figure (String)
     */

    public static String getFormatedFigure(double figure){
        NumberFormat numberFormat=DecimalFormat.getCurrencyInstance();
        numberFormat.setCurrency(Currency.getInstance(Locale.US));
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(figure);
    }
}
