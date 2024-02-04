package com.example.todolistinkotlin.network

import android.util.Log
import com.example.todolistinkotlin.model.TodoData

class TodoDataRepositoryImp(private val apiInterface: ApiInterface):TodoDataRepository {

    override suspend fun sendAnalyticsReport(previousData:TodoData,data:TodoData){
        //here we fire api which will be written in apiInterface
       // val response = apiInterface.sendAnalyticsData(previousData,data)            //this will throw exception rn that's why it commented rn
        // we will be sending the complete todoList data to the server with the variable to tell if it was being editing or added
        Log.i("TAG","previous data :  $previousData  \t\t  NEW DATA : $data   ")
    }

    override suspend fun sendDeleteAnalyticsReport(previousData: TodoData) {
        //here we fire api which will be written in apiInterface
        // val response = apiInterface.sendAnalyticsData(previousData,data)            //this will throw exception rn that's why it commented rn
        // we will be sending the complete todoList data to the server with the variable to tell if it was being editing or added
        Log.i("TAG","previous data :  $previousData   ")
    }
}


interface TodoDataRepository {
    suspend fun sendAnalyticsReport(previousData: TodoData,data:TodoData)
    suspend fun sendDeleteAnalyticsReport(previousData: TodoData)
}