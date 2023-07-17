package com.example.android.clinicmanagement.session

import androidx.lifecycle.MutableLiveData
//Int data type for error liveData value represents a resource for string that
//we want to display in case of an error
data class NewSessionDataState(
    val date: String = "",
    val errorDate: MutableLiveData<Int?>? = MutableLiveData(null),
    val time: String = "",
    val errorTime: MutableLiveData<Int?>? = MutableLiveData(null),
    val amountPayed: String = "",
    val errorAmountPayed: MutableLiveData<Int?>? = MutableLiveData(null)
)
