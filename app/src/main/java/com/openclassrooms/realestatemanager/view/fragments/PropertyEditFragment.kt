package com.openclassrooms.realestatemanager.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.*
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.activities.BaseActivity
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureAdapter
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureViewHolder
import kotlinx.android.synthetic.main.dialog_picture_description_request.view.*
import kotlinx.android.synthetic.main.fragment_property_edit.view.*
import pl.aprilapps.easyphotopicker.*

/**************************************************************************************************
 * Allows the user to create or edit a property
 *************************************************************************************************/

class PropertyEditFragment : PropertyBaseFragment(), PictureViewHolder.Listener {

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object {

        /**Requests for internal calls**/

        const val KEY_REQUEST_PERMISSION_WRITE=101
        const val KEY_REQUEST_PERMISSION_WRITE_AND_CAMERA=102

        /**Permissions**/

        const val KEY_PERMISSION_WRITE=Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val KEY_PERMISSION_CAMERA=Manifest.permission.CAMERA
    }

    /*********************************************************************************************
     * Interface allowing to watch initializations events
     ********************************************************************************************/

    interface InitializationListener{
        fun onInitializationCompleted()
    }

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    /**Text input layouts**/

    private val adTitleInputLayout by lazy {layout.fragment_property_edit_ad_title_layout}
    private val priceInputLayout by lazy {layout.fragment_property_edit_price_layout}
    private val typeInputLayout by lazy {layout.fragment_property_edit_type_layout}
    private val descriptionInputLayout by lazy {layout.fragment_property_edit_description_layout}
    private val sizeInputLayout by lazy {layout.fragment_property_edit_size_layout}
    private val nbRoomsInputLayout by lazy {layout.fragment_property_edit_nb_rooms_layout}
    private val nbBedroomsInputLayout by lazy {layout.fragment_property_edit_nb_bedrooms_layout}
    private val nbBathroomsInputLayout by lazy {layout.fragment_property_edit_nb_bathrooms_layout}
    private val buildYearInputLayout by lazy {layout.fragment_property_edit_build_year_layout}
    private val addressInputLayout by lazy {layout.fragment_property_edit_address_layout}
    private val cityInputLayout by lazy {layout.fragment_property_edit_city_layout}
    private val stateInputLayout by lazy {layout.fragment_property_edit_state_layout}
    private val countryInputLayout by lazy {layout.fragment_property_edit_country_layout}
    private val realtorInputLayout by lazy {layout.fragment_property_edit_realtor_layout}
    private val adDateInputLayout by lazy {layout.fragment_property_edit_ad_date_layout}
    private val saleDateInputLayout by lazy {layout.fragment_property_edit_sale_date_layout}

    /**Components on the screen**/

    private val adTitleText by lazy {layout.fragment_property_edit_ad_title }
    private val priceText by lazy {layout.fragment_property_edit_price}
    private val typeTextDropdown by lazy {layout.fragment_property_edit_type}
    private val picturesRecyclerView by lazy {layout.fragment_property_edit_pictures}
    private val descriptionText by lazy {layout.fragment_property_edit_description}
    private val sizeText by lazy {layout.fragment_property_edit_size}
    private val nbRoomsText by lazy {layout.fragment_property_edit_nb_rooms}
    private val nbBedroomsText by lazy {layout.fragment_property_edit_nb_bedrooms}
    private val nbBathroomsText by lazy {layout.fragment_property_edit_nb_bathrooms}
    private val buildYearText by lazy {layout.fragment_property_edit_build_year}
    private val extrasChipGroup by lazy {layout.fragment_property_edit_extras}
    private val extrasChips=ArrayList<Chip>()
    private val addressText by lazy {layout.fragment_property_edit_address}
    private val cityText by lazy {layout.fragment_property_edit_city}
    private val stateText by lazy {layout.fragment_property_edit_state}
    private val countryText by lazy {layout.fragment_property_edit_country}
    private val realtorTextDropDown by lazy {layout.fragment_property_edit_realtor}
    private val adDateText by lazy {layout.fragment_property_edit_ad_date}
    private val saleInfoLayout by lazy {layout.fragment_property_edit_sale_info}
    private val soldSwitch by lazy {layout.fragment_property_edit_sold}
    private val saleDateText by lazy {layout.fragment_property_edit_sale_date}
    private lateinit var cancelButton :Button                                       //Only on tablets
    private lateinit var saveButton :Button                                         //Only on tablets

    /**Components support**/

    private lateinit var pictureAdapter:PictureAdapter

    /*********************************************************************************************
     * Pictures support
     ********************************************************************************************/

    private lateinit var easyImage:EasyImage                            //EasyImage support allowing to pick pictures on the device
    private val deletePicturesIdsTag:ArrayList<Int> =arrayListOf()      //Tags allowing to delete pictures from the database

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        /*Initializes all the UI components*/

        initializeTypeTextDropdown(object:InitializationListener{
            override fun onInitializationCompleted() {
                initializePicturesRecyclerView()
                initializeExtrasChipGroup(object:InitializationListener{
                    override fun onInitializationCompleted() {
                        initializeRealtorTextDropdown(object:InitializationListener{
                            override fun onInitializationCompleted() {
                                initializeAdDateText()
                                initializeSaleInfo()
                                initializeButtons()

                                /*Initializes pictures support items*/

                                initializeEasyImage()

                                /*If a property id exists, then loads the property's data*/

                                if(propertyId!=null) loadProperty()
                            }
                        })
                    }
                })
            }
        })
        return this.layout
    }

    /*********************************************************************************************
     * PropertyBaseFragment
     ********************************************************************************************/

    override fun getLayoutId() = R.layout.fragment_property_edit

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeTypeTextDropdown(listener:InitializationListener){
        this.propertyTypeViewModel.getAllPropertyTypes().observe(this, Observer{
            initializeTextDropDown(this.typeTextDropdown, R.layout.dropdown_menu_standard, it)
            listener.onInitializationCompleted()
        })
    }

    private fun initializePicturesRecyclerView(){
        this.pictureAdapter= PictureAdapter(this.pictures, true, this)
        this.picturesRecyclerView.adapter=this.pictureAdapter
        this.picturesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun initializeExtrasChipGroup(listener:InitializationListener){
        this.extraViewModel.gelAllExtra().observe(this, Observer{
            initializeChipGroup(this.extrasChipGroup, this.extrasChips, R.layout.chip_standard, it)
            listener.onInitializationCompleted()
        })
    }

    private fun initializeRealtorTextDropdown(listener:InitializationListener){
        this.realtorViewModel.getAllRealtors().observe(this, Observer{
            initializeTextDropDown(this.realtorTextDropDown, R.layout.dropdown_menu_standard, it)
            listener.onInitializationCompleted()
        })
    }

    private fun initializeAdDateText(){
        initializeDateText(this.adDateText)
    }

    private fun initializeSaleInfo(){
        if(this.propertyId==null){
            this.saleInfoLayout.visibility=View.GONE
        }else {
            initializeDateText(this.saleDateText)
            this.soldSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    this.saleDateInputLayout.setBoxBackgroundColorResource(android.R.color.transparent)
                }else{
                    this.saleDateText.text=null
                    this.saleDateInputLayout.setBoxBackgroundColorResource(R.color.colorGray)
                }
            }
        }
    }

    @Suppress("PLUGIN_WARNING")
    private fun initializeButtons(){
        if(this.layout.fragment_property_edit_button_cancel!=null&&this.layout.fragment_property_edit_button_save!=null){
            this.cancelButton=this.layout.fragment_property_edit_button_cancel
            this.saveButton=this.layout.fragment_property_edit_button_save
            this.cancelButton.setOnClickListener { finish(this.propertyId) }
            this.saveButton.setOnClickListener { saveProperty() }
        }
    }

    private fun initializeEasyImage(){
        this.easyImage= EasyImage.Builder(context!!)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .allowMultiple(true)
                .build()
    }

    /*********************************************************************************************
     * Listens UI events on picturesRecyclerView
     ********************************************************************************************/

    override fun onPictureClick(pictures: List<Picture?>, position: Int) {
        //Nothing
    }

    override fun onDeletePictureButtonClick(position: Int) {
        removePicture(position)
    }

    override fun onAddPictureButtonClick(position: Int) {
        requestWrite()
    }

    override fun onTakePictureButtonClick(position: Int) {
        requestCamera()
    }

    /*********************************************************************************************
     * Pictures management
     ********************************************************************************************/

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    private fun requestPictureDescription(picturePath:String){

        /*Prepares the UI for the dialog*/

        val dialogView=requireActivity().layoutInflater
                .inflate(R.layout.dialog_picture_description_request, null)
        val pictureDescriptionPicture=dialogView.dialog_picture_description_request_picture
        val pictureDescriptionText=dialogView.dialog_picture_description_request_text
        pictureDescriptionPicture.setImageURI(picturePath.toUri())

        /*Shows the dialog requesting a description for the picture*/

        val dialog = AlertDialog.Builder(activity!!)
                .setTitle(resources.getString(R.string.dialog_title_input))
                .setMessage(resources.getString(R.string.dialog_message_input_picture_description))
                .setView(dialogView)
                .setPositiveButton(
                        R.string.dialog_button_validate
                ) { dialogPositive, which ->

                    /*Once the positive button is clicked, adds the picture to the property*/

                    val pictureDescription=pictureDescriptionText.text.toString()
                    addPicture(Picture(path=picturePath, description = pictureDescription))
                }
                .setNegativeButton(
                        R.string.dialog_button_cancel
                ) { dialogNegative, which -> }
                .create()
        dialog.show()
    }

    private fun addPicture(picture:Picture){
        this.pictures.add(this.pictures.size-1, picture)
        this.pictureAdapter.notifyDataSetChanged()
    }

    private fun removePicture(position:Int){
        this.deletePicturesIdsTag.add(this.pictures[position]?.id!!)
        this.pictures.removeAt(position)
        this.pictureAdapter.notifyDataSetChanged()
    }

    /*********************************************************************************************
     * Input control
     ********************************************************************************************/

    /**Checks that all input fields are valid**/

    private fun checkInputIsValid():Boolean{

        var isValid=true

        if(!checkTextIsNotEmpty(this.adTitleText, this.adTitleInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.priceText, this.priceInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.typeTextDropdown, this.typeInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.descriptionText, this.descriptionInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.sizeText, this.sizeInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.nbRoomsText, this.nbRoomsInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.nbBedroomsText, this.nbBedroomsInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.nbBathroomsText, this.nbBathroomsInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.buildYearText, this.buildYearInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.addressText, this.addressInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.cityText, this.cityInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.stateText, this.stateInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.countryText, this.countryInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.realtorTextDropDown, this.realtorInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.adDateText, this.adDateInputLayout)) isValid=false
        if(!checkSaleDateIsValid()) isValid=false

        return isValid
    }

    /**Checks that a simple text field is not empty**/

    private fun checkTextIsNotEmpty(editText:TextInputEditText, textLayout:TextInputLayout):Boolean{
        if(editText.text.isNullOrEmpty()) {
            textLayout.error = resources.getString(R.string.error_mandatory_field)
            return false
        }else{
            textLayout.error=null
            return true
        }
    }

    /**Checks that a dropDown text field is not empy**/

    private fun checkTextIsNotEmpty(editText:AutoCompleteTextView, textLayout:TextInputLayout):Boolean{
        if(editText.text.isNullOrEmpty()) {
            textLayout.error = resources.getString(R.string.error_mandatory_field)
            return false
        }else{
            textLayout.error=null
            return true
        }
    }

    /**Specific function checking that saleDate is valid (must be filled only if the property is sold)**/

    private fun checkSaleDateIsValid():Boolean{
        when{
            this.soldSwitch.isChecked&&this.saleDateText.text.isNullOrEmpty()-> {
                this.saleDateInputLayout.error = resources.getString(R.string.error_mandatory_sale_date)
                return false
            }
            !this.soldSwitch.isChecked&&!this.saleDateText.text.isNullOrEmpty()->{
                this.saleDateInputLayout.error = resources.getString(R.string.error_forbidden_sale_date)
                return false
            }
            else ->
                return true
        }
    }

    /*********************************************************************************************
     * Saves property's items
     ********************************************************************************************/

    fun saveProperty(){

        /*Checks that all input fields are valid and eventually shows a message*/

        if(!checkInputIsValid()){
            (activity as BaseActivity).showErrorDialog(
                    resources.getString(R.string.dialog_title_validation_issue),
                    resources.getString(R.string.dialog_message_input_not_valid))
        }else {

            /*Saves all fields into a property*/

            val property = Property()
            property.adTitle = this.adTitleText.text.toString()
            property.price = Integer.parseInt(this.priceText.text.toString())
            property.typeId = (this.typeTextDropdown.tag as PropertyType).id
            if(this.pictures.isNotEmpty()) property.mainPicturePath=this.pictures[0]?.path
            property.description = this.descriptionText.text.toString()
            property.size = Integer.parseInt(this.sizeText.text.toString())
            property.nbRooms = Integer.parseInt(this.nbRoomsText.text.toString())
            property.nbBedrooms = Integer.parseInt(this.nbBedroomsText.text.toString())
            property.nbBathrooms = Integer.parseInt(this.nbBathroomsText.text.toString())
            property.buildYear = Integer.parseInt(this.buildYearText.text.toString())
            property.address = this.addressText.text.toString()
            property.city = this.cityText.text.toString()
            property.state = this.stateText.text.toString()
            property.country = this.countryText.text.toString()
            property.realtorId = (this.realtorTextDropDown.tag as Realtor).id
            property.adDate = Utils.getDateFromString(this.adDateText.text.toString())
            property.sold = this.soldSwitch.isChecked
            if (!this.saleDateText.text.isNullOrEmpty())
                property.saleDate = Utils.getDateFromString(this.saleDateText.text.toString())

            /*If this is a new property then creates it in the database. Else updates it.*/

            if (this.propertyId == null) {
                this.propertyId = this.propertyViewModel.insertProperty(property).toInt()
                property.id=this.propertyId
            } else {
                property.id = this.propertyId
                this.propertyViewModel.updateProperty(property)
            }
            val propertyId = this.propertyId
            savePropertyExtras(propertyId!!)
            savePropertyPictures(propertyId)

            /*Leaves the fragment*/

            if(activity is MainActivity){
                (activity as MainActivity).showSnackbar(resources.getString(R.string.toast_message_property_saved))
            }else {
                activity!!.setResult(Activity.RESULT_OK)
            }
            finish(propertyId)
        }
    }

    private fun savePropertyExtras(propertyId:Int){
        this.propertyViewModel.deletePropertyExtra(propertyId)
        val extrasIds=getIdsFromChips(this.extrasChips, Extra::class.java)
        for(extraId in extrasIds){
            if(extraId!=null) {
                this.propertyViewModel.insertPropertyExtra(propertyId, extraId)
            }
        }
    }

    private fun savePropertyPictures(propertyId:Int){
        this.deletePicturesIdsTag.forEach {
            this.propertyViewModel.deletePropertyPicture(it)
        }
        this.pictures.forEach {
            if(it?.id==null&&it?.path!=null){
                val pictureToInsert=Picture(path=it.path, description=it.description, propertyId=propertyId)
                this.propertyViewModel.insertPropertyPicture(pictureToInsert)
            }
        }
    }

    /*********************************************************************************************
     * Loads property's items
     ********************************************************************************************/

    private fun loadProperty() {

        this.propertyViewModel.getProperty(this.propertyId!!.toInt()).observe(this, Observer {
            val property = it
            this.adTitleText.setText(property.adTitle)
            this.priceText.setText(property.price.toString())
            val typeId = property.typeId
            if (typeId != null) loadPropertyType(typeId)
            this.descriptionText.setText(property.description)
            this.sizeText.setText(property.size.toString())
            this.nbRoomsText.setText(property.nbRooms.toString())
            this.nbBedroomsText.setText(property.nbBedrooms.toString())
            this.nbBathroomsText.setText(property.nbBathrooms.toString())
            this.buildYearText.setText(property.buildYear.toString())
            this.addressText.setText(property.address)
            this.cityText.setText(property.city)
            this.stateText.setText(property.state)
            this.countryText.setText(property.country)
            val realtorId = property.realtorId
            if (realtorId != null) loadPropertyRealtor(realtorId)
            this.adDateText.setText(Utils.getStringFromDate(property.adDate))
            this.soldSwitch.isChecked = property.sold
            if (property.saleDate != null) {
                this.saleDateText.setText(Utils.getStringFromDate(property.saleDate))
                this.saleDateInputLayout.setBoxBackgroundColorResource(android.R.color.transparent)
            }
        })

        loadPropertyExtras(propertyId!!)
        loadPropertyPictures(propertyId!!)
    }

    private fun loadPropertyType(typeId:Int){
        val propertyType=this.typeTextDropdown.adapter.getItem(typeId-1)
        this.typeTextDropdown.setText(propertyType.toString(), false)
        this.typeTextDropdown.tag=propertyType
    }

    private fun loadPropertyPictures(propertyId:Int){
        this.propertyViewModel.getPropertyPictures(propertyId).observe(this, Observer {
            this.pictures.clear()
            this.pictures.addAll(it)
            this.pictures.add(null)
            this.pictureAdapter.notifyDataSetChanged()
        })
    }

    private fun loadPropertyExtras(propertyId:Int){
        this.propertyViewModel.getPropertyExtras(propertyId).observe(this, Observer {
            for(extra in it){
                this.extrasChips[extra.extraId-1].isChecked=true
            }
        })
    }

    private fun loadPropertyRealtor(realtorId:Int){
        this.realtorViewModel.getRealtor(realtorId).observe(this, Observer{
            this.realtorTextDropDown.setText(it.toString(), false)
            this.realtorTextDropDown.tag=it
        })
    }

    /*********************************************************************************************
     * Permissions management
     ********************************************************************************************/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            KEY_REQUEST_PERMISSION_WRITE->if(grantResults.isNotEmpty()){
                when(grantResults[0]){
                    PackageManager.PERMISSION_GRANTED -> startAddPictureIntent()
                    PackageManager.PERMISSION_DENIED -> Log.d("TAG_PERMISSION", "Permission denied")
                }
            }
            KEY_REQUEST_PERMISSION_WRITE_AND_CAMERA -> if (grantResults.isNotEmpty()) {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> startTakePictureIntent()
                    PackageManager.PERMISSION_DENIED -> Log.d("TAG_PERMISSION", "Permission denied")
                }
            }
        }
    }

    private fun requestWrite(){
        if(Build.VERSION.SDK_INT>=23
                &&activity!!.checkSelfPermission(KEY_PERMISSION_WRITE)!= PackageManager.PERMISSION_GRANTED){

            if(shouldShowRequestPermissionRationale(KEY_PERMISSION_WRITE)){

                (activity as BaseActivity).showInfoDialog(
                        resources.getString(R.string.dialog_title_permission_request),
                        resources.getString(R.string.dialog_message_permission_request_write))

            }else{
                requestPermissions(arrayOf(KEY_PERMISSION_WRITE), KEY_REQUEST_PERMISSION_WRITE)
            }
        }else{
            startAddPictureIntent()
        }
    }

    private fun requestCamera(){
        if(Build.VERSION.SDK_INT>=23
                &&(activity!!.checkSelfPermission(KEY_PERMISSION_WRITE)!= PackageManager.PERMISSION_GRANTED
                        ||activity!!.checkSelfPermission(KEY_PERMISSION_CAMERA)!= PackageManager.PERMISSION_GRANTED)){

            if(shouldShowRequestPermissionRationale(KEY_PERMISSION_WRITE)
                            ||shouldShowRequestPermissionRationale(KEY_PERMISSION_CAMERA)){

                (activity as BaseActivity).showInfoDialog(
                        resources.getString(R.string.dialog_title_permission_request),
                        resources.getString(R.string.dialog_message_permission_request_write_and_camera))

            }else{
                requestPermissions(
                        arrayOf(KEY_PERMISSION_WRITE, KEY_PERMISSION_CAMERA),
                        KEY_REQUEST_PERMISSION_WRITE_AND_CAMERA)
            }
        }else{
            startTakePictureIntent()
        }
    }

    /*********************************************************************************************
     * Intents management
     ********************************************************************************************/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleNewPictureResult(requestCode, resultCode, data)
    }

    fun startAddPictureIntent(){
        this.easyImage.openGallery(this)
    }

    fun startTakePictureIntent(){
        this.easyImage.openCameraForImage(this)
    }

    private fun handleNewPictureResult(requestCode: Int, resultCode: Int, data: Intent?){

        this.easyImage.handleActivityResult(requestCode, resultCode, data, activity!!, object: DefaultCallback(){

            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                for(image in imageFiles){
                    requestPictureDescription(image.file.toURI().path)
                }
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                super.onImagePickerError(error, source)
                Log.d("TAG_PICTURE", error.message)
                (activity as BaseActivity).showErrorDialog(
                        resources.getString(R.string.dialog_title_sundry_issue),
                        resources.getString(R.string.dialog_message_picture_not_loaded)
                )
            }
        })
    }

    /*********************************************************************************************
     * Leaves the fragment
     ********************************************************************************************/

    private fun finish(propertyId:Int?){
        if(this.layout.fragment_property_edit_button_cancel!=null&&this.layout.fragment_property_edit_button_save!=null){
            if(this.propertyId!=null){
                (activity as MainActivity).openPropertyDetail(propertyId!!)
            }else{
                (activity as MainActivity).resetSecondFragment()
            }
        }else{
            activity!!.finish()
        }
    }
}
