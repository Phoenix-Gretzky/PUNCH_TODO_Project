package com.example.todolistinkotlin.viewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todolistinkotlin.database.ToDoListDataEntity
import com.example.todolistinkotlin.database.ToDoListDatabase
import com.example.todolistinkotlin.model.ToDoListData
import com.example.todolistinkotlin.model.TodoData
import com.example.todolistinkotlin.network.TodoDataRepositoryImp
import com.example.todolistinkotlin.notification.AlarmReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 *   Created by Sundar Pichai on 5/8/19.
 */
class ToDoListViewModel(val context: Application,private val repositoryImp: TodoDataRepositoryImp) : AndroidViewModel(context) {
    var toDoListData = MutableLiveData<ToDoListData>()

    var database: ToDoListDatabase? = null
    var isEditing:Boolean=false;
    var previousData:TodoData

    var getAllData = mutableListOf(ToDoListDataEntity())
    val toDoList = MutableLiveData<List<ToDoListDataEntity>>()


    init {
        database = ToDoListDatabase.getInstance(context)
        database?.toDoListDao()?.getAll()?.let {
            getAllData = it as MutableList<ToDoListDataEntity>
        }
        previousData= TodoData()
    }

    var title = ObservableField<String>("")
    var date = ObservableField<String>("")
    var time = ObservableField<String>("")

    var month = 0
    var day = 0
    var year = 0

    var hour = 0
    var minute = 0

    var position: Int = -1
    var index: Long = -1


    @RequiresApi(Build.VERSION_CODES.M)
    fun click(v: View) {

        Log.d("Click", "click")
        if (title.get().toString().isNotBlank() && date.get().toString().isNotBlank() && time.get().toString().isNotBlank()) {
            addData(title.get().toString(), date.get().toString(), time.get().toString(), id = index)

            val Data= TodoData(title.get().toString(), date.get().toString(), time.get().toString(), index,this.isEditing)
            title.set("")
            date.set("")
            time.set("")


            CoroutineScope(Dispatchers.IO).launch {
                sendAnalytics(previousData,Data)
            }

            isEditing=false;
            previousData=TodoData()
        }else{
            Toast.makeText(context,"Enter All Filed data",Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @WorkerThread
    private fun addData(title: String, date: String, time: String, id: Long) {
        //database?.toDoListDao()?.insert(ToDoListDataEntity(title = title, date = date, time = time))
        if (position != -1) {
            database?.toDoListDao()?.update(title = title, date = date, time = time, id = id)
        } else {
            val newId = database?.toDoListDao()?.insert(ToDoListDataEntity(title = title, date = date, time = time, isShow = 0))

            val cal : Calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())

            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.DAY_OF_MONTH, day)

            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            Log.d("Alarm Title","$month , $date : ${cal.time}")
            newId?.let {
                setAlarm(cal, 0, it, title,hour,minute)
            }

        }

        database?.toDoListDao()?.getAll().let {
            getAllData = it as MutableList<ToDoListDataEntity>
            getPreviousList()
        }
    }


    fun getPreviousList() {
        toDoList.value = getAllData
    }

    fun delete(id: Long) {
        database?.toDoListDao()?.Delete(id)
        database?.toDoListDao()?.getAll().let {
            getAllData = it as MutableList<ToDoListDataEntity>
            getPreviousList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAlarm(calender: Calendar, i: Int, id: Long, title: String, hour:Int,minute:Int) {

        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("INTENT_NOTIFY", true)
        intent.putExtra("isShow", i)
        intent.putExtra("id", id)
        intent.putExtra("title", title)
        intent.putExtra("date","Time-> $hour:$minute")
        val pandingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (i == 0) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,  calender.timeInMillis , pandingIntent)
        } else {
            alarmManager.cancel(pandingIntent)
        }
    }


    private suspend fun sendAnalytics(previousData: TodoData,data: TodoData)
    {
        try {
            repositoryImp.sendAnalyticsReport(previousData,data)
        } catch (e: Exception) {
            // Handle errors (e.g., show error message)
            e.printStackTrace()
            print(e)

        }
    }
    private suspend fun sendAnalyticsForDelete(previousData: TodoData)
    {
        try {
            repositoryImp.sendDeleteAnalyticsReport(previousData)
        } catch (e: Exception) {
            // Handle errors (e.g., show error message)
            e.printStackTrace()
            print(e)

        }
    }



    fun deleteAnalytics()
    {
        CoroutineScope(Dispatchers.IO).launch {
            sendAnalyticsForDelete(previousData)
        }
    }
}