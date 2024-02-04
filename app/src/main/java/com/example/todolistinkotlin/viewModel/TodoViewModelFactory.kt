package com.example.todolistinkotlin.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolistinkotlin.network.TodoDataRepositoryImp

class TodoViewModelFactory(private val application: Application, private val repositoryImp: TodoDataRepositoryImp):ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  ToDoListViewModel(
            application,
            repositoryImp
        ) as T
    }
}