package com.example.virtualexpensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.virtualexpensetracker.data.UserStore
import com.example.virtualexpensetracker.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            val email = binding.emailEditTextSignup.text.toString().trim()
            val password = binding.passwordEditTextSignup.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (email.contains("@") && password.length >= 6) {
                    UserStore.addUser(this, email)

                    Toast.makeText(this, "Signup Successful! Please login.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Must contain a digit, a letter and a special char", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginText.setOnClickListener {
            finish()
        }
    }
}
