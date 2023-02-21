package com.example.myapplication

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.EditText
import android.graphics.Bitmap
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import android.provider.MediaStore
import android.os.Environment
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var firstName: String? = null
    private var lastName: String? = null

    private var firstNameET: EditText? = null
    private var lastNameET: EditText? = null
    private var submitButton: Button? = null
    private var cameraButton: Button? = null

    private var thumbnailImage: Bitmap? = null
    private var thumbnailIV: ImageView? = null
    private var displayIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submitButton = findViewById<View>(R.id.submitButton) as Button
        cameraButton = findViewById<View>(R.id.picButton) as Button
        thumbnailIV = findViewById<View>(R.id.imageView) as ImageView

        submitButton!!.setOnClickListener(this)
        cameraButton!!.setOnClickListener(this)

        displayIntent = Intent(this, LoggedInActivity::class.java)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.submitButton -> {
                // get first name string from edit text
                firstNameET = findViewById<View>(R.id.editTextTextFirstName) as EditText
                firstName = firstNameET!!.text.toString()
                // get last name string from edit text
                lastNameET = findViewById<View>(R.id.editTextTextLastName) as EditText
                lastName = lastNameET!!.text.toString()

                //check if either of the edit texts are empty
                if (firstName.isNullOrBlank() || lastName.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, "Enter both first and last name!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Logged in!", Toast.LENGTH_SHORT).show()

                    // pass the first and last name strings to new activity
                    displayIntent!!.putExtra("FN_DATA", firstName)
                    displayIntent!!.putExtra("LN_DATA", lastName)
                    startActivity(displayIntent) //explicit intent
                    }
                }
                R.id.picButton -> {

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    cameraLauncher.launch(cameraIntent)
                }catch(ex:ActivityNotFoundException){
                }
            }
        }
    }
    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val extras = result.data!!.extras
            thumbnailImage = extras!!["data"] as Bitmap?

            if (isExternalStorageWritable) {
                val filePathString = saveImage(thumbnailImage)
                val thumbnailImage = BitmapFactory.decodeFile(filePathString)
                if (thumbnailImage != null) {
                    thumbnailIV!!.setImageBitmap(thumbnailImage)
                }
            } else {
                Toast.makeText(this, "External storage not writable.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImage(finalBitmap: Bitmap?): String {
        val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Thumbnail_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(this, "file saved!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }
}