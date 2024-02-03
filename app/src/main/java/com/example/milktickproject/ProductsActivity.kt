package com.example.milktickproject


import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class ProductsActivity : AppCompatActivity() {


    private lateinit var productsList: ArrayList<Product>
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)


        mDbRef = FirebaseDatabase.getInstance().reference


        val listView = findViewById<ListView>(R.id.productslistView)
        productsList = arrayListOf()
        productsAdapter = ProductsAdapter(this, productsList)
        listView.adapter = productsAdapter


        mDbRef.child("Product").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productsList.clear()


                for (postSnapshot in snapshot.children) {
                    val product = postSnapshot.getValue(Product::class.java)
                    if (product!=null) {
                        productsList.add(product)
                    }
                }
                productsAdapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle the error, e.g., display a toast or log the error
            }
        })
    }
}
