package com.example.virtualexpensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.virtualexpensetracker.data.CurrentUser
import com.example.virtualexpensetracker.data.UserStore
import com.example.virtualexpensetracker.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (UserStore.isUserRegistered(this, email)) {
                    if (password.length >= 6) {
                        CurrentUser.email = email

                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Must contain a num, a letter & a special char", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Account not found. Please sign up.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, SignupActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
