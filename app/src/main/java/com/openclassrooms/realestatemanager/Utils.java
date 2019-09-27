package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
}
