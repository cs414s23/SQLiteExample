package com.example.sqliteexample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * A companion object allows to hold static fields which are common to all instances of a given
     * class.
     */
    companion object {

        // Database name
        val DATABASE_NAME = "contacts.db"

        // Version number
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1

        // Table(s) name
        val TABLE_NAME = "contact_table"

        // Column names
        val _ID = "id" // _(underscore indicates primary key), it is a convention
        val NAME = "name"
        val EMAIL = "email"
    }


    /**
     * onCreate() is called when the database is created for the first time.
     */
    override fun onCreate(db: SQLiteDatabase?) {

        // Create a table
        val SQL_CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME} (" +
                    "${_ID} INTEGER PRIMARY KEY," +
                    "${NAME} TEXT," +
                    "${EMAIL} TEXT)"

        db?.execSQL(SQL_CREATE_TABLE)
    }

    /**
     * onUpgrade() is called when the database needs to be upgraded.
     * This database is only a cache for online data, so its upgrade policy is
     * to simply to discard the data and start over
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${TABLE_NAME}"
        db?.execSQL(SQL_DELETE_TABLE)
        onCreate(db)
    }

    /**
     * our insertData() method allows to insert data to SQLIte database.
     */
    fun insertData(name: String, email: String) {

        // Gets the data repository in write mode
        val db = this.writableDatabase

        // Add new data using ContentValues which represents a series of key value pairs
        val contentValues = ContentValues()
        contentValues.put(NAME, name)
        contentValues.put(EMAIL, email)

        // Insert the new row
        db.insert(TABLE_NAME, null, contentValues)
    }

    /**
     * our updateData() methods allows to update a row with new field values.
     */
    fun updateData(id: String, name: String, email: String): Boolean {

        // Gets the data repository in write mode
        val db = this.writableDatabase

        // New value for one column
        val contentValues = ContentValues()
        contentValues.put(_ID, id)
        contentValues.put(NAME, name)
        contentValues.put(EMAIL, email)

        // Which row to update, based on id
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    /**
     * deleteData method allows to delete a given row based on the id
     */
    fun deleteData(id : String) : Int {

        // Gets the data repository in write mode
        val db = this.writableDatabase

        return db.delete(TABLE_NAME,"ID = ?", arrayOf(id))
    }

    /**
     * viewAllData() method queries the database to get all records in the database
     * The below getter property will return a Cursor containing our dataset.
     */
    val viewAllData : Cursor
        get() {
            // Gets the data repository in write mode
            val db = this.writableDatabase
            // Cursor iterates through one row at a time in the results
            val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            return cursor
        }
}



