package com.openclassrooms.realestatemanager.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        Locale.setDefault(Locale.US);
        String dateString="10/5/19";
        Date date=Utils.getDateFromString(Utils.getDate(2019, 9, 5));
        assertEquals(date, Utils.getDateFromString(dateString));
    }

    @Test
    public void given_5oct2019_when_getStringFromDate_then_check_result_5oct2019(){
        Locale.setDefault(Locale.US);
        Date date=Utils.getDateFromString(Utils.getDate(2019, 9, 5));
        String dateString="10/5/19";
        assertEquals(dateString, Utils.getStringFromDate(date));
    }

    @Test
    public void given_5oct2019_when_getDate_then_check_result_5oct2019(){
        Locale.setDefault(Locale.US);
        int year=2019;
        int month=9;
        int day=5;
        String date="10/5/19";
        assertEquals(date, Utils.getDate(year, month, day));
    }

    @Test
    public void given_5oct20191year3months2days_when_offsetDate_then_check_result_3jul2018(){
        Date actualDate=Utils.getDateFromString(Utils.getDate(2019, 9, 5));
        Date expectedDate=(Utils.getDateFromString(Utils.getDate(2018, 6, 3)));
        assertEquals(expectedDate, Utils.offsetDate(actualDate, -1, -3, -2));
    }

    @Test
    public void given_5oct2019and5Jun2019_when_calculateDifferenceBetweenDates_then_check_result_3(){
        Date firstDate=Utils.getDateFromString(Utils.getDate(2019, 9, 5));
        Date secondDate=Utils.getDateFromString(Utils.getDate(2019, 6, 5));
        assertEquals(3, Utils.calculateDifferenceBetweenDates(firstDate, secondDate));
    }

    @Test
    public void given_1000000_when_getFormatedFigure_then_check_result_1000000(){
        Locale.setDefault(Locale.US);
        assertEquals("1,000,000", Utils.getFormatedFigure(1000000));
    }
}