package com.openclassrooms.realestatemanager;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsInstrumentTest {

    @Test
    public void given_context_when_isInternetAvailable_then_check_result_true() {
        Context context= InstrumentationRegistry.getInstrumentation().getContext();
        assertTrue(Utils.isInternetAvailable(context));
    }
}