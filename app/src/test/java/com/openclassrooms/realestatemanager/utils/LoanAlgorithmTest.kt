package com.openclassrooms.realestatemanager.utils

import org.junit.Test

import org.junit.Assert.*

class LoanAlgorithmTest {

    @Test
    fun given_investment1000000Contribution300000_when_calculateLoanAmount_then_check_result700000() {
        val investmentAmount:Long=1000000
        val contributionAmount:Long=300000
        assertEquals(700000, LoanAlgorithm.calculateLoanAmount(investmentAmount, contributionAmount))
    }

    @Test
    fun given_loan700000Years20Rate1percent_when_calculateLoanMonthlyPayments_then_checkResult() {
        val loanAmount:Long=700000
        val nbYears=20
        val interestRate=0.01
        assertEquals(3219.26, LoanAlgorithm.calculateLoanMonthlyPayments(
                loanAmount, nbYears, interestRate), 0.1)
    }
}