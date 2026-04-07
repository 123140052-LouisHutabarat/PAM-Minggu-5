# 📝 Notes App - PAM Minggu 5

**Nama: Louis Hutabarat**
**NIM: 123140052**

---

Aplikasi pencatatan (Notes App) sederhana yang dibangun menggunakan **Jetpack Compose**. Aplikasi ini mengimplementasikan navigasi multi-layar, *state management* menggunakan ViewModel, dan dukungan tema terang/gelap (Light/Dark Mode) yang terintegrasi penuh dengan Material Design 3.

## ✨ Fitur Utama
* **Manajemen Catatan:** Tambah, Edit, Lihat Detail, dan Hapus catatan secara dinamis.
* **Favorit:** Tandai catatan penting (ikon hati) agar otomatis tersimpan di tab khusus Favorit.
* **Profil Pengguna:** Halaman profil melayang (Card UI) yang interaktif, di mana data diri (Nama, Bio, Kontak) dapat diedit dan disimpan secara langsung.
* **UI/UX Dinamis:** Penyesuaian warna otomatis dan sinkronisasi antara Dark Mode dan Light Mode tanpa mengubah *system settings* bawaan perangkat.

---

## 🎥 Demo Aplikasi

Untuk melihat fungsionalitas aplikasi (navigasi, animasi, dan fungsi *read/write/edit* data), silakan tonton video demo singkat berikut:

👉 **[Tonton Video Demo Aplikasi di Sini](https://drive.google.com/drive/folders/1XUgiB1ykJMnHYy59ssDPaI_r-3s1mVdz)**

> *Catatan: Pastikan menggunakan akun yang memiliki akses, atau link sudah di-set ke "Anyone with the link".*

---

## 📂 Struktur File

Proyek ini menyimpan komponen utamanya dalam satu *package* sejajar agar lebih ringkas dan mudah diakses:

📁 app/src/main/java/org/example/project/
├── App.kt               # Entry point utama, Scaffold, NavHost Navigasi, dan MaterialTheme
├── Note.kt              # Data class (Model) untuk struktur objek Catatan
├── NotesScreens.kt      # Berisi seluruh komponen UI Layar (List, Favorit, Detail, Add, Edit)
├── NotesViewModel.kt    # State management untuk logika CRUD catatan & favorit
├── ProfileUiState.kt    # Data class state penyimpan input form profil
├── ProfileViewModel.kt  # State management logika untuk interaksi form profil
└── Screen.kt            # Sealed class pendefinisi rute navigasi aplikasi

## 🗺️ Alur Navigasi (Navigation Flow)

Berikut adalah alur navigasi antar layar di dalam aplikasi ini:

* **Bottom Navigation** menghubungkan 3 tab utama: `NotesList`, `Favorites`, dan `Profile`.
* Dari `NotesList`, pengguna dapat membuka `AddNoteScreen` via tombol FAB (+).
* Memilih kartu catatan dari halaman List atau Favorites akan membuka `NoteDetailScreen`.
* Halaman `NoteDetailScreen` menyediakan akses langsung untuk masuk ke halaman `EditNoteScreen`.

## 📱 Screenshots

### 1. Daftar Catatan & Favorit
| Notes List (Light Mode) | Notes List (Dark Mode) | Favorites Screen |
| :---: | :---: | :---: |
| ![Notes Light](https://github.com/user-attachments/assets/74da16dd-101e-428c-a2d9-357fdb15afea) | ![Notes Dark](https://github.com/user-attachments/assets/630a8d11-eeeb-4f43-acec-eb1256d3069a) | ![Favorites](https://github.com/user-attachments/assets/903fb8bd-fc64-4e2c-941d-c4cd6d32290c) |

### 2. Form Catatan (Tambah, Detail, Edit)
| Add Note Screen | Note Detail Screen | Edit Note Screen |
| :---: | :---: | :---: |
| ![Add Note](https://github.com/user-attachments/assets/5894dbae-8139-457d-9e2e-e715b4dcc60f) | ![Detail Note](https://github.com/user-attachments/assets/36ef2d63-215c-48a7-a5a6-3d19e5fbed01) | 

### 3. Profil Pengguna
| Profile Screen | Edit Profile Form |
| :---: | :---: |
| ![Profile](https://github.com/user-attachments/assets/6a36601f-b32b-42eb-99de-4d0d0610a4e4) | ![Edit Profile](https://github.com/user-attachments/assets/247ac462-5e69-46ec-9a26-a63f466cbcf1) |

---

## 🛠️ Teknologi yang Digunakan

* **Bahasa Pemrograman:** Kotlin
* **UI Toolkit:** Jetpack Compose
* **Navigasi:** Navigation Compose
* **Arsitektur:** MVVM (Model-View-ViewModel) dengan `StateFlow`
* **Desain Antarmuka:** Material Design 3

---
