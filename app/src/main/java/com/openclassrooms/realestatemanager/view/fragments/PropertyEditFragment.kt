package com.openclassrooms.realestatemanager.view.fragments

import android.Manifest
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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.activities.BaseActivity
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureAdapter
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureViewHolder
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

        const val KEY_REQUEST_PERMISSION_WRITE_AND_CAMERA=101

        /**Permissions**/

        const val KEY_PERMISSION_WRITE=Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val KEY_PERMISSION_CAMERA=Manifest.permission.CAMERA
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
    private val postalCodeInputLayout by lazy {layout.fragment_property_edit_postal_code_layout}
    private val cityInputLayout by lazy {layout.fragment_property_edit_city_layout}
    private val countryInputLayout by lazy {layout.fragment_property_edit_country_layout}
    private val realtorInputLayout by lazy {layout.fragment_property_edit_realtor_layout}
    private val adDateInputLayout by lazy {layout.fragment_property_edit_ad_date_layout}
    private val saleDateInputLayout by lazy {layout.fragment_property_edit_sale_date_layout}

    /**Other components**/

    private lateinit var pictureAdapter:PictureAdapter
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
    private val postalCodeText by lazy {layout.fragment_property_edit_postal_code}
    private val cityText by lazy {layout.fragment_property_edit_city}
    private val countryText by lazy {layout.fragment_property_edit_country}
    private val realtorTextDropDown by lazy {layout.fragment_property_edit_realtor}
    private val adDateText by lazy {layout.fragment_property_edit_ad_date}
    private val saleInfoLayout by lazy {layout.fragment_property_edit_sale_info}
    private val soldSwitch by lazy {layout.fragment_property_edit_sold}
    private val saleDateText by lazy {layout.fragment_property_edit_sale_date}
    private lateinit var cancelButton :Button
    private lateinit var saveButton :Button

    /*********************************************************************************************
     * Pictures support
     ********************************************************************************************/

    private lateinit var easyImage:EasyImage

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        /*Initializes all the UI components*/

        initializeTypeTextDropdown()
        initializePicturesRecyclerView()
        initializeExtrasChipGroup()
        initializeRealtorTextDropdown()
        initializeAdDateText()
        initializeSaleInfo()
        initializeButtons()

        /*Initializes pictures support items*/

        initializeEasyImage()

        /*If a property id exists, then loads the property's data*/

        if(this.propertyId!=null) loadProperty()

        return this.layout
    }

    /*********************************************************************************************
     * PropertyBaseFragment
     ********************************************************************************************/

    override fun getLayoutId() = R.layout.fragment_property_edit

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeTypeTextDropdown(){
        this.propertyTypeViewModel.getAllPropertyTypes().observe(this, Observer{
            initializeTextDropDown(this.typeTextDropdown, R.layout.dropdown_menu_standard, it)
        })
    }

    private fun initializePicturesRecyclerView(){
        this.pictureAdapter= PictureAdapter(this.picturesPaths, true, this)
        this.picturesRecyclerView.adapter=this.pictureAdapter
        this.picturesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun initializeExtrasChipGroup(){
        this.extraViewModel.gelAllExtra().observe(this, Observer{
            initializeChipGroup(this.extrasChipGroup, this.extrasChips, R.layout.chip_standard, it)
        })
    }

    private fun initializeRealtorTextDropdown(){
        this.realtorViewModel.getAllRealtors().observe(this, Observer{
            initializeTextDropDown(this.realtorTextDropDown, R.layout.dropdown_menu_standard, it)
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
        }
    }

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

    override fun onDeletePictureButtonClick(position: Int) {
        removePicture(position)
    }

    override fun onAddPictureButtonClick(position: Int) {
        startAddPictureIntent()
    }

    override fun onTakePictureButtonClick(position: Int) {
        requestCamera()
    }

    /*********************************************************************************************
     * Pictures management
     ********************************************************************************************/

    fun addPicture(picturePath:String){
        this.picturesPaths.add(this.picturesPaths.size-1, picturePath)
        this.pictureAdapter.notifyDataSetChanged()
    }

    fun removePicture(position:Int){
        this.picturesPaths.removeAt(position)
        this.pictureAdapter.notifyDataSetChanged()
    }

    /*********************************************************************************************
     * Input control
     ********************************************************************************************/

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
        if(!checkTextIsNotEmpty(this.postalCodeText, this.postalCodeInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.cityText, this.cityInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.countryText, this.countryInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.realtorTextDropDown, this.realtorInputLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.adDateText, this.adDateInputLayout)) isValid=false
        if(!checkSaleDateIsValid()) isValid=false

        return isValid
    }

    private fun checkTextIsNotEmpty(editText:TextInputEditText, textLayout:TextInputLayout):Boolean{
        if(editText.text.isNullOrEmpty()) {
            textLayout.error = resources.getString(R.string.error_mandatory_field)
            return false
        }else{
            textLayout.error=null
            return true
        }
    }

    private fun checkTextIsNotEmpty(editText:AutoCompleteTextView, textLayout:TextInputLayout):Boolean{
        if(editText.text.isNullOrEmpty()) {
            textLayout.error = resources.getString(R.string.error_mandatory_field)
            return false
        }else{
            textLayout.error=null
            return true
        }
    }

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
     * Data management
     ********************************************************************************************/

    fun saveProperty(){

        if(!checkInputIsValid()){
            (activity as BaseActivity).showSimpleDialog(
                    resources.getString(R.string.dialog_title_validation_issue),
                    resources.getString(R.string.dialog_message_input_not_valid))
        }else {

            val property = Property()
            property.adTitle = this.adTitleText.text.toString()
            property.price = Integer.parseInt(this.priceText.text.toString())
            property.typeId = (this.typeTextDropdown.tag as PropertyType).id
            property.picturesPaths = this.picturesPaths.filterNotNull()
            property.description = this.descriptionText.text.toString()
            property.size = Integer.parseInt(this.sizeText.text.toString())
            property.nbRooms = Integer.parseInt(this.nbRoomsText.text.toString())
            property.nbBedrooms = Integer.parseInt(this.nbBedroomsText.text.toString())
            property.nbBathrooms = Integer.parseInt(this.nbBathroomsText.text.toString())
            property.buildYear = Integer.parseInt(this.buildYearText.text.toString())
            property.address = this.addressText.text.toString()
            property.postalCode = this.postalCodeText.text.toString()
            property.city = this.cityText.text.toString()
            property.country = this.countryText.text.toString()
            property.realtorId = (this.realtorTextDropDown.tag as Realtor).id
            property.adDate = Utils.getDateFromString(this.adDateText.text.toString())
            property.sold = this.soldSwitch.isChecked
            if (!this.saleDateText.text.isNullOrEmpty())
                property.saleDate = Utils.getDateFromString(this.saleDateText.text.toString())
            if (this.propertyId == null) {
                this.propertyId = this.propertyViewModel.insertProperty(property).toInt()
            } else {
                property.id = this.propertyId
                this.propertyViewModel.updateProperty(property)
            }
            val propertyId = this.propertyId
            savePropertyExtras(propertyId!!.toInt())

            Toast.makeText(context!!, R.string.toast_message_property_saved, Toast.LENGTH_LONG).show()

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

    private fun loadProperty() {

        this.propertyViewModel.getProperty(this.propertyId!!.toInt()).observe(this, Observer {
            val property = it
            this.adTitleText.setText(property.adTitle)
            this.priceText.setText(property.price.toString())
            val typeId = property.typeId
            if (typeId != null) loadPropertyType(typeId)
            loadPropertyPictures(property.picturesPaths)
            this.descriptionText.setText(property.description)
            this.sizeText.setText(property.size.toString())
            this.nbRoomsText.setText(property.nbRooms.toString())
            this.nbBedroomsText.setText(property.nbBedrooms.toString())
            this.nbBathroomsText.setText(property.nbBathrooms.toString())
            this.buildYearText.setText(property.buildYear.toString())
            this.addressText.setText(property.address)
            this.postalCodeText.setText(property.postalCode)
            this.cityText.setText(property.city)
            this.countryText.setText(property.country)
            val realtorId = property.realtorId
            if (realtorId != null) loadPropertyRealtor(realtorId)
            this.adDateText.setText(Utils.getStringFromDate(property.adDate))
            this.soldSwitch.isChecked = property.sold
            if (property.saleDate != null)
                this.saleDateText.setText(Utils.getStringFromDate(property.saleDate))
        })

        loadPropertyExtras(propertyId!!)
    }

    private fun loadPropertyType(typeId:Int){
        val propertyType=this.typeTextDropdown.adapter.getItem(typeId-1)
        this.typeTextDropdown.setText(propertyType.toString(), false)
        this.typeTextDropdown.tag=propertyType
    }

    private fun loadPropertyPictures(picturesPaths:List<String>){
        this.picturesPaths.clear()
        this.picturesPaths.addAll(picturesPaths)
        this.picturesPaths.add(null)
        this.pictureAdapter.notifyDataSetChanged()
    }

    private fun loadPropertyExtras(propertyId:Int){
        this.propertyViewModel.getPropertyExtras(propertyId).observe(this, Observer {
            for(extra in it){
                this.extrasChips[extra.extraId-1].isChecked=true
            }
        })
    }

    private fun loadPropertyRealtor(realtorId:String){
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
            KEY_REQUEST_PERMISSION_WRITE_AND_CAMERA -> if (grantResults.size > 0) {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> startTakePictureIntent()
                    PackageManager.PERMISSION_DENIED -> Log.d("TAG_PERMISSION", "Permission denied")
                }
            }
        }
    }

    private fun requestCamera(){
        if(Build.VERSION.SDK_INT>=23
                &&(activity!!.checkSelfPermission(KEY_PERMISSION_WRITE)!= PackageManager.PERMISSION_GRANTED
                        ||activity!!.checkSelfPermission(KEY_PERMISSION_CAMERA)!= PackageManager.PERMISSION_GRANTED)){

            if(shouldShowRequestPermissionRationale(KEY_PERMISSION_WRITE)
                            ||shouldShowRequestPermissionRationale(KEY_PERMISSION_CAMERA)){

                (activity as BaseActivity).showSimpleDialog(
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
                    addPicture(image.file.toURI().path)
                }
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                super.onImagePickerError(error, source)
                Log.d("TAG_PICTURE", error.message)
                (activity as BaseActivity).showSimpleDialog(
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
                //TODO handle
            }
        }else{
            activity!!.finish()
        }
    }
}
