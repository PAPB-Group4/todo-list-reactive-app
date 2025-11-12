package com.kelompok4.todolistreactiveapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok4.todolistreactiveapp.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map


enum class FilterType { ALL, ACTIVE, DONE }

class TodoViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    private val _filter = MutableStateFlow(FilterType.ALL)
    val filter: StateFlow<FilterType> = _filter

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    // Tambahkan di class TodoViewModel
    val activeCount: StateFlow<Int> = _todos
        .map { list -> list.count { !it.isDone } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val completedCount: StateFlow<Int> = _todos
        .map { list -> list.count { it.isDone } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val filteredTodos: StateFlow<List<Todo>> =
        combine(_todos, _filter, _query) { todos, filter, q ->
            val base = when (filter) {
                FilterType.ALL -> todos
                FilterType.ACTIVE -> todos.filter { !it.isDone }
                FilterType.DONE -> todos.filter { it.isDone }
            }
            if (q.isBlank()) base
            else base.filter { it.title.contains(q, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addTask(title: String) {
        val nextId = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Todo(id = nextId, title = title)
        _todos.value = _todos.value + newTask
    }

    fun toggleTask(id: Int) {
        _todos.value = _todos.value.map { t ->
            if (t.id == id) t.copy(isDone = !t.isDone) else t
        }
    }

    fun deleteTask(id: Int) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }

    fun setFilter(filterType: FilterType) {
        _filter.value = filterType
    }

    fun setQuery(q: String) {
        _query.value = q
    }
}
