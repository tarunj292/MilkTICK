package com.example.milktickproject


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class OffersHorizontalAdapter(private val offersList: List<Offer>):
    RecyclerView.Adapter<OffersHorizontalAdapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val textView: TextView
        val imageView: ImageView
        init {
            textView = itemView.findViewById(R.id.offersHorizontalTextView)
            imageView = itemView.findViewById(R.id.offersHorizontalImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offers_horizontal_layout, parent, false)
        return MyViewHolder(view)
    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = offersList[position].name
        val context = holder.itemView.context

        Glide.with(context).load(offersList[position].imageResId).error(R.drawable.default_image).into(holder.imageView)




        holder.imageView.setOnClickListener {
            val intentOffers = Intent(context, OffersActivity::class.java)
            context.startActivity(intentOffers)
        }


        holder.textView.setOnClickListener {
            val intentOffers = Intent(context, OffersActivity::class.java)
            context.startActivity(intentOffers)
        }
    }


    override fun getItemCount() = offersList.size
    }