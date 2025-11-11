package com.kelompok4.todolistreactiveapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kelompok4.todolistreactiveapp.model.Todo
interface TodoRepository {
    suspend fun loadAll(): List<Todo>
    suspend fun insert(title: String)
}
class TodoViewModelWithRepo(
    private val repo: TodoRepository
) : ViewModel() {
// Implementasi dengan repo
}
class TodoViewModelFactory(
    private val repo: TodoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TodoViewModelWithRepo(repo) as T
    }
}