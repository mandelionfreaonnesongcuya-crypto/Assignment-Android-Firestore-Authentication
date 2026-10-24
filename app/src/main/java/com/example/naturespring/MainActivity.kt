package com.example.naturespring

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/** HOME page (shown only after Firebase confirms the login). */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applySystemBarsPadding(findViewById(R.id.root))

        val user = FirebaseAuth.getInstance().currentUser

        // Not logged in? Send the user back to the login screen.
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Show who is logged in
        findViewById<TextView>(R.id.tvWelcome).text = "Welcome, ${user.email}"

        // Button 5: Info
        findViewById<Button>(R.id.btnInfo).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About NatureSpring")
                .setMessage("NatureSpring: Nature • Community • Growth.\nSigned in with Firebase Authentication.")
                .setPositiveButton("OK", null)
                .show()
        }

        // Button 6: Logout -> back to the login screen
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
