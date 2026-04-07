package org.example.project

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class NotesViewModel {
    private val _notes = MutableStateFlow(listOf(
        Note("id_1", "Ide Proyek KMP", "Coba gabungkan Compose Multiplatform."),
        Note("id_2", "Daftar Belanja", "Beli kabel jumper, ESP32."),
        Note("id_3", "Tugas Kuliah", "Selesaikan laporan PAM minggu ini.")
    ))
    val notes = _notes.asStateFlow()

    private val _favoriteNotes = MutableStateFlow<List<Note>>(emptyList())
    val favoriteNotes = _favoriteNotes.asStateFlow()

    fun addNote(title: String, content: String) {
        val newNote = Note(id = "id_${Random.nextInt()}", title = title, content = content)
        _notes.update { it + newNote }
    }

    fun updateNote(noteId: String, newTitle: String, newContent: String) {
        _notes.update { list ->
            list.map { note ->
                if (note.id == noteId) note.copy(title = newTitle, content = newContent) else note
            }
        }
        _favoriteNotes.update { list ->
            list.map { note ->
                if (note.id == noteId) note.copy(title = newTitle, content = newContent) else note
            }
        }
    }

    fun deleteNote(noteId: String) {
        _notes.update { list -> list.filter { it.id != noteId } }
        _favoriteNotes.update { list -> list.filter { it.id != noteId } }
    }

    fun toggleFavorite(note: Note) {
        _favoriteNotes.update { current ->
            if (current.any { it.id == note.id }) current.filter { it.id != note.id } else current + note
        }
    }
}