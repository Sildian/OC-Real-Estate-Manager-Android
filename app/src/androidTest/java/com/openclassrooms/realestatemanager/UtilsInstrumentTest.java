package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsInstrumentTest {

    @Test
    public void given_context_when_isInternetAvailable_then_check_result_true() {
        Context context= InstrumentationRegistry.getContext();
        assertTrue(Utils.isInternetAvailable(context));
    }
}