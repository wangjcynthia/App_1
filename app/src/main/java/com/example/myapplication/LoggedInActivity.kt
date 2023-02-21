package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.View

class LoggedInActivity : AppCompatActivity() {
    var nameTextView: TextView? = null
    var firstNameString: String? = null
    var lastNameString: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        // get the text view
        nameTextView = findViewById<View>(R.id.textView) as TextView
        val receivedIntent = intent

        firstNameString = receivedIntent.getStringExtra("FN_DATA")
        lastNameString = receivedIntent.getStringExtra("LN_DATA")

        //set the text view
        nameTextView!!.text = firstNameString + " " + lastNameString + " is logged in!"
    }
}