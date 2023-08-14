package com.assignment.mdev1001_m2023_assignment4

import android.app.Application
import android.content.Context
import com.assignment.mdev1001_m2023_assignment4.extension.SharedPreferenceManager
import com.google.firebase.FirebaseApp

class Assignment4Application :Application(){

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        SharedPreferenceManager.init(appContext)

    }

    companion object {
        lateinit var appContext: Context
    }


}