package com.example.milktickproject

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProductsAdapter(context: Context, products: List<Product>)
    : ArrayAdapter<Product>(context, R.layout.products_layout, products) {


    private lateinit var mDbRef: DatabaseReference
    private var uid = FirebaseAuth.getInstance().currentUser!!.uid


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.products_layout, parent, false)




        val pdtName = view.findViewById<TextView>(R.id.pdtTitle)
        val pdtPrice = view.findViewById<TextView>(R.id.pdtPrice)
        val pdtImg = view.findViewById<ImageView>(R.id.pdtImg)
        val btnSubscribe = view.findViewById<Button>(R.id.pdtSubscribe)
        val btnBuy = view.findViewById<Button>(R.id.pdtBuy)
        val pdtQuantity = view.findViewById<TextView>(R.id.pdtQuantity)
        val pdtDelete = view.findViewById<ImageView>(R.id.pdtDelete)
        val pdtDropDown = view.findViewById<ImageView>(R.id.pdtDropDown)




        val product = getItem(position)
        var pdtCurrentQty = product?.qty!![uid]
        val priceToString = product.price.toString()
        val quantityString = "Qty: " + product.qty[uid].toString()
        pdtName.text = product.name
        pdtPrice.text = "$priceToString Rs."
        pdtQuantity.text = quantityString
        Glide.with(context).load(product.imageResId).error(R.drawable.default_image).into(pdtImg)


        if(product.qty[uid]!! > 0){
            btnBuy.visibility = View.GONE
            pdtDelete.visibility = View.VISIBLE
            pdtQuantity.visibility = View.VISIBLE
            pdtDropDown.visibility = View.VISIBLE
        } else{
            btnBuy.visibility = View.VISIBLE
            pdtDelete.visibility = View.GONE
            pdtQuantity.visibility = View.GONE
            pdtDropDown.visibility = View.GONE
        }
        if(product.SUBSCRIBE!![uid] == true) {
            btnSubscribe.text = context.getString(R.string.subscribed)
            btnSubscribe.setBackgroundColor(Color.RED)
        }


        btnSubscribe.setOnClickListener {
            subscribeFunction(product.SUBSCRIBE[uid], btnSubscribe, product.name, view.context)
        }




        btnBuy.setOnClickListener {
            btnBuy.visibility = View.GONE
            pdtDelete.visibility = View.VISIBLE
            pdtQuantity.visibility = View.VISIBLE
            pdtDropDown.visibility = View.VISIBLE
            pdtCurrentQty = pdtCurrentQty!! + 1
            mDbRef = FirebaseDatabase.getInstance().getReference("Product").child(product.name!!).child("qty")
            val user = mapOf<String, Any>(
                uid to pdtCurrentQty!!
            )




            mDbRef.updateChildren(user).addOnSuccessListener {
                Toast.makeText(view.context, "Successful", Toast.LENGTH_SHORT).show()




            }.addOnFailureListener {
                Toast.makeText(view.context,"Fail", Toast.LENGTH_SHORT).show()
            }
        }


        pdtDelete.setOnClickListener{
            CartAdapter.updateQuantity(pdtCurrentQty!!, product.name!!, view.context, -1)
        }


        pdtDropDown.setOnClickListener {
            CartAdapter.updateQuantity(pdtCurrentQty!!, product.name!!, view.context, 1)
        }


        return view
    }


    companion object {
        fun subscribeFunction(
            subscribe: Boolean?,
            btnSubscribe: Button,
            productName: String?,
            context: Context,
        ) {


            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val mDbRef = FirebaseDatabase.getInstance().getReference("Product")
            if (subscribe == false) {
                btnSubscribe.text = context.getString(R.string.subscribed)
                btnSubscribe.setBackgroundColor(Color.RED)
                val user = mapOf<String, Any>(
                    uid to true
                )
                mDbRef.child(productName!!).child("SUBSCRIBE").updateChildren(user).addOnSuccessListener {
                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                }
            } else if (subscribe == true) {
                btnSubscribe.text = context.getString(R.string.subscribed)


                btnSubscribe.setBackgroundColor(Color.parseColor("#4a03a6"))
                val user = mapOf<String, Any>(
                    uid to false
                )
                mDbRef.child(productName!!).child("SUBSCRIBE").updateChildren(user).addOnSuccessListener {
                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
