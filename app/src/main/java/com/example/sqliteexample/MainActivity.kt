package com.example.sqliteexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    //To access your database, instantiate your subclass of SQLiteOpenHelper
    private val dbHelper = ContactDbHelper(this)

    // Create instances of EditText as global variables so that other methods can access them
    // lateinit allows us to declare a variable first and then initialize it
    // some point in the future during our program's execution cycle.
    private lateinit var idText: EditText
    private lateinit var nameText : EditText
    private lateinit var emailText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the id, name and email EditText fields from the corresponding views in the layout
        idText = findViewById(R.id.text_id)
        nameText = findViewById(R.id.text_name)
        emailText = findViewById(R.id.text_email)

    }

    /*
     * Adds/Inserts a records in the database via dbHelper class
     * This function uses the dbHelper class to interact with the database.
     */
    fun addButton(view: View) {

        if (nameText.text.isEmpty() || emailText.text.isEmpty()){
            showToast("Please enter name and email")
            return
        }
        try {
            dbHelper.insertData(nameText.text.toString(), emailText.text.toString())
            clearEditTexts()
            showToast("Successfully added a record")
        } catch (e: Exception) {
            Log.e(TAG, "error: $e")
        }
    }


    /*
     * Queries the database to get all records in the database
     * This function uses the dbHelper class to interact with the database.
     */
    fun viewAllDataButton(view: View) {

        try {
            val cursor = dbHelper.viewAllData
            if (cursor.count == 0) {
                showDialog("Error", "No record has been found")
                return
            }

            val buffer = StringBuffer()
            while (cursor.moveToNext()) {
                buffer.append("ID :" + cursor.getInt(0) + "\n")
                buffer.append("NAME :" + cursor.getString(1) + "\n")
                buffer.append("EMAIL :" + cursor.getString(2) + "\n\n")
            }
            showDialog("Data Listing", buffer.toString())
        } catch (e: Exception) {
            Log.e(TAG, "error: $e")
        }
    }


    /*
     * Deletes a record in the database based on the given ID.
     * This function uses the dbHelper class to interact with the database.
     */
    fun deleteButton(view: View) {

        if (idText.text.isEmpty()){
            showToast("An ID must be entered!")
            return
        }
        try {
            dbHelper.deleteData(idText.text.toString())
            clearEditTexts()
            showToast("Record has been deleted.")
        } catch (e: Exception){
            e.printStackTrace()
            Log.e(TAG, "error: $e")
            showToast(e.message.toString())
        }
    }


    /*
     * Updates a record in the database based on the given ID.
     * This function uses the dbHelper class to interact with the database.
     */
    fun updateButton(view: View) {

        if (idText.text.isEmpty()){
            showToast("An ID must be entered!")
            return
        }

        try {
            val isUpdated = dbHelper.updateData(
                idText.text.toString(),
                nameText.text.toString(),
                emailText.text.toString()
            )

            if (isUpdated) {
                showToast("Record Updated Successfully")
                clearEditTexts()
            } else {
                showToast("Record Not Updated")
            }

        } catch (e: Exception){
            e.printStackTrace()
            Log.e(TAG, "error: $e")
            showToast(e.message.toString())
        }
    }


    /**
     * A helper function to show Toast message
     */
    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    /*
     * show an alert dialog with data dialog.
     */
    private fun showDialog(title : String,Message : String){
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }


    /*
     * A helper function to clear our editTexts
     */
    private fun clearEditTexts(){
        idText.text.clear()
        nameText.text.clear()
        emailText.text.clear()
    }


    /*
     * Since getWritableDatabase() and getReadableDatabase() are expensive to call when the database
     * is closed, you should leave your database connection open for as long as you possibly need to access it.
     * Typically, it is optimal to close the database in the onDestroy() of the calling Activity.
     */
    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
