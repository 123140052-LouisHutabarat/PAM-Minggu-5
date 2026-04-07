package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import minggu5pam.composeapp.generated.resources.Res
import minggu5pam.composeapp.generated.resources.luis
import org.jetbrains.compose.resources.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val profileViewModel = remember { ProfileViewModel() }
    val notesViewModel = remember { NotesViewModel() }

    val profileUiState by profileViewModel.uiState.collectAsState()
    val currentNotes by notesViewModel.notes.collectAsState()
    val favoriteNotes by notesViewModel.favoriteNotes.collectAsState()

    val isDarkMode = profileUiState.isDarkMode
    val primaryBlue = Color(0xFF1976D2)

    val myColorScheme = if (isDarkMode) {
        darkColorScheme(
            primary = Color(0xFF90CAF9),
            onPrimary = Color(0xFF003258),
            surface = Color(0xFF1E1E1E),
            background = Color(0xFF121212)
        )
    } else {
        lightColorScheme(
            primary = primaryBlue,
            onPrimary = Color.White,
            surface = Color.White,
            background = Color(0xFFF5F5F5)
        )
    }

    MaterialTheme(colorScheme = myColorScheme) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val showBottomBar = currentRoute in listOf(Screen.Notes.route, Screen.Favorites.route, Screen.Profile.route)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Notes App") },
                    navigationIcon = {
                        if (!showBottomBar) {
                            IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Default.ArrowBack, null) }
                        }
                    },
                    actions = {
                        IconButton(onClick = { profileViewModel.toggleDarkMode(!isDarkMode) }) {
                            Icon(if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, null)
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        listOf(Screen.Notes, Screen.Favorites, Screen.Profile).forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon!!, null) },
                                label = { Text(screen.title) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(navController, Screen.Notes.route, Modifier.padding(padding)) {
                composable(Screen.Notes.route) { NotesListScreen(navController, currentNotes, favoriteNotes, notesViewModel) }
                composable(Screen.Favorites.route) { FavoritesScreen(navController, favoriteNotes, notesViewModel) }
                composable(Screen.Profile.route) { MyProfileApp(profileUiState, profileViewModel) }
                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        onNoteSaved = { title, content ->
                            notesViewModel.addNote(title, content)
                            navController.navigateUp()
                        }
                    )
                }
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                    NoteDetailContent(currentNotes.find { it.id == noteId }, navController)
                }
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                    val noteSelected = currentNotes.find { it.id == noteId }
                    EditNoteScreen(
                        noteData = noteSelected,
                        onNoteSaved = { newTitle, newContent ->
                            notesViewModel.updateNote(noteId, newTitle, newContent)
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MyProfileApp(uiState: ProfileUiState, viewModel: ProfileViewModel) {
    val containerColor = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White
    Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        if (uiState.isEditing) {
            EditProfileForm(
                uiState.name, uiState.bio, uiState.email, uiState.phone, uiState.location,
                isDarkMode = uiState.isDarkMode,
                onSave = { n, b, e, p, l -> viewModel.saveProfile(n, b, e, p, l) },
                onCancel = { viewModel.setEditingMode(false) }
            )
        } else {
            Card(
                shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = containerColor)
            ) {
                ProfileCard(uiState.name, uiState.bio, uiState.email, uiState.phone, uiState.location) { viewModel.setEditingMode(true) }
            }
        }
    }
}

@Composable
fun EditProfileForm(
    name: String, bio: String, email: String, phone: String, loc: String,
    isDarkMode: Boolean, onSave: (String, String, String, String, String) -> Unit, onCancel: () -> Unit
) {
    var n by remember { mutableStateOf(name) }
    var b by remember { mutableStateOf(bio) }
    var e by remember { mutableStateOf(email) }
    var p by remember { mutableStateOf(phone) }
    var l by remember { mutableStateOf(loc) }
    val containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White

    Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = containerColor), elevation = CardDefaults.cardElevation(8.dp)) {
        Column(Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            Text("Edit Profil", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = n, onValueChange = { n = it }, label = { Text("Nama") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = b, onValueChange = { b = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = e, onValueChange = { e = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = p, onValueChange = { p = it }, label = { Text("Telepon") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = l, onValueChange = { l = it }, label = { Text("Lokasi") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) { Text("Batal", color = MaterialTheme.colorScheme.primary) }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onSave(n, b, e, p, l) }) {
                    Text("Simpan", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
fun ProfileCard(name: String, bio: String, email: String, phone: String, loc: String, onEdit: () -> Unit) {
    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(Res.drawable.luis), null, Modifier.size(120.dp).clip(CircleShape), contentScale = ContentScale.Crop)
        Spacer(Modifier.height(16.dp))
        Text(name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(bio, textAlign = TextAlign.Center, color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
        InfoItem(Icons.Default.Email, email)
        InfoItem(Icons.Default.Phone, phone)
        InfoItem(Icons.Default.LocationOn, loc)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onEdit, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Edit Profile", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Text(text, fontSize = 14.sp)
    }
}