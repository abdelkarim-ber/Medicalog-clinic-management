package com.example.android.clinicmanagement.data.datasources

import com.example.android.clinicmanagement.data.dao.QuotationDao
import com.example.android.clinicmanagement.data.models.Quotation
import com.example.android.clinicmanagement.data.models.QuotationTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuotationDataSource (private val quotationDao: QuotationDao) {
    suspend fun addQuotationTrack(quotationTrack: QuotationTrack){
        withContext(Dispatchers.IO){
            quotationDao.insert(quotationTrack)
        }
    }
    suspend fun deleteQuotationTrack(quotationTrack: QuotationTrack){
        withContext(Dispatchers.IO){
            quotationDao.delete(quotationTrack)
        }
    }

    suspend fun getQuotationWithPatientId(patientId:Long): Quotation {
       return withContext(Dispatchers.IO){
            quotationDao.getQuotationWithPatientId(patientId)
        }
    }
    suspend fun generateQuotationTrackNumberForCurrentYear():Int{
       return withContext(Dispatchers.IO){
            quotationDao.generateQuotationTrackNumberForCurrentYear()
        }
    }


    suspend fun patientExistsWithId(patientId:Long):Boolean{
       return withContext(Dispatchers.IO){
            quotationDao.patientExistsWithId(patientId)
        }
    }
}