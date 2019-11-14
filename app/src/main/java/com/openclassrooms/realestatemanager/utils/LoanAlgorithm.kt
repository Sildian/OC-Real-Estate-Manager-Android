package com.openclassrooms.realestatemanager.utils

import kotlin.math.pow

/**************************************************************************************************
 * These functions calculate loans related amounts
 *************************************************************************************************/

object LoanAlgorithm {

    fun calculateLoanAmount(investmentAmount:Long, contributionAmount:Long):Long{
        return investmentAmount-contributionAmount
    }

    fun calculateLoanMonthlyPayments(loanAmount:Long, nbYears:Int, interestRate:Double):Double{
        val nbMonths=nbYears*12
        return (loanAmount*(interestRate/12))/(1- (1 + (interestRate / 12)).pow(-nbMonths.toDouble()))
    }
}