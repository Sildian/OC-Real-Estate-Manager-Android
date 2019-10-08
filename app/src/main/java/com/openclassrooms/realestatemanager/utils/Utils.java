package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
}
