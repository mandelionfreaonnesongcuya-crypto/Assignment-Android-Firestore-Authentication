package com.example.naturespring

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        applySystemBarsPadding(findViewById(R.id.root))

        // Connects this screen to Firebase Authentication
        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvError = findViewById(R.id.tvError)
        val btnGoRegister = findViewById<Button>(R.id.btnGoRegister)

        // If we arrived from the Register screen, fill in the new email
        intent.getStringExtra("email")?.let { etEmail.setText(it) }

        // Button 1: Login
        btnLogin.setOnClickListener { attemptLogin() }

        // Button 2: go to Register screen
        btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        // Already signed in? Skip the login screen.
        if (auth.currentUser != null) goToHome()
    }

    private fun attemptLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // Validation before we ask Firebase
        if (email.isEmpty()) return showError("Email cannot be empty.")
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return showError("Please enter a valid email address.")
        if (password.isEmpty()) return showError("Password cannot be empty.")
        if (password.length < 6) return showError("Password must be at least 6 characters.")

        tvError.visibility = View.GONE
        btnLogin.isEnabled = false

        // Ask Firebase to check the email + password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btnLogin.isEnabled = true
                if (task.isSuccessful) {
                    goToHome()
                } else {
                    showError(friendlyAuthError(task.exception))
                }
            }
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
