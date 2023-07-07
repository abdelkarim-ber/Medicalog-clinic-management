package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo
/**
*@param LastSession date In seconds of patient last session
*
 */
data class PatientStatus(
    val id: Long,
    @ColumnInfo(name = "first_name") val firstName:String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "invoice_number") val invNumber:Int?,
     val gender:Char,
     val age:Int,
     val diagnosis:String,
     val lastSession:Long?
)
