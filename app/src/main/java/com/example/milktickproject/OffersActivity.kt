package com.example.milktickproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OffersActivity : AppCompatActivity() {

    private lateinit var offersList: ArrayList<Offer>
    private lateinit var offersAdapter: OffersAdapter
    private lateinit var mDbRef: DatabaseReference
    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers)

        mDbRef = FirebaseDatabase.getInstance().reference
        
        var listView = findViewById<ListView>(R.id.offersListView)
        offersList = arrayListOf()
        offersAdapter = OffersAdapter(this, offersList)
        listView.adapter = offersAdapter

        mDbRef.child("Offer").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                offersList.clear()


                for (postSnapshot in snapshot.children) {
                    val offer = postSnapshot.getValue(Offer::class.java)
                    if (offer != null) {
                        offersList.add(offer)
                    }
                }
                offersAdapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle the error, e.g., display a toast or log the error
            }
        })
        
    }
}