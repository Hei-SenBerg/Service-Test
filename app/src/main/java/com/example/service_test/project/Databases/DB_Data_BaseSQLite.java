package com.example.service_test.project.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Data_BaseSQLite extends SQLiteOpenHelper {

    // Table Names
    private static final String TABLE_DATA = "employee";
    private static final String KEY = "id_sqlite";
    private static final String KEY_ID = "id";
    private static final String KEY_EMPLOYEE_NAME = "employee_name";
    private static final String KEY_EMPLOYEE_SALARY = "employee_salary";
    private static final String KEY_EMPLOYEE_AGE = "employee_age";
    private static final String KEY_EMPLOYEE_IMAGE = "profile_image";

    // bon achat table create statement
    private static final String CREATE_TABLE_BON_ACHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_DATA + "("
            + KEY + " INTEGER PRIMARY KEY,"
            + KEY_ID + " TEXT,"
            + KEY_EMPLOYEE_NAME + " TEXT,"
            + KEY_EMPLOYEE_SALARY + " TEXT,"
            + KEY_EMPLOYEE_AGE + " TEXT,"
            + KEY_EMPLOYEE_IMAGE + " TEXT" + ")";



    public DB_Data_BaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BON_ACHAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        // create new tables
        onCreate(db);
    }

}