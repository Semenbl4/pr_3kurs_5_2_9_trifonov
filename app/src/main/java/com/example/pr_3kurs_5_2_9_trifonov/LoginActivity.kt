package com.example.pr_3kurs_5_2_9_trifonov

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("user_json")) {
            openSearchScreen()
        }

        btnLogin.setOnClickListener {
            val loginText = etLogin.text.toString().trim()
            val passwordText = etPassword.text.toString().trim()

            if (loginText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(login = loginText, password = passwordText)

                val gson = Gson()
                val userJson = gson.toJson(user)

                sharedPreferences.edit().putString("user_json", userJson).apply()

                Toast.makeText(this, "Авторизация успешна!", Toast.LENGTH_SHORT).show()
                openSearchScreen()
            }
        }
    }

    private fun openSearchScreen() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
        finish()
    }
}