package com.example.milktickproject

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase.*
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.Locale

class HolidaysAdapter(context: Context, holidaysList: ArrayList<String>) :
    ArrayAdapter<String>(context, R.layout.holidays_layout, holidaysList){

        private lateinit var mDbRef: DatabaseReference
        private val uid = FirebaseAuth.getInstance().currentUser!!.uid

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView?: LayoutInflater.from(context).inflate(R.layout.holidays_layout, parent, false)

            val holidaysDate = view.findViewById<TextView>(R.id.tvDate)
            val holidaysDay = view.findViewById<TextView>(R.id.tvDay)
            val holidaysDelete = view.findViewById<ImageView>(R.id.holidaysDelete)

            val dateString = getItem(position)
            holidaysDate.text = dateString
            holidaysDay.text = getDayOfWeekFromDate(dateString)

            holidaysDelete.setOnClickListener {
                mDbRef = getInstance().reference.child("user").child(uid).child("holidays")
                removeDataFromDb(mDbRef, dateString)
            }

            return view
        }

    private fun getDayOfWeekFromDate(dateString: String?): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        return daysOfWeek[dayOfWeek - 1]
    }

    private fun removeDataFromDb(mDbRef: DatabaseReference, date: String?) {
        mDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    if(postSnapshot.value == date){
                        var key = postSnapshot.key
                        mDbRef.child(key!!).removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}