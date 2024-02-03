package com.example.milktickproject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.Locale

class HolidaysActivity : AppCompatActivity() {

    private lateinit var holidaysList: ArrayList<String>
    private lateinit var holidaysAdapter: HolidaysAdapter
    private lateinit var mDbRef: DatabaseReference
    private val calendar = Calendar.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidays)

        mDbRef = FirebaseDatabase.getInstance().reference


        var noDate1 = findViewById<TextView>(R.id.TextView1)
        var noDate2 = findViewById<TextView>(R.id.TextView2)
        var showDatePickerBtn = findViewById<Button>(R.id.btnShowDatePicker)

        val listView = findViewById<ListView>(R.id.holidaysListView)
        holidaysList = arrayListOf()
        holidaysAdapter = HolidaysAdapter(this, holidaysList)
        listView.adapter = holidaysAdapter

        mDbRef.child("user").child(uid).child("holidays").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holidaysList.clear()
                for(holidays in snapshot.children){
                    val dates = holidays.value as String

                    val isDateBeforeToday = isDateBeforeToday(dates)
                    if (!isDateBeforeToday) {
                        holidaysList.add(dates)
                    }
                }
                holidaysAdapter.notifyDataSetChanged()
                if(holidaysList.isEmpty()){
                    listView.visibility = View.GONE
                    noDate1.visibility = View.VISIBLE
                    noDate2.visibility = View.VISIBLE
                } else{
                    listView.visibility = View.VISIBLE
                    noDate1.visibility = View.GONE
                    noDate2.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        showDatePickerBtn.setOnClickListener {
            showDatePicker()
        }
    }

    private fun isDateBeforeToday(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            val dateToCompare = dateFormat.parse(dateString)

            val todaysDate = Calendar.getInstance().time

            return dateToCompare!!.before(todaysDate)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun showDatePicker() {
        var datePickerDialog = DatePickerDialog(
            this,
            { _, year:Int, monthOfYear: Int, dayOfMonth: Int ->

                var selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                var dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
                var formattedDate = dateFormat.format(selectedDate.time)

                storeSelectedDateInSet(formattedDate)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000
        datePickerDialog.show()
    }

    private fun storeSelectedDateInSet(selectedDate: String) {
        mDbRef.child("user").child(uid).child("holidays").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val selectedDatesSet = mutableSetOf<String>()
                for(dateSnapshot in snapshot.children) {
                    if(dateSnapshot != null){
                        val date = dateSnapshot.getValue(String::class.java)
                        selectedDatesSet.add(date!!)
                    }
                }
                selectedDatesSet.add(selectedDate)
                var selectedDatesList = sortDates(selectedDatesSet)
                storeDateListInDb(selectedDatesList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sortDates(dateStrings: Set<String>): List<String> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateObjects = dateStrings.map { dateFormat.parse(it) }
        val sortedDates = dateObjects.sortedWith { date1, date2 ->
            date1!!.compareTo(date2!!)
        }
        val sortedDateStrings = sortedDates.map { dateFormat.format(it) }
        return sortedDateStrings
    }

    private fun storeDateListInDb(selectedDatesList: List<String>) {
        val datesRef = mDbRef.child("user").child(uid).child("holidays").setValue(selectedDatesList)
    }

}