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


class CartActivity : AppCompatActivity() {


    private lateinit var cartList: ArrayList<Product>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var uid = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference


        val listView = findViewById<ListView>(R.id.cartlistView)
        val tv1 = findViewById<TextView>(R.id.TextView1)
        val tv2 = findViewById<TextView>(R.id.TextView2)
        val btnOrder = findViewById<Button>(R.id.cartbtnOrder)
        val btnSubscription = findViewById<Button>(R.id.gotoSubscription)
        val btnProducts = findViewById<Button>(R.id.gotoProducts)


        cartList = arrayListOf()
        cartAdapter = CartAdapter(this, cartList)
        listView.adapter = cartAdapter


        mDbRef.child("Product").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()


                for(postSnapshot in snapshot.children){
                    val product = postSnapshot.getValue(Product::class.java)
                    if(product != null && product.qty!![uid]!! > 0)//Add here condition qty!=0
                    {
                        cartList.add(product)
                    }
                }
                cartAdapter.notifyDataSetChanged()
                if (cartList.isEmpty()){
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
            mDbRef = FirebaseDatabase.getInstance().getReference("Product")
            mDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var totalAmount = 0L
                    for (productSnapshot in dataSnapshot.children) {
                        val productName = productSnapshot.key
                        var product = productSnapshot.getValue(Product::class.java)
                        if(product!!.qty!![uid]!! > 0){
                            totalAmount += product.price!! * product.qty!![uid]!!
                        }
                    }
                    getUserBalance { getBalance ->
                        if(getBalance>=totalAmount){
                            var updatedBalance = getBalance-totalAmount
                            WalletActivity.updateInDb(this@CartActivity, mAuth.currentUser!!.uid, updatedBalance)
                            emptyCart()
                        }
                        else{
                            Toast.makeText(this@CartActivity, "Your balance is ${getBalance}Rs. and bill is of ${totalAmount}Rs.\nPop Up your balance by ${totalAmount-getBalance} or Remove some item(s) worth ${totalAmount-getBalance}", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

        btnSubscription.setOnClickListener {
            val intentSubscription = Intent(this, SubscriptionActivity::class.java)
            intentSubscription.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentSubscription)
        }

        btnProducts.setOnClickListener {
            val intentProducts = Intent(this, ProductsActivity::class.java)
            intentProducts.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentProducts)
        }
    }

    private fun getUserBalance(callback: (Long) -> Unit) {
        val uid = mAuth.currentUser!!.uid
        mDbRef = FirebaseDatabase.getInstance().reference.child("user").child(uid)

        mDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                var getBalance = user?.balance?.toLong() ?: 0L
                callback(getBalance)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun emptyCart() {
        mDbRef = FirebaseDatabase.getInstance().getReference("Product")
        mDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productSnapshot in dataSnapshot.children) {
                    val productName = productSnapshot.key
                    mDbRef.child(productName!!).child("qty").child(uid).setValue(0)
                        .addOnSuccessListener {
                            Toast.makeText(this@CartActivity, "Ordered Successful", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener {
                            Toast.makeText(this@CartActivity, "Fail", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@CartActivity, "Database Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


//class CartActivity : AppCompatActivity() {
//
//    private lateinit var cartList: ArrayList<Product>
//    private lateinit var cartAdapter: CartAdapter
//    private lateinit var mDbRef: DatabaseReference
//    private lateinit var mAuth: FirebaseAuth
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cart)
//
//        mAuth = FirebaseAuth.getInstance()
//        mDbRef = FirebaseDatabase.getInstance().reference.child("MilkTick")
//
//        val listView = findViewById<ListView>(R.id.cartlistView)
//        cartList = arrayListOf()
//        cartAdapter = CartAdapter(this, cartList)
//        listView.adapter = cartAdapter
//
//        mDbRef.child("Product").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                cartList.clear()
//                for(postSnapshot in snapshot.children){
//                    val product = postSnapshot.getValue(Product::class.java)
//                    if(product != null )//Add here condition qty!=0
//                    {
//                        getProductQTY(mDbRef, mAuth, product.match) { qty ->
//                            product.qty = qty
//                            cartList.add(product)
//                            cartAdapter.notifyDataSetChanged()
//                        }
//                    }
//                }
////                cartAdapter.notifyDataSetChanged() //Check without this it work or not
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//
//        val btnOrder = findViewById<Button>(R.id.cartbtnOrder)
//        val btnSubscription = findViewById<Button>(R.id.cartbtnSubscription)
//
//        btnOrder.setOnClickListener {
//            Toast.makeText(this, "Ordered Successfully", Toast.LENGTH_SHORT).show()
//        }
//        btnSubscription.setOnClickListener {
//            Toast.makeText(this, "Open", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun getProductQTY(mDbRef: DatabaseReference, mAuth: FirebaseAuth, productmatch: String?, callback: (Long) -> Unit) {
//        var productqty = 0L
//        mDbRef.child("UserOrder").child(mAuth.currentUser!!.uid).child(productmatch!!)
//            .addListenerForSingleValueEvent(object :
//                ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        if (productmatch == snapshot.key) {
//                            productqty = snapshot.value as Long
//                        }
//                        callback(productqty)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle onCancelled if needed
//                }
//            })
//    }
//}