package com.kelompok4.todolistreactiveapp.model

data class Todo(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)