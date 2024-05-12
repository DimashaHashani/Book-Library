package com.example.booklibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity: Activity() {
    private lateinit var dbHelper: ContactsDatabaseHelper
    private lateinit var db: SQLiteDatabase
    private var isRotated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = ContactsDatabaseHelper(this)
        db = dbHelper.writableDatabase

        createContactsTableIfNotExists()

        val addButton = findViewById<Button>(R.id.addButton)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val numberEditText = findViewById<EditText>(R.id.numberEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val box = findViewById<LinearLayout>(R.id.box)
        val newButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        newButton.setOnClickListener {
            if (isRotated) {
                newButton.rotation = 0.0f
                isRotated = false
                box.visibility = View.GONE
            } else {
                newButton.rotation = 45.0f
                isRotated = true
                box.visibility = View.VISIBLE
            }
        }

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val number = numberEditText.text.toString()
            val email = emailEditText.text.toString()

            val values = ContentValues().apply {
                put("name", name)
                put("number", number)
                put("email", email)
            }

            db.insert("contacts", null, values)

            updateTableView()
        }

        updateTableView()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    private fun createContactsTableIfNotExists() {
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='contacts'", null)
        val tableExists = cursor.moveToFirst()
        cursor.close()

        if (!tableExists) {
            val createContactsTable = "CREATE TABLE contacts (" +
                    "_id INTEGER PRIMARY KEY," +
                    "name TEXT," +
                    "number TEXT," +
                    "email TEXT" +
                    ");"
            db.execSQL(createContactsTable)
        }
    }

    @SuppressLint("Range")
    private fun updateTableView() {
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        // Clear the current rows from the table view
        tableLayout.removeAllViews()

        // Query the database to get all rows
        val cursor = db.query("contacts", arrayOf("_id", "name", "number", "email"), null, null, null, null, null)

        // Iterate over the rows in the cursor and add them to the table view
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val number = cursor.getString(cursor.getColumnIndex("number"))
            val email = cursor.getString(cursor.getColumnIndex("email"))

            val tableRow = LayoutInflater.from(this).inflate(R.layout.table_row, null) as TableRow
            tableRow.findViewById<TextView>(R.id.nameTextView).text = name
            tableRow.findViewById<TextView>(R.id.numberTextView).text = number
            tableRow.findViewById<TextView>(R.id.emailTextView).text = email

            val removeButton = tableRow.findViewById<Button>(R.id.removeButton)
            removeButton.setOnClickListener {
                db.delete("contacts", "_id = ?", arrayOf(id.toString()))
                tableLayout.removeView(tableRow)
            }

            tableLayout.addView(tableRow)
        }
        cursor.close()
    }
}
