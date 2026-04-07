package org.example.project

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Notes : Screen("notes", "Notes", Icons.Default.List)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)

    object AddNote : Screen("add_note", "Add Note")

    object NoteDetail : Screen("note_detail/{noteId}", "Note Detail") {
        fun createRoute(noteId: String) = "note_detail/$noteId"
    }
    object EditNote : Screen("edit_note/{noteId}", "Edit Note") {
        fun createRoute(noteId: String) = "edit_note/$noteId"
    }
}