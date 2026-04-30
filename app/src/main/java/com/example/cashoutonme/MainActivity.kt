package com.example.cashoutonme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashoutonme.ui.theme.CashoutOnMeTheme

// Theme colors
object AppPalette {
    val TealPrimary = Color(0xFF004D56) 
    val GreenSecondary = Color(0xFF8BC34A) 
    val BackgroundLight = Color(0xFFF0F7F8)
    val SurfaceWhite = Color(0xFFFFFFFF)
    val ErrorRed = Color(0xFFB00020)
}

enum class Screen {
    Landing, Login, Register, Home, Form, Result
}

data class SeminarRegistrationData(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: String = "",
    val seminarType: String = ""
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashoutOnMeTheme {
                var currentScreen by remember { mutableStateOf(Screen.Landing) }
                var loggedInUserName by remember { mutableStateOf("User") }
                var registrationResult by remember { mutableStateOf(SeminarRegistrationData()) }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentScreen != Screen.Landing && currentScreen != Screen.Login && currentScreen != Screen.Register) {
                            @OptIn(ExperimentalMaterial3Api::class)
                            TopAppBar(
                                title = { 
                                    Text(
                                        "Seminar Registration App",
                                        fontWeight = FontWeight.Bold, 
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                },
                                navigationIcon = {
                                    if (currentScreen != Screen.Home) {
                                        IconButton(onClick = { 
                                            currentScreen = when(currentScreen) {
                                                Screen.Form -> Screen.Home
                                                Screen.Result -> Screen.Home
                                                else -> Screen.Home
                                            }
                                        }) {
                                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppPalette.TealPrimary)
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(AppPalette.BackgroundLight)
                        .padding(if (currentScreen == Screen.Landing || currentScreen == Screen.Login || currentScreen == Screen.Register) PaddingValues(0.dp) else innerPadding)) {
                        when (currentScreen) {
                            Screen.Landing -> LandingScreen(
                                onNavigateToLogin = { currentScreen = Screen.Login },
                                onNavigateToRegister = { currentScreen = Screen.Register }
                            )
                            Screen.Login -> LoginScreen(
                                onLoginSuccess = { username -> 
                                    loggedInUserName = username
                                    currentScreen = Screen.Home 
                                },
                                onNavigateToRegister = { currentScreen = Screen.Register },
                                onBack = { currentScreen = Screen.Landing }
                            )
                            Screen.Register -> RegisterScreen(
                                onRegisterSuccess = { currentScreen = Screen.Login },
                                onBack = { currentScreen = Screen.Landing }
                            )
                            Screen.Home -> HomeScreen(
                                userName = loggedInUserName,
                                onNavigateToForm = { currentScreen = Screen.Form }
                            )
                            Screen.Form -> FormPendaftaranScreen(
                                onRegistrationSuccess = { data ->
                                    registrationResult = data
                                    currentScreen = Screen.Result
                                }
                            )
                            Screen.Result -> HalamanHasilScreen(
                                data = registrationResult,
                                onBackToHome = { currentScreen = Screen.Home }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.logo_seminar),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun ModernButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppPalette.TealPrimary,
    contentColor: Color = Color.White,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(if(enabled) 4.dp else 0.dp, RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null, tint = AppPalette.TealPrimary) } },
            shape = RoundedCornerShape(16.dp),
            isError = error != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppPalette.TealPrimary,
                focusedLabelColor = AppPalette.TealPrimary,
                unfocusedBorderColor = Color.LightGray,
                errorBorderColor = AppPalette.ErrorRed
            )
        )
        if (error != null) {
            Text(
                text = error,
                color = AppPalette.ErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun LandingScreen(onNavigateToLogin: () -> Unit, onNavigateToRegister: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, AppPalette.BackgroundLight)))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(60.dp))
        AppLogo(Modifier.size(180.dp))
        Spacer(Modifier.height(40.dp))
        Text("Seminar App", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = AppPalette.TealPrimary)
        Spacer(Modifier.height(24.dp))
        Text(
            "Temukan seminar terbaik untuk karir Anda.",
            fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.Gray
        )
        Spacer(Modifier.weight(1f))
        ModernButton("LOG IN", onNavigateToLogin)
        Spacer(Modifier.height(16.dp))
        ModernButton("SIGN UP", onNavigateToRegister, containerColor = Color.White, contentColor = AppPalette.TealPrimary)
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit, onNavigateToRegister: () -> Unit, onBack: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(AppPalette.SurfaceWhite).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.Close, contentDescription = "Close", tint = AppPalette.TealPrimary) }
        }
        Spacer(Modifier.height(20.dp))
        AppLogo(Modifier.size(120.dp))
        Spacer(Modifier.height(40.dp))
        Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
        Spacer(Modifier.height(40.dp))
        ValidatedTextField(username, { username = it }, "Username", leadingIcon = Icons.Default.Person)
        Spacer(Modifier.height(16.dp))
        ValidatedTextField(password, { password = it }, "Password", leadingIcon = Icons.Default.Lock, visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(40.dp))
        ModernButton("LOG IN", { if(username.isNotEmpty()) onLoginSuccess(username) })
        Spacer(Modifier.weight(1f))
        Row {
            Text("Belum punya akun? ", color = Color.Gray)
            Text("Sign Up", color = AppPalette.TealPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToRegister() })
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onBack: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppPalette.TealPrimary) }
        Spacer(Modifier.height(20.dp))
        Text("Buat Akun", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
        Spacer(Modifier.height(24.dp))
        ValidatedTextField(username, { username = it }, "Username", leadingIcon = Icons.Default.Person)
        Spacer(Modifier.height(16.dp))
        ValidatedTextField(email, { email = it }, "Email", leadingIcon = Icons.Default.Email)
        Spacer(Modifier.height(16.dp))
        ValidatedTextField(password, { password = it }, "Password", leadingIcon = Icons.Default.Lock, visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(40.dp))
        ModernButton("REGISTER", { if(username.isNotEmpty()) onRegisterSuccess() })
    }
}

@Composable
fun HomeScreen(userName: String, onNavigateToForm: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                AppLogo(Modifier.size(100.dp))
                Spacer(Modifier.height(24.dp))
                Text("Halo, $userName!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
                Spacer(Modifier.height(12.dp))
                Text("Siap untuk menambah ilmu hari ini?", color = Color.Gray, textAlign = TextAlign.Center)
                Spacer(Modifier.height(40.dp))
                ModernButton("Daftar Seminar", onNavigateToForm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPendaftaranScreen(onRegistrationSuccess: (SeminarRegistrationData) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var seminarType by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    // Errors
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var genderError by remember { mutableStateOf<String?>(null) }
    var seminarError by remember { mutableStateOf<String?>(null) }
    var showCheckboxWarning by remember { mutableStateOf(false) }

    var showConfirmDialog by remember { mutableStateOf(false) }
    
    val seminarOptions = listOf("Android Development", "UI/UX Design", "Digital Marketing", "Cyber Security", "Data Science")
    var expanded by remember { mutableStateOf(false) }

    // Validation Logic
    fun validate(): Boolean {
        var isValid = true
        if (name.isBlank()) { nameError = "Nama wajib diisi"; isValid = false } else nameError = null
        
        if (email.isBlank()) { emailError = "Email wajib diisi"; isValid = false }
        else if (!email.contains("@")) { emailError = "Email harus mengandung @"; isValid = false }
        else emailError = null
        
        if (phone.isBlank()) { phoneError = "Nomor HP wajib diisi"; isValid = false }
        else if (!phone.all { it.isDigit() }) { phoneError = "Nomor HP hanya boleh angka"; isValid = false }
        else if (phone.length < 10 || phone.length > 13) { phoneError = "Panjang 10–13 digit"; isValid = false }
        else if (!phone.startsWith("08")) { phoneError = "Harus diawali dengan 08"; isValid = false }
        else phoneError = null
        
        if (gender.isEmpty()) { genderError = "Pilih jenis kelamin"; isValid = false } else genderError = null
        if (seminarType.isEmpty()) { seminarError = "Pilih seminar"; isValid = false } else seminarError = null
        
        if (!isAgreed) { showCheckboxWarning = true; isValid = false } else showCheckboxWarning = false
        
        return isValid
    }

    // Real-time validation effects
    LaunchedEffect(name) { if(name.isNotEmpty()) nameError = null }
    LaunchedEffect(email) { if(email.contains("@")) emailError = null }
    LaunchedEffect(phone) { 
        if(phone.startsWith("08") && phone.length in 10..13 && phone.all { it.isDigit() }) phoneError = null 
    }
    LaunchedEffect(gender) { if(gender.isNotEmpty()) genderError = null }
    LaunchedEffect(seminarType) { if(seminarType.isNotEmpty()) seminarError = null }
    LaunchedEffect(isAgreed) { if(isAgreed) showCheckboxWarning = false }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Form Pendaftaran", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
        
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ValidatedTextField(name, { name = it }, "Nama", nameError, leadingIcon = Icons.Default.Badge)
                ValidatedTextField(email, { email = it }, "Email", emailError, leadingIcon = Icons.Default.Email, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                ValidatedTextField(phone, { phone = it }, "Nomor HP", phoneError, leadingIcon = Icons.Default.Phone, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                
                Text("Jenis Kelamin", fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = gender == "Laki-laki", onClick = { gender = "Laki-laki" })
                    Text("Laki-laki")
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = gender == "Perempuan", onClick = { gender = "Perempuan" })
                    Text("Perempuan")
                }
                if(genderError != null) Text(genderError!!, color = AppPalette.ErrorRed, fontSize = 12.sp)
                
                Text("Pilihan Seminar", fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = seminarType,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Pilih Seminar") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        isError = seminarError != null
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        seminarOptions.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { seminarType = option; expanded = false })
                        }
                    }
                }
                if(seminarError != null) Text(seminarError!!, color = AppPalette.ErrorRed, fontSize = 12.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAgreed, onCheckedChange = { isAgreed = it })
                    Text("Saya menyetujui data yang diinput benar", fontSize = 13.sp)
                }
                if(showCheckboxWarning) Text("Harap centang persetujuan", color = AppPalette.ErrorRed, fontSize = 12.sp)
                
                Spacer(Modifier.height(16.dp))
                ModernButton("SUBMIT", { if(validate()) showConfirmDialog = true })
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah data yang Anda isi sudah benar?") },
            confirmButton = {
                TextButton(onClick = { 
                    showConfirmDialog = false
                    onRegistrationSuccess(SeminarRegistrationData(name, email, phone, gender, seminarType))
                }) { Text("YA") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("TIDAK") }
            }
        )
    }
}

@Composable
fun HalamanHasilScreen(data: SeminarRegistrationData, onBackToHome: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(80.dp), tint = AppPalette.GreenSecondary)
                Spacer(Modifier.height(16.dp))
                Text("Pendaftaran Berhasil", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
                
                Divider(Modifier.padding(vertical = 24.dp), color = Color.LightGray)
                
                InfoRow("Nama", data.name)
                InfoRow("Email", data.email)
                InfoRow("Nomor HP", data.phone)
                InfoRow("Jenis Kelamin", data.gender)
                InfoRow("Seminar", data.seminarType)
                
                Spacer(Modifier.height(40.dp))
                ModernButton("KEMBALI KE BERANDA", onBackToHome)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = AppPalette.TealPrimary)
    }
}
