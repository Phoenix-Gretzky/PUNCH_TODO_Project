package com.example.todolistinkotlin

import android.app.Application
import android.content.Context
import com.example.todolistinkotlin.network.RetrofitFactory

class MyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
 }