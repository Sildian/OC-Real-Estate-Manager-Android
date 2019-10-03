package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.coremodel.Extra

/**************************************************************************************************
 * DAO for Extra
 *************************************************************************************************/

@Dao
interface ExtraDAO {

    @Query("SELECT * FROM Extra")
    fun getAllExtras(): LiveData<List<Extra>>

    @Insert
    fun insertExtra(extra:Extra)
}