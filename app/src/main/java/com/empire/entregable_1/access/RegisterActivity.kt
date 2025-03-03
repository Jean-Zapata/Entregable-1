package com.empire.entregable_1.access

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.R
import com.empire.entregable_1.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // Views
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var confirmPasswordRegister: EditText
    private lateinit var registerButton: Button
    private lateinit var goToLogin: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Inicializar Firebase Auth y Database
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        iniciarComponentes()
        goToLogin.setOnClickListener { goToLogin() }
        registerButton.setOnClickListener { registerUser() }
    }

    private fun iniciarComponentes() {
        emailRegister = findViewById(R.id.emailRegister)
        passwordRegister = findViewById(R.id.passwordRegister)
        confirmPasswordRegister = findViewById(R.id.confirmPasswordRegister) // Corregido el typo
        registerButton = findViewById(R.id.registerButton)
        goToLogin = findViewById(R.id.goToLogin)
    }

    private fun goToLogin() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para que el usuario no pueda volver atrás con el botón "Back"
    }

    private fun registerUser() {
        val email = emailRegister.text.toString().trim()
        val password = passwordRegister.text.toString().trim()
        val confirmPassword = confirmPasswordRegister.text.toString().trim()

        // Validaciones
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        // Registrar al usuario en Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    val uid = auth.currentUser?.uid ?: ""
                    Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()

                    // Guardar el usuario en Firebase Realtime Database
                    saveUser(uid, email, password)
                } else {
                    // Si el registro falla, mostrar un mensaje de error
                    Toast.makeText(
                        this,
                        "Error al registrar: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveUser(uid: String, email: String, password: String) {
        // Crear un objeto Users con los datos del usuario
        val user = Users(uid, email, password)

        // Guardar el usuario en la base de datos bajo el nodo "Users" y su UID
        database.child("Users").child(uid).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show()

                    // Navegar a la actividad de inicio de sesión
                    goToLogin()
                } else {
                    Toast.makeText(
                        this,
                        "Error al guardar usuario: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}