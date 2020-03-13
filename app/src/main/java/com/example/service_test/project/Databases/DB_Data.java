package com.example.service_test.project.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.service_test.R;
import com.example.service_test.project.Models.Employee;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DB_Data {

    private Context mContext;

    // Table Names
    private static final String TABLE_DATA = "employee";
    private static final String KEY = "id_sqlite";
    private static final String KEY_ID = "id";
    private static final String KEY_EMPLOYEE_NAME = "employee_name";
    private static final String KEY_EMPLOYEE_SALARY = "employee_salary";
    private static final String KEY_EMPLOYEE_AGE = "employee_age";
    private static final String KEY_EMPLOYEE_IMAGE = "profile_image";

    private static final int DATABASE_VERSION = 17;
    private static final String DATABASE_NAME = "employee.db";

    private SQLiteDatabase bdd;
    private DB_Data_BaseSQLite BaseSQLite;

    //Création de la table
    public DB_Data(Context context){
        BaseSQLite = new DB_Data_BaseSQLite(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext=context;
    }

    //Ouverture de la base en écriture
    public void open(){
        if (bdd==null || !bdd.isOpen()) bdd = BaseSQLite.getWritableDatabase();
    }
    public void openRead(){
        bdd = BaseSQLite.getReadableDatabase();
    }

    //Fermeture de l'accès à la base
    public void close(){
        if (bdd!=null && bdd.isOpen()) bdd.close();
    }

    //Insertion de l'element bon d'achat
    public boolean insertEnregistrementBonAchat (Employee Elements){

        if (bdd==null || !bdd.isOpen()) bdd = BaseSQLite.getWritableDatabase();
        bdd.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, Elements.getId());
        values.put(KEY_EMPLOYEE_NAME, Elements.getEmployee_name());
        values.put(KEY_EMPLOYEE_SALARY, Elements.getSalary());
        values.put(KEY_EMPLOYEE_AGE, Elements.getAge());


        bdd.insert(TABLE_DATA, null, values);

        //Insertion de l'element dans la BDD via le ContentValues
        bdd.setTransactionSuccessful();
        bdd.endTransaction();

        return true;

    }

    //Insertion de la liste bons d'achats
    public int insertEnregistrementBonAchat(List<Employee> Elements){

        if (bdd==null || !bdd.isOpen()) bdd = BaseSQLite.getWritableDatabase();
        bdd.beginTransaction();
        ContentValues values = new ContentValues();
        int iValeur = 0;

        for (int i = 0; i < Elements.size(); i++) {


            values.put(KEY_ID, Elements.get(i).getId());
            values.put(KEY_EMPLOYEE_NAME, Elements.get(i).getEmployee_name());
            values.put(KEY_EMPLOYEE_SALARY, Elements.get(i).getSalary());
            values.put(KEY_EMPLOYEE_AGE, Elements.get(i).getAge());
            values.put(KEY_EMPLOYEE_IMAGE, Elements.get(i).getImage());

            bdd.insert(TABLE_DATA, null, values);
        }

        //Insertion de l'element dans la BDD via le ContentValues
        bdd.setTransactionSuccessful();
        bdd.endTransaction();

        return iValeur;

    }

    //Recuperation de la liste bons d'achats
    public List<Employee> getAllBonAchats() {

        List<Employee> dataList = new ArrayList<Employee>();
        String selectQuery = "SELECT  * FROM " + TABLE_DATA;

        Cursor cursor = bdd.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Employee data = new Employee();

                data.setId(cursor.getString(1));
                data.setEmployee_name(cursor.getString(2));
                data.setSalary(cursor.getString(3));
                data.setAge(cursor.getString(4));
                data.setImage(cursor.getBlob(5));

                dataList.add(data);

            } while (cursor.moveToNext());
        }

        return dataList;
    }

    //Suppression du cntenu de la table bon d'achat
    public int removeEnregistrementsAchats(){
        if (bdd==null || !bdd.isOpen()) bdd = BaseSQLite.getWritableDatabase();
        return bdd.delete(TABLE_DATA, null, null);
    }

    public void updateEmployee (Employee Elements){

        if (bdd==null || !bdd.isOpen()) bdd = BaseSQLite.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(KEY_ID, Elements.getId());
        values.put(KEY_EMPLOYEE_NAME, Elements.getEmployee_name());
        values.put(KEY_EMPLOYEE_SALARY, Elements.getSalary());
        values.put(KEY_EMPLOYEE_AGE, Elements.getAge());
        values.put(KEY_EMPLOYEE_IMAGE, Elements.getImage());

        bdd.update(TABLE_DATA, values, "id=" + Elements.getId() , null);


    }

}