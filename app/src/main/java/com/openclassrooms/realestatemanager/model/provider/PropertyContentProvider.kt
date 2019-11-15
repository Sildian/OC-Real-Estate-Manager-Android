package com.openclassrooms.realestatemanager.model.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.model.sqlite.SQLiteDatabase

/**************************************************************************************************
 * Content provider for Property
 *************************************************************************************************/

class PropertyContentProvider:ContentProvider() {

    companion object{
        const val AUTHORITY="com.openclassrooms.realestatemanager.model.provider"
        val TABLE_NAME= "Property"
        val URI_PROPERTY=Uri.parse("content://"+AUTHORITY+"/"+ TABLE_NAME)
        val TYPE="vnd.android.cursor.item/"+ AUTHORITY+"."+ TABLE_NAME
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        if(context!=null){
            val cursor= SQLiteDatabase.getInstance(context!!).propertyDAO.getAllPropertiesFromContentProvider()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }else{
            throw IllegalArgumentException("Failed to query properties.")
        }
    }

    override fun getType(uri: Uri): String? {
        return TYPE
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}