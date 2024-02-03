package com.example.milktickproject


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class WalletActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)


        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference


        val btnHis = findViewById<Button>(R.id.btn1)
        val editTextAmount = findViewById<EditText>(R.id.editText)
        val add100 = findViewById<Button>(R.id.btn100)
        val add200 = findViewById<Button>(R.id.btn200)
        val add500 = findViewById<Button>(R.id.btn500)
        val addBal = findViewById<Button>(R.id.btnAdd)


        var balance = 0


        btnHis.setOnClickListener {
//            val i = Intent(this, HistoryActivity::class.java)
//            startActivity(i)
        }


        mDbRef.child("user").child(mAuth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)
                if (currentUser?.balance != null) {
                    balance = currentUser.balance!!.toInt()
                    displayBalance(balance)
                }}


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
        add100.setOnClickListener {
            updateAmount(100)
        }
        add200.setOnClickListener {
            updateAmount(200)
        }
        add500.setOnClickListener {
            updateAmount(500)
        }


        addBal.setOnClickListener {
            var getAmt = editTextAmount.text.toString()
            if (getAmt.isEmpty()) {
                Toast.makeText(this,"Enter a Amount", Toast.LENGTH_SHORT).show()
            } else {
                var amt = getAmt.toInt()
                balance+=amt
                displayBalance(balance)
            }
        }
    }


    private fun displayBalance(balance: Int) {


        val tvBal = findViewById<TextView>(R.id.tv1)


        val uid = mAuth.currentUser!!.uid
        updateInDb(this, uid, balance.toLong())
        var editTextAmount = findViewById<EditText>(R.id.editText)
        editTextAmount.setText("")
        tvBal!!.text = "Current Balance $balance Rs."
    }

    private fun updateAmount(amountToAdd: Int) {
        var editTextAmount = findViewById<EditText>(R.id.editText)
        if (editTextAmount.text.toString().isEmpty()) {
            editTextAmount.setText(amountToAdd.toString())
        } else {
            var getAmt = editTextAmount.text.toString()
            var amt = getAmt.toInt()
            amt += amountToAdd
            editTextAmount.setText("$amt")
        }


    }
    companion object{
        fun updateInDb(context: Context, uid: String, updatedBalance: Long) {
            var mDbRef = FirebaseDatabase.getInstance().reference
            val user = mapOf<String, Any>(
                "balance" to updatedBalance.toString()
            )
            mDbRef.child("user").child(uid).updateChildren(user).addOnSuccessListener {
                if (context is WalletActivity) {
                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context,"Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
