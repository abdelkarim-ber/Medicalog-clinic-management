package com.example.android.clinicmanagement.data.datasources

import com.example.android.clinicmanagement.data.dao.InvoiceDao
import com.example.android.clinicmanagement.data.models.Invoice
import com.example.android.clinicmanagement.data.models.InvoiceTrack
import com.example.android.clinicmanagement.data.models.Receipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InvoiceDataSource (private val invoiceDao: InvoiceDao) {
    suspend fun addInvoiceTrack(invoiceTrack: InvoiceTrack){
        withContext(Dispatchers.IO){
            invoiceDao.insert(invoiceTrack)
        }
    }
    suspend fun deleteInvoiceTrack(invoiceTrack: InvoiceTrack){
        withContext(Dispatchers.IO){
            invoiceDao.delete(invoiceTrack)
        }
    }

    suspend fun getInvoiceWithPatientId(patientId:Long): Receipt? {
        return withContext(Dispatchers.IO){
            invoiceDao.getInvoiceWithPatientId(patientId)
        }
    }
    suspend fun generateInvoiceTrackNumberForCurrentYear():Int{
        return withContext(Dispatchers.IO){
            invoiceDao.generateInvoiceTrackNumberForCurrentYear()
        }
    }
    suspend fun patientExistsWithId(patientId:Long):Boolean{
        return withContext(Dispatchers.IO){
            invoiceDao.patientExistsWithId(patientId)
        }
    }
}