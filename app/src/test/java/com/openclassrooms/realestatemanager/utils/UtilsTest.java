package com.openclassrooms.realestatemanager.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void given_1000_when_convertDollarToEuro_then_check_result_812() {
        assertEquals(812, Utils.convertDollarToEuro(1000));
    }

    @Test
    public void given_1000_when_convertEuroToDollar_then_check_result_1232() {
        assertEquals(1232, Utils.convertEuroToDollar(1000));
    }

    @Test
    public void given_nothing_when_getTodayDate_then_check_result_today() {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/YYYY");
        String date=format.format(new Date());
        assertEquals(date, Utils.getTodayDate());
    }

    @Test
    public void given_5oct2019_when_getDateFromString_then_check_result_5oct2019(){
        String dateString="05/10/2019";
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 5);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        assertEquals(date, Utils.getDateFromString(dateString));
    }

    @Test
    public void given_5oct2019_when_getStringFromDate_then_check_result_5oct2019(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 5);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        String dateString="05/10/19";
        assertEquals(dateString, Utils.getStringFromDate(date));
    }

    @Test
    public void given_5oct2019_when_getDate_then_check_result_5oct2019(){
        int year=2019;
        int month=9;
        int day=5;
        String date="05/10/19";
        assertEquals(date, Utils.getDate(year, month, day));
    }
}