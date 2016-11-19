package com.dangnhat.mycontact.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dangn on 11/10/2016.
 */

public class ContactDBHelper extends SQLiteOpenHelper {

    /** Ten database */
    private static final String DATABASE_NAME = "contact_database.db";

    /*
    Database version
     */
    private static final int DATABASE_VERSION = 1;

    /* Hàm tạo của lớp ContactDbHelper */
    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tbl_contact (id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT NOT NULL, last_name TEXT NULL, avatar  BLOB NULL, favorite INT NOT NULL);");
        db.execSQL("CREATE TABLE tbl_group (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
        db.execSQL("CREATE TABLE tbl_contact_phone (id INTEGER PRIMARY KEY AUTOINCREMENT, contact_id INTEGER NOT NULL, name INTEGER NOT NULL, phone_number TEXT NOT NULL);");
        db.execSQL("CREATE TABLE tbl_contact_email (id INTEGER PRIMARY KEY AUTOINCREMENT, contact_id INTEGER NOT NULL, name INTEGER NOT NULL, email TEXT NOT NULL);");
        db.execSQL("CREATE TABLE tbl_contact_im (id INTEGER PRIMARY KEY AUTOINCREMENT, contact_id INTEGER NOT NULL, name INTEGER NOT NULL, account TEXT NOT NULL);");
        db.execSQL("CREATE TABLE tbl_contact_group (id INTEGER PRIMARY KEY AUTOINCREMENT, contact_id INTEGER NOT NULL, group_id INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_contact");
        db.execSQL("DROP TABLE IF EXISTS tbl_group");
        db.execSQL("DROP TABLE IF EXISTS tbl_contact_phone");
        db.execSQL("DROP TABLE IF EXISTS tbl_contact_email");
        db.execSQL("DROP TABLE IF EXISTS tbl_contact_im");
        db.execSQL("DROP TABLE IF EXISTS tbl_contact_group");
        onCreate(db);
    }
}
