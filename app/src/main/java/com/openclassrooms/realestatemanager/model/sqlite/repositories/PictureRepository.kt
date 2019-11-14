package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.Picture
import com.openclassrooms.realestatemanager.model.sqlite.dao.PictureDAO

/**************************************************************************************************
 * Repository for Picture
 *************************************************************************************************/

class PictureRepository (val pictureDAO:PictureDAO) {

    fun getPropertyPictures(propertyId:Int): LiveData<List<Picture>> =
            this.pictureDAO.getPropertyPictures(propertyId)

    fun insertPropertyPicture(picture:Picture){
        this.pictureDAO.insertPropertyPicture(picture)
    }

    fun deletePropertyPicture(id:Int){
        this.pictureDAO.deletePropertyPicture(id)
    }
}