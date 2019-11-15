package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.LoanAlgorithm
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.activities.BaseActivity
import kotlinx.android.synthetic.main.fragment_loan.view.*

/**************************************************************************************************
 * Loan simulator for the user
 *************************************************************************************************/

class LoanFragment : Fragment() {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    /**Main layout**/

    private lateinit var layout:View

    /**Text input layouts**/

    private val investmentAmountLayout by lazy {layout.fragment_loan_investment_layout}
    private val contributionAmountLayout by lazy {layout.fragment_loan_contribution_layout}
    private val durationLayout by lazy {layout.fragment_loan_duration_layout}
    private val interestRateLayout by lazy {layout.fragment_loan_interest_rate_layout}

    /**Components on the screen**/

    private val investmentAmount by lazy {layout.fragment_loan_investment}
    private val contributionAmount by lazy {layout.fragment_loan_contribution}
    private val loanAmount by lazy {layout.fragment_loan_loan}
    private val duration by lazy {layout.fragment_loan_duration}
    private val interestRate by lazy {layout.fragment_loan_interest_rate}
    private val monthlyPayments by lazy {layout.fragment_loan_monthly_payments}
    private val calculateButton by lazy {layout.fragment_loan_button_calculate}

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.layout=inflater.inflate(R.layout.fragment_loan, container, false)
        initializeCalculateButton()
        return this.layout
    }

    /*********************************************************************************************
     * UI initialization
     ********************************************************************************************/

    private fun initializeCalculateButton(){
        this.calculateButton.setOnClickListener { calculateLoanAmounts() }
    }

    /*********************************************************************************************
     * Loan calculation
     ********************************************************************************************/

    private fun calculateLoanAmounts(){
        if(checkInputIsValid()) {
            val loanAmount = calculateLoanAmount()
            calculateMonthlyPaymentsAmount(loanAmount)
        }
        else{
            (activity as BaseActivity).showErrorDialog(
                    resources.getString(R.string.dialog_title_validation_issue),
                    resources.getString(R.string.dialog_message_input_not_valid))
        }
    }

    private fun calculateLoanAmount():Long{
        val investmentAmount:Long=this.investmentAmount.text.toString().toLong()
        val contributionAmount:Long=this.contributionAmount.text.toString().toLong()
        val result=LoanAlgorithm.calculateLoanAmount(investmentAmount, contributionAmount)
        val formatedResult= Utils.getFormatedFigure(result)
        val loanAmountToDisplay=resources.getString(R.string.label_loan_loan)+" : "+formatedResult
        this.loanAmount.text=loanAmountToDisplay
        return result
    }

    private fun calculateMonthlyPaymentsAmount(loanAmount:Long){
        val duration:Int=this.duration.text.toString().toInt()
        val interestRate:Double=this.interestRate.text.toString().toDouble()
        val result=LoanAlgorithm.calculateLoanMonthlyPayments(loanAmount, duration, interestRate)
        val formatedResult=Utils.getFormatedFigure(result)
        val monthlyPaymentsToDisplay=resources.getString(R.string.label_loan_monthly_payments)+" : "+formatedResult
        this.monthlyPayments.text=monthlyPaymentsToDisplay
    }

    /*********************************************************************************************
     * Input control
     ********************************************************************************************/

    /**Checks that all input fields are valid**/

    private fun checkInputIsValid():Boolean{
        var isValid=true
        if(!checkTextIsNotEmpty(this.investmentAmount, this.investmentAmountLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.contributionAmount, this.contributionAmountLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.duration, this.durationLayout)) isValid=false
        if(!checkTextIsNotEmpty(this.interestRate, this.interestRateLayout)) isValid=false
        return isValid
    }

    /**Checks that a simple text field is not empty**/

    private fun checkTextIsNotEmpty(editText: TextInputEditText, textLayout: TextInputLayout):Boolean{
        if(editText.text.isNullOrEmpty()) {
            textLayout.error = resources.getString(R.string.error_mandatory_field)
            return false
        }else{
            textLayout.error=null
            return true
        }
    }
}
