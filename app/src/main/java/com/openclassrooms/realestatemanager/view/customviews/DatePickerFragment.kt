package com.openclassrooms.realestatemanager.view.customviews

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Utils
import java.util.*

/**************************************************************************************************
 * Provides a dialog allowing the user to pick a Date in a calendar
 * @Param callView : the view calling the datePickerFragment
 *************************************************************************************************/

class DatePickerFragment(val callView: AutoCompleteTextView)
    :DialogFragment(), DatePickerDialog.OnDateSetListener
{

    /**UI components**/

    private lateinit var datePickerDialog: DatePickerDialog

    /**Data**/

    private var year=0
    private var month=0
    private var day=0

    /**Callbacks**/

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initializeDate()
        this.datePickerDialog= DatePickerDialog(context!!, R.style.DatePickerTheme,
                this, this.year, this.month, this.day)
        return this.datePickerDialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date=Utils.getDate(year, month, dayOfMonth)
        this.callView.setText(date)
        this.callView.clearFocus()
    }

    override fun onCancel(dialog: DialogInterface) {
        this.callView.setText("")
        this.callView.clearFocus()
        super.onCancel(dialog)
    }

    /**Initializes the DatePicker's date**/

    private fun initializeDate(){

        /*Sets the date to today by default*/

        val calendar= Calendar.getInstance()

        /*If the callView already holds a date, then sets it as the DatePicker's date*/

        if(!this.callView.text.isNullOrEmpty()){
            calendar.time= Utils.getDateFromString(this.callView.text.toString())
        }

        this.year=calendar.get(Calendar.YEAR)
        this.month=calendar.get(Calendar.MONTH)
        this.day=calendar.get(Calendar.DAY_OF_MONTH)
    }
}