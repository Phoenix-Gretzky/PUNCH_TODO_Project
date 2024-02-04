package com.example.todolistinkotlin.model

import androidx.room.Entity

/**
 *   Created by Sundar Pichai on 5/8/19.
 */


class ToDoListData(
    val title: String = "",
    val date: String = "",
    val time: String = "",
    var indexDb: Long = 0,
    val isShow : Int = 0
)