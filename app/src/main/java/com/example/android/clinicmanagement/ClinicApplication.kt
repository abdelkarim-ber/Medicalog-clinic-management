package com.example.android.clinicmanagement

import android.app.Application
import com.example.android.clinicmanagement.data.AppContainer
import com.example.android.clinicmanagement.data.ClinicDatabase
import com.example.android.clinicmanagement.data.datasources.*
import com.example.android.clinicmanagement.data.repositories.*

class ClinicApplication :Application() {

    lateinit var appContainer:AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }



}