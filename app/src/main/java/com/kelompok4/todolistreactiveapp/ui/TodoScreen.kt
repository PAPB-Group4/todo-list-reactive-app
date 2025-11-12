package com.kelompok4.todolistreactiveapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kelompok4.todolistreactiveapp.viewmodel.FilterType
import com.kelompok4.todolistreactiveapp.viewmodel.TodoViewModel

@Composable
fun TodoScreen(vm: TodoViewModel = viewModel()) {
    val activeCount by vm.activeCount.collectAsState()
    val completedCount by vm.completedCount.collectAsState()
    val todos by vm.filteredTodos.collectAsState()
    val currentFilter by vm.filter.collectAsState()
    val query by vm.query.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tambah tugas...") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    vm.addTask(text.trim())
                    text = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) { Text("Tambah") }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            FilterButton("Semua", currentFilter == FilterType.ALL) { vm.setFilter(FilterType.ALL) }
            FilterButton(
                "Aktif",
                currentFilter == FilterType.ACTIVE
            ) { vm.setFilter(FilterType.ACTIVE) }
            FilterButton(
                "Selesai",
                currentFilter == FilterType.DONE
            ) { vm.setFilter(FilterType.DONE) }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { vm.setQuery(it) },
            placeholder = { Text("Cari judul…") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Divider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Aktif: $activeCount", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Selesai: $completedCount", style = MaterialTheme.typography.bodyMedium)
        }

        LazyColumn {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onToggle = { vm.toggleTask(todo.id) },
                    onDelete = { vm.deleteTask(todo.id) }
                )
            }
        }
    }
}


        @Composable
fun FilterButton(text: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(onClick = onClick) { Text(text) }
    } else {
        OutlinedButton(onClick = onClick) { Text(text) }
    }
}
