package com.example.android.clinicmanagement.data

import android.content.Context
import com.example.android.clinicmanagement.data.datasources.*
import com.example.android.clinicmanagement.data.repositories.*

// Container of objects shared across the whole app
class AppContainer(context: Context) {

    private val clinicDatabase = ClinicDatabase.getInstance(context)
    private val patientDataSource = PatientDataSource(clinicDatabase.patientDao)
    private val sessionDataSource = SessionDataSource(clinicDatabase.sessionDao)
    private val expenditureDataSource = ExpenditureDataSource(clinicDatabase.expenditureDao)
    private val quotationDataSource = QuotationDataSource(clinicDatabase.quotationDao)
    private val invoiceDataSource = InvoiceDataSource(clinicDatabase.invoiceDao)

    val patientRepository = PatientRepository(patientDataSource)
    val sessionRepository = SessionRepository(sessionDataSource)
    val expenditureRepository = ExpenditureRepository(expenditureDataSource)
    val quotationRepository = QuotationRepository(quotationDataSource)
    val invoiceRepository = InvoiceRepository(invoiceDataSource)
}