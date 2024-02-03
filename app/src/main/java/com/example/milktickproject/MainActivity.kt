package com.example.milktickproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.show()

//        this below button gives you back option never to use it on main activity/your first activity
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Title"

        //below title many times will not work because title in manifest has higher priority so instead use supportActionBar.title
        toolbar.title = "My Title"
        toolbar.subtitle = "SubTitle"
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        MenuInflater(this).inflate(R.menu.opt_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        val itemId =  item.itemId
//
//        when (itemId){
//            R.id.opt_new -> Toast.makeText(this, "Created new File", Toast.LENGTH_SHORT).show()
//            R.id.opt_open -> Toast.makeText(this, "File Opened", Toast.LENGTH_SHORT).show()
//            R.id.opt_save -> Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show()
//            android.R.id.home -> Toast.makeText(this, "Go back", Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
}