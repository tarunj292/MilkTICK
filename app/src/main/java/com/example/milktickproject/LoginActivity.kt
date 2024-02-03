package com.example.milktickproject


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //Remember this is used to remove ActionBar for user on this activity which you have changed from Themes
        supportActionBar?.hide()


        mAuth = FirebaseAuth.getInstance()


        val getEmail = findViewById<TextInputEditText>(R.id.Lemail)
        val getPassword = findViewById<TextInputEditText>(R.id.Lpassword)
        val btnRegister = findViewById<TextView>(R.id.RegisterNow)
        val btn = findViewById<Button>(R.id.Loginbtn)


        btnRegister.setOnClickListener {
            val intentRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentRegister)
        }

        btn.setOnClickListener {
            val email = getEmail.text.toString()
            val password = getPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            }
            else {
                login(email, password)
                getEmail.text = null
                getPassword.text = null
            }
        }
    }
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
