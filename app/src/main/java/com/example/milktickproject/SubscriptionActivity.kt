package com.example.milktickproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubscriptionActivity : AppCompatActivity() {

    private lateinit var subscriptionList: ArrayList<Product>
    private lateinit var subscriptionAdapter: SubscriptionAdapter
    private lateinit var mDbRef: DatabaseReference
    private var uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        mDbRef = FirebaseDatabase.getInstance().reference

        val tv1 = findViewById<TextView>(R.id.TextView1)
        val tv2 = findViewById<TextView>(R.id.TextView2)
        val btnOrder = findViewById<Button>(R.id.subscriptionbtnOrder)
        val btnSubscription = findViewById<Button>(R.id.gotoCart)
        val btnProducts = findViewById<Button>(R.id.gotoProducts)


        val listView = findViewById<ListView>(R.id.subscriptionlistView)
        subscriptionList = arrayListOf()
        subscriptionAdapter = SubscriptionAdapter(this, subscriptionList)
        listView.adapter = subscriptionAdapter


        mDbRef.child("Product").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                subscriptionList.clear()


                for(postSnapshot in snapshot.children){
                    val product = postSnapshot.getValue(Product::class.java)
                    if(product != null && product.SUBSCRIBE!![uid] == true)//Add here condition qty!=0
                    {
                        subscriptionList.add(product)
                    }
                }
                subscriptionAdapter.notifyDataSetChanged()
                if (subscriptionList.isEmpty()){
                    listView.visibility = View.GONE
                    tv1.visibility = View.VISIBLE
                    tv2.visibility = View.VISIBLE
                    btnProducts.visibility = View.VISIBLE
                } else{
                    listView.visibility = View.VISIBLE
                    tv1.visibility = View.GONE
                    tv2.visibility = View.GONE
                    btnProducts.visibility = View.GONE
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

        btnOrder.setOnClickListener {
            Toast.makeText(this, "Ordered Successfully", Toast.LENGTH_SHORT).show()
        }


        btnSubscription.setOnClickListener {
            val intentCart = Intent(this, CartActivity::class.java)
            intentCart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentCart)
        }

        btnProducts.setOnClickListener {
            val intentProducts = Intent(this, ProductsActivity::class.java)
            intentProducts.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentProducts)
        }

    }
}