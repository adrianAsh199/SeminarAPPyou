# Seminar Registration App 🎓

Aplikasi pendaftaran seminar berbasis Android yang dibangun menggunakan **Jetpack Compose**. Proyek ini dirancang untuk memenuhi tugas UTS (Ujian Tengah Semester) dengan fokus pada implementasi form yang kompleks, validasi data real-time, dan navigasi antar layar.

## 🚀 Fitur Utama

Aplikasi ini mencakup alur pendaftaran lengkap yang terdiri dari 5 layar utama:

1.  **Halaman Awal (Landing)**: Sambutan menarik dengan logo aplikasi dan navigasi Login/Sign Up.
2.  **Login & Registrasi**: Sistem autentikasi sederhana dengan validasi input wajib diisi.
3.  **Halaman Utama (Home)**: Menampilkan nama user dan akses cepat ke pendaftaran seminar.
4.  **Form Pendaftaran Lengkap**:
    *   Input Nama, Email, dan Nomor HP.
    *   Pemilihan Jenis Kelamin menggunakan **RadioButton**.
    *   Pemilihan Tipe Seminar menggunakan **Dropdown Menu (Spinner)**.
    *   Checkbox Persetujuan data.
5.  **Halaman Hasil**: Menampilkan resume data pendaftaran setelah berhasil disubmit.

## 🛠️ Validasi Data (Sesuai Ketentuan UTS)

Aplikasi menerapkan sistem validasi ketat untuk memastikan integritas data:
*   **Wajib Diisi**: Semua field dalam form tidak boleh kosong.
*   **Validasi Email**: Harus mengandung karakter `@`.
*   **Validasi Nomor HP**:
    *   Hanya menerima input angka.
    *   Panjang karakter antara 10–13 digit.
    *   Wajib diawali dengan prefix `08`.
*   **Real-time Feedback**: Pesan error muncul seketika di bawah field saat user mengetik jika data tidak valid.
*   **Dialog Konfirmasi**: Muncul alert dialog sebelum data benar-benar dikirim untuk memastikan akurasi data.

## 🎨 Desain & UI/UX

*   **Material Design 3**: Menggunakan komponen UI modern seperti `Card`, `TopAppBar`, dan `ExposedDropdownMenu`.
*   **Palet Warna Konsisten**: Dominasi warna *Teal Primary* dan *Green Secondary* untuk kesan profesional dan bersih.
*   **Responsif**: Layout yang didukung oleh `verticalScroll` sehingga nyaman digunakan di berbagai ukuran layar.

## ⚙️ Teknologi yang Digunakan

*   **Bahasa**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Design System**: Material Design 3 (M3)
*   **Iconography**: Material Symbols (Extended Icons)

## 📸 Cara Menjalankan

1.  Clone repository ini:
    
