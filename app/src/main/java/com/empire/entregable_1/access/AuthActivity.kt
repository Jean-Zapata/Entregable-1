package com.empire.entregable_1.access

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.MainActivity
import com.empire.entregable_1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var goToSignUp: TextView
    private lateinit var forgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        iniciarComponentes()
        setupListeners()
    }

    private fun iniciarComponentes() {
        emailEditText = findViewById(R.id.eMail)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        goToSignUp = findViewById(R.id.goToSignUp)
        forgotPassword = findViewById(R.id.forgotPassword)
    }

    private fun setupListeners() {
        loginButton.setOnClickListener { loginUser() }
        goToSignUp.setOnClickListener { goToRegister() }
        forgotPassword.setOnClickListener { resetPassword() }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Autenticar al usuario con Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    // Navegar a la actividad principal (por ejemplo, MainActivity)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra la actividad actual para que el usuario no pueda volver atrás
                } else {
                    // Si el inicio de sesión falla, mostrar un mensaje de error
                    Toast.makeText(
                        this,
                        "Error al iniciar sesión: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para que el usuario no pueda volver atrás
    }

    private fun resetPassword() {
        val email = emailEditText.text.toString().trim()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Enviar correo de restablecimiento de contraseña
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Correo de restablecimiento enviado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error al enviar el correo: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}