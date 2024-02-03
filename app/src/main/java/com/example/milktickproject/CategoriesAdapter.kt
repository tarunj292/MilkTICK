package com.example.milktickproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CategoriesAdapter(private val productsList: List<Product>):
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val textView: TextView
        val imageView: ImageView
        init {
            textView = itemView.findViewById(R.id.categoriesTextView)
            imageView = itemView.findViewById(R.id.categoriesImageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_layout, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = productsList[position].name
        Glide.with(holder.itemView.context).load(productsList[position].imageResId).error(R.drawable.default_image).into(holder.imageView)


        val context = holder.itemView.context


        holder.imageView.setOnClickListener {
            val intentProducts = Intent(context, ProductsActivity::class.java)
            context.startActivity(intentProducts)
        }
        holder.textView.setOnClickListener {
            val intentProducts = Intent(context, ProductsActivity::class.java)
            context.startActivity(intentProducts)
        }


    }


    override fun getItemCount() = productsList.size
}
