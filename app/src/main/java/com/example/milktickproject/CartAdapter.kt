package com.example.milktickproject


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CartAdapter(context: Context, cartList: ArrayList<Product>)
    : ArrayAdapter<Product>(context, R.layout.cart_layout, cartList) {

    private var uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false)


        val cartName = view.findViewById<TextView>(R.id.cartTitle)
        val cartPrice = view.findViewById<TextView>(R.id.cartPrice)
        val cartQuantity = view.findViewById<TextView>(R.id.cartQuantity)
        val cartDelete = view.findViewById<ImageView>(R.id.cartDelete)
        val cartDropDown = view.findViewById<ImageView>(R.id.cartDropDown)
        val cartImg = view.findViewById<ImageView>(R.id.cartImg)


        val product = getItem(position)
        val pdtCurrentQty = product?.qty!![uid]
        val priceToString = product.price.toString()
        val quantityString = "Qty: " + product.qty[uid].toString()
        cartName.text = product.name
        cartPrice.text = "$priceToString Rs."
        cartQuantity.text = quantityString
        Glide.with(context).load(product.imageResId).error(R.drawable.default_image).into(cartImg)


        cartDelete.setOnClickListener{
            updateQuantity(pdtCurrentQty!!, product.name!!, view.context, -1)
        }

        cartDropDown.setOnClickListener {
            updateQuantity(pdtCurrentQty!!, product.name!!, view.context, 1)
        }

        return view
    }

    companion object{
        fun updateQuantity(pdtCurrentQty: Long, productName: String, context: Context, change: Int) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val changeQty = pdtCurrentQty + change
            val mDbRef = FirebaseDatabase.getInstance().getReference("Product").child(productName).child("qty")

            val user = mapOf<String, Any>(
                uid to changeQty
            )

            mDbRef.updateChildren(user).addOnFailureListener {
                Toast.makeText(context,"Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
