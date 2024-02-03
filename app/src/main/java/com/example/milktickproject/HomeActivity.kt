package com.example.milktickproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity : AppCompatActivity() {

    private lateinit var mDbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var offersHorizontaladapter: OffersHorizontalAdapter
    private lateinit var productsList: ArrayList<Product>
    private lateinit var offersList: ArrayList<Offer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mDbRef = FirebaseDatabase.getInstance().reference

        val namaste = findViewById<TextView>(R.id.NamasteUser)
        var products = findViewById<TextView>(R.id.categories)
        var offers = findViewById<TextView>(R.id.offers)
        var wallet = findViewById<TextView>(R.id.CV1)
        var subscription = findViewById<TextView>(R.id.CV2)
        var holidays = findViewById<TextView>(R.id.CV4)
        var cart = findViewById<ImageView>(R.id.cartImg)

        getUserName{userName ->
            namaste.text = "Namaste! $userName"
        }

        products.setOnClickListener{
            val intentProducts = Intent(this, ProductsActivity::class.java)
            startActivity(intentProducts)
        }
        offers.setOnClickListener {
            val intentOffers = Intent(this, OffersActivity::class.java)
            startActivity(intentOffers)
        }
        wallet.setOnClickListener {
            val intentWallet = Intent(this, WalletActivity::class.java)
            startActivity(intentWallet)
        }
        cart.setOnClickListener{
            val intentCart = Intent(this, CartActivity::class.java)
            startActivity(intentCart)
        }
        subscription.setOnClickListener{
            val intentSubscription = Intent(this, SubscriptionActivity::class.java)
            startActivity(intentSubscription)
        }
        holidays.setOnClickListener{
            val intentHolidays = Intent(this, HolidaysActivity::class.java)
            startActivity(intentHolidays)
        }

        productsList = arrayListOf()
//        Recycler View for categories
        recyclerView = findViewById(R.id.categoriesRecyclerView)
        categoriesAdapter = CategoriesAdapter(productsList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = categoriesAdapter


        //change here child("Product") to child("Categories")
        mDbRef.child("Product").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productsList.clear()
                for(postSnapshot in snapshot.children){
                    val product = postSnapshot.getValue(Product::class.java)
                    if(product!=null){
                        productsList.add(product)
                    }
                }
                categoriesAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




        offersList = arrayListOf()
//        Recycler View for offers
        recyclerView = findViewById(R.id.offersRecyclerView)
        offersHorizontaladapter = OffersHorizontalAdapter(offersList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = offersHorizontaladapter


        mDbRef.child("Offer").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                offersList.clear()
                for (postSnapshot in snapshot.children) {
                    val offer = postSnapshot.getValue(Offer::class.java)
                    if (offer != null) {
                        offersList.add(offer)
                    }
                }
                offersHorizontaladapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error, e.g., display a toast or log the error
            }
        })

    }

    private fun getUserName(callback: (String) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("user").child(uid).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var userName = snapshot.value as String
                callback.invoke(userName)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error, e.g., display a toast or log the error
            }
        })
    }
}
