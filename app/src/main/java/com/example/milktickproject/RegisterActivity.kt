package com.example.milktickproject


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        val getName = findViewById<TextInputEditText>(R.id.Rname)
        val getEmail = findViewById<TextInputEditText>(R.id.Remail)
        val getPassword = findViewById<TextInputEditText>(R.id.Rpassword)
        val btnLogin = findViewById<TextView>(R.id.loginNow)
        val btn = findViewById<Button>(R.id.Rbtn)


        btnLogin.setOnClickListener{
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }


        mAuth = FirebaseAuth.getInstance()


        btn.setOnClickListener {
            val email = getEmail.text.toString()
            val password = getPassword.text.toString()
            val name = getName.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            }
            else {
                validateUserName(name, email, password)
                getName.text = null
                getEmail.text = null
                getPassword.text = null
            }
        }
    }

    private fun validateUserName(name: String, email: String, password: String) {
            val usernamePattern = Pattern.compile("^[a-zA-Z ]{3,}$")
            if (usernamePattern.matcher(name).matches()) {
                createUser(name, email, password)
            } else {
                Toast.makeText(this, "Invalid username. Please use only letters and spaces, and name must be at least 3 characters long.", Toast.LENGTH_SHORT).show()
            }
    }


    private fun createUser(name: String, email: String, password: String) {


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->

            if(task.isSuccessful){
                addUserToDatabase(name, email, mAuth.currentUser?.uid!!)

                val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                finish()
                startActivity(intent)
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Your createUser failed",Toast.LENGTH_LONG).show()
        }
    }


    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        val balance = "0"
        mDbRef.child("user").child(uid).setValue(User(name, email, uid, balance))

        mDbRef = FirebaseDatabase.getInstance().getReference("Product")
        mDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productSnapshot in dataSnapshot.children) {
                    val productName = productSnapshot.key
                    mDbRef.child(productName!!).child("qty").child(uid).setValue(0)
                    mDbRef.child(productName).child("SUBSCRIBE").child(uid).setValue(false)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Database Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
