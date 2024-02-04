package com.example.todolistinkotlin.model

data class TodoData(val title:String="", val date:String="", val time:String="", val id: Long=-1, val isEditing:Boolean=false)