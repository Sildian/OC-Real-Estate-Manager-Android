package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.LoanAlgorithm
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
        val loanAmount=calculateLoanAmount()
        calculateMonthlyPaymentsAmount(loanAmount)
    }

    private fun calculateLoanAmount():Long{
        val investmentAmount:Long=this.investmentAmount.text.toString().toLong()
        val contributionAmount:Long=this.contributionAmount.text.toString().toLong()
        val result=LoanAlgorithm.calculateLoanAmount(investmentAmount, contributionAmount)
        val loanAmountToDisplay=resources.getString(R.string.label_loan_loan)+" : "+result
        this.loanAmount.text=loanAmountToDisplay
        return result
    }

    private fun calculateMonthlyPaymentsAmount(loanAmount:Long){
        val duration:Int=this.duration.text.toString().toInt()
        val interestRate:Double=this.interestRate.text.toString().toDouble()
        val result=LoanAlgorithm.calculateLoanMonthlyPayments(loanAmount, duration, interestRate)
        val monthlyPaymentsToDisplay=resources.getString(R.string.label_loan_monthly_payments)+" : "+result
        this.monthlyPayments.text=monthlyPaymentsToDisplay
    }
}
