package com.example.booklibrary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper // Constructor
    (private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PAGES + " INTEGER)"
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "BookLibrary.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "my_library"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "book_title"
        const val COLUMN_AUTHOR = "book_author"
        const val COLUMN_PAGES = "book_pages"
    }
}
