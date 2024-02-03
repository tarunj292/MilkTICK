package com.example.milktickproject


import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference


class OffersAdapter(context: Context, offers: List<Offer>) :
    ArrayAdapter<Offer>(context, R.layout.offers_layout, offers) {

    private lateinit var mDbRef: DatabaseReference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.offers_layout, parent, false)

        val ofrName = view.findViewById<TextView>(R.id.ofrName)
        val ofrDiscPrice = view.findViewById<TextView>(R.id.ofrDiscPrice)
        val ofrPrice = view.findViewById<TextView>(R.id.ofrPrice)
        val ofrImg = view.findViewById<ImageView>(R.id.ofrImg)
//        val ofrDesc = view.findViewById<ImageView>(R.id.ofrDesc)
        val btnGrab = view.findViewById<Button>(R.id.ofrGrab)

        val offer = getItem(position)
        var ofrCurrentQty = offer?.qty
//        ofrDesc.text = offer?.desc
        ofrName.text = offer?.name
        ofrPrice.text = "Rs. ${offer?.price.toString()}"
        ofrDiscPrice.text = "Rs. ${offer?.discPrice.toString()}"
        ofrPrice.paintFlags = ofrPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


        if (offer != null) {

            Glide.with(context).load(offer.imageResId).error(R.drawable.default_image).into(ofrImg)
        }


        btnGrab.setOnClickListener {
//            ofrCurrentQty = ofrCurrentQty!! + 1
//            mDbRef = FirebaseDatabase.getInstance().getReference("Offer")
//            val user = mapOf<String, Any>(
//                "qty" to ofrCurrentQty!!
//            )
//
//
//            mDbRef.child(offer!!.name!!).updateChildren(user).addOnSuccessListener {
//                Toast.makeText(view.context, "Successful", Toast.LENGTH_SHORT).show()
//
//
//            }.addOnFailureListener {
//                Toast.makeText(view.context,"Fail", Toast.LENGTH_SHORT).show()
//            }
        }


        return view

    }
}
