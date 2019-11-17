package com.openclassrooms.realestatemanager.model.provider

import org.junit.Assert.*
import android.content.ContentResolver
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.model.sqlite.SQLiteDatabase
import org.junit.Before
import org.junit.Test
import android.content.ContentUris

class PropertyContentProviderTest{

    private lateinit var contentResolver: ContentResolver

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                SQLiteDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        this.contentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver()
    }

    @Test
    fun given_nothing_when_query_then_checkResultIsNotNull(){
        val cursor = this.contentResolver.query(ContentUris.withAppendedId(
                PropertyContentProvider.URI_PROPERTY, 1), null, null, null, null)
        assertNotNull(cursor)
        cursor?.close()
    }
}