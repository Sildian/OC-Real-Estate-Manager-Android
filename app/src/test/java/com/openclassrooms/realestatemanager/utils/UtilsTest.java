package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
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
}