package com.example.todolistinkotlin.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistinkotlin.*
import com.example.todolistinkotlin.databinding.ActivityMainBinding
import com.example.todolistinkotlin.model.ToDoListData
import com.example.todolistinkotlin.model.TodoData
import com.example.todolistinkotlin.network.RetrofitFactory
import com.example.todolistinkotlin.network.TodoDataRepositoryImp
import com.example.todolistinkotlin.viewModel.ToDoListViewModel
import com.example.todolistinkotlin.viewModel.TodoViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),
    OnItemClick {

    val list = mutableListOf<ToDoListData>()

    val c = Calendar.getInstance()

    val month: Int = c.get(Calendar.MONTH)
    val year: Int = c.get(Calendar.YEAR)
    val day: Int = c.get(Calendar.DAY_OF_MONTH)

    var cal = Calendar.getInstance()

    private val listAdapter =
        ListAdapter(list, this)

    private lateinit var binding: ActivityMainBinding

    private val repository: TodoDataRepositoryImp by lazy {
        TodoDataRepositoryImp(RetrofitFactory.service)
    }

    private val viewModel: ToDoListViewModel by lazy {
        val todoViewModelFactory =
            TodoViewModelFactory(
                application,
                repository
            )
        ViewModelProviders.of(this,todoViewModelFactory)[ToDoListViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this

        rvTodoList.layoutManager = LinearLayoutManager(this)
        rvTodoList.adapter = listAdapter
        binding.vieModel = viewModel


        viewModel.getPreviousList()

        viewModel.toDoList.observe(this, androidx.lifecycle.Observer { it ->
            //list.addAll(it)
            if (it == null)
                return@Observer

            list.clear()
            val tempList = mutableListOf<ToDoListData>()
            it.forEach {
                tempList.add(
                    ToDoListData(
                        title = it.title,
                        date = it.date,
                        time = it.time,
                        indexDb = it.id,
                        isShow = it.isShow
                    )
                )

            }

            list.addAll(tempList)
            listAdapter.notifyDataSetChanged()
            viewModel.position = -1;

            viewModel.toDoList.value = null
        })

        viewModel.toDoListData.observe(this, androidx.lifecycle.Observer {
            if (viewModel.position != -1) {
                list.set(viewModel.position, it)
                listAdapter.notifyItemChanged(viewModel.position)
            } else {
                list.add(it)
                listAdapter.notifyDataSetChanged()
            }
            viewModel.position = -1;
        })

        etdate.setOnClickListener {

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                etdate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                viewModel.month = monthOfYear
                viewModel.year = year
                viewModel.day = dayOfMonth
            }, year, month, day)

            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()

        }
        etTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                this.cal.set(Calendar.HOUR_OF_DAY, hour)
                this.cal.set(Calendar.MINUTE, minute)

                viewModel.hour = hour
                viewModel.minute = minute

                etTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }

            this.cal = cal
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onItemClick(v: View, position: Int) {

        viewModel.previousData= TodoData(list.get(position).title,list.get(position).date,list.get(position).time,list.get(position).indexDb)

        alert {
            message = list.get(position).title
            positiveButton("Edit") {
                viewModel.title.set(list.get(position).title)
                viewModel.date.set(list.get(position).date)
                viewModel.time.set(list.get(position).time)
                viewModel.position = position
                viewModel.index = list.get(position).indexDb
                viewModel.isEditing=true;
                editText.isFocusable = true
            }
            negativeButton("Delete") {
                viewModel.delete(list.get(position).indexDb)
                viewModel.deleteAnalytics()
            }

        }.show()

    }

    override fun onStop() {
        super.onStop()
    }
}
