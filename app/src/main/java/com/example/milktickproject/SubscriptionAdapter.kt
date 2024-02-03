package com.example.milktickproject

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SubscriptionAdapter(context: Context, subscriptionList: ArrayList<Product>) :
    ArrayAdapter<Product>(context, R.layout.subscription_layout, subscriptionList) {

    private lateinit var mDbRef: DatabaseReference
    private var uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.subscription_layout, parent, false)


        mDbRef = FirebaseDatabase.getInstance().getReference("Product")

        val subscriptionName = view.findViewById<TextView>(R.id.subscriptionTitle)
        val subscriptionPrice = view.findViewById<TextView>(R.id.subscriptionPrice)
        val subscriptionBtn = view.findViewById<Button>(R.id.subscriptionBtn)
        val subscriptionImg = view.findViewById<ImageView>(R.id.subscriptionImg)


        val product = getItem(position)
        subscriptionName.text = product?.name
        subscriptionPrice.text = product?.price.toString()
        if (product != null) {
            Glide.with(context).load(product.imageResId).error(R.drawable.default_image).into(subscriptionImg)
        }
        if(product!!.SUBSCRIBE!![uid] == true) {
            subscriptionBtn.text = context.getString(R.string.subscribed)
            subscriptionBtn.setBackgroundColor(Color.RED)
        }

        subscriptionBtn.setOnClickListener{
            ProductsAdapter.subscribeFunction(product.SUBSCRIBE!![uid], subscriptionBtn, product.name, view.context)
        }


        return view
    }


}
