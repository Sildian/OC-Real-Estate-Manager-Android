package com.openclassrooms.realestatemanager.utils

import kotlin.math.pow

/**************************************************************************************************
 * These functions calculate loans related amounts
 *************************************************************************************************/

object LoanAlgorithm {

    /**Calculates a loan amount
     * @param investmentAmount : the amount of the investment
     * @param contributionAmount : the amount of the contribution from the buyer
     */

    fun calculateLoanAmount(investmentAmount:Long, contributionAmount:Long):Long{
        return investmentAmount-contributionAmount
    }

    /**Calculates a loan's monthly payments
     * @param loanAmount : the amount of the loan
     * @param nbYears : the number of years for the loan
     * @param interestRate : the interest rate
     */

    fun calculateLoanMonthlyPayments(loanAmount:Long, nbYears:Int, interestRate:Double):Double{
        val nbMonths=nbYears*12
        return (loanAmount*(interestRate/12))/(1- (1 + (interestRate / 12)).pow(-nbMonths.toDouble()))
    }
}