package org.example.project

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun NoteCard(note: Note, isFav: Boolean, onClick: () -> Unit, onLongPress: () -> Unit, onFavClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) { detectTapGestures(onTap = { onClick() }, onLongPress = { onLongPress() }) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(note.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(note.content, maxLines = 2, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onFavClick) {
                Icon(if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (isFav) Color.Red else Color.Gray)
            }
        }
    }
}

@Composable
fun NotesListScreen(navController: NavController, notesList: List<Note>, favList: List<Note>, viewModel: NotesViewModel) {
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(notesList, key = { it.id }) { note ->
                NoteCard(note, favList.any { it.id == note.id },
                    onClick = { navController.navigate(Screen.NoteDetail.createRoute(note.id)) },
                    onLongPress = { noteToDelete = note },
                    onFavClick = { viewModel.toggleFavorite(note) }
                )
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddNote.route) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah Catatan")
        }
    }

    if (noteToDelete != null) {
        AlertDialog(onDismissRequest = { noteToDelete = null }, title = { Text("Hapus Catatan?") },
            text = { Text("Hapus '${noteToDelete!!.title}' secara permanen?") },
            confirmButton = { TextButton(onClick = { viewModel.deleteNote(noteToDelete!!.id); noteToDelete = null }) { Text("Hapus", color = Color.Red) } },
            dismissButton = { TextButton(onClick = { noteToDelete = null }) { Text("Batal", color = MaterialTheme.colorScheme.primary) } }
        )
    }
}

@Composable
fun FavoritesScreen(navController: NavController, favList: List<Note>, viewModel: NotesViewModel) {
    if (favList.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada favorit", color = Color.Gray) }
    } else {
        LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(favList, key = { it.id }) { note ->
                NoteCard(note, true,
                    onClick = { navController.navigate(Screen.NoteDetail.createRoute(note.id)) },

                    onLongPress = { /* Jangan lakukan apa-apa */ },
                    onFavClick = { viewModel.toggleFavorite(note) }
                )
            }
        }
    }
}

@Composable
fun AddNoteScreen(onNoteSaved: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul Catatan") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi Catatan") }, modifier = Modifier.fillMaxWidth().weight(1f), minLines = 5)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { if (title.isNotBlank() && content.isNotBlank()) onNoteSaved(title, content) },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        ) {

            Text("Simpan Catatan", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun NoteDetailContent(noteData: Note?, navController: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (noteData == null) {
            Text("Data tidak ditemukan")
        } else {
            Text(noteData.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text(noteData.content, fontSize = 18.sp, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onBackground)

            Button(
                onClick = { navController.navigate(Screen.EditNote.createRoute(noteData.id)) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            ) {

                Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Edit Catatan", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun EditNoteScreen(noteData: Note?, onNoteSaved: (String, String) -> Unit) {
    var title by remember { mutableStateOf(noteData?.title ?: "") }
    var content by remember { mutableStateOf(noteData?.content ?: "") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { Text("Judul Catatan") },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = content, onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth().weight(1f), minLines = 5
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { if (title.isNotBlank() && content.isNotBlank()) onNoteSaved(title, content) },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        ) {

            Text("Update Catatan", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}