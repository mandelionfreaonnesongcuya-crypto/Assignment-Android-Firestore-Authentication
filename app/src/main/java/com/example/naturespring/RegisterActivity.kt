package com.example.naturespring

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirm: EditText
    private lateinit var btnCreate: Button
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        applySystemBarsPadding(findViewById(R.id.root))

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etRegEmail)
        etPassword = findViewById(R.id.etRegPassword)
        etConfirm = findViewById(R.id.etRegConfirm)
        btnCreate = findViewById(R.id.btnCreateAccount)
        tvError = findViewById(R.id.tvRegError)
        val btnBack = findViewById<Button>(R.id.btnBackToLogin)

        // Button 3: Create Account
        btnCreate.setOnClickListener { attemptRegister() }

        // Button 4: back to Login screen
        btnBack.setOnClickListener { finish() }
    }

    private fun attemptRegister() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirm = etConfirm.text.toString()

        if (email.isEmpty()) return showError("Email cannot be empty.")
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return showError("Please enter a valid email address.")
        if (password.length < 6) return showError("Password must be at least 6 characters.")
        if (password != confirm) return showError("Passwords do not match.")

        tvError.visibility = View.GONE
        btnCreate.isEnabled = false

        // Ask Firebase to create the new user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btnCreate.isEnabled = true
                if (task.isSuccessful) {
                    // Firebase signs the new user in automatically.
                    // Sign out so the user can demonstrate logging in.
                    auth.signOut()
                    Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("email", email)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    showError(friendlyAuthError(task.exception))
                }
            }
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}
