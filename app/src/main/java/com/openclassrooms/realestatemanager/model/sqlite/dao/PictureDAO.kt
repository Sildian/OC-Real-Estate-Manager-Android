package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.model.coremodel.Picture

/**************************************************************************************************
 * DAO for Picture
 *************************************************************************************************/

@Dao
interface PictureDAO {

    @Query("SELECT * FROM Picture WHERE propertyId=:propertyId")
    fun getPropertyPictures(propertyId:Int): LiveData<List<Picture>>

    @Insert
    fun insertPropertyPicture(picture:Picture)

    @Query("DELETE FROM Picture WHERE id=:id")
    fun deletePropertyPicture(id:Int)
}