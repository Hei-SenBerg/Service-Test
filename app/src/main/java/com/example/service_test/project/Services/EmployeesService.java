package com.example.service_test.project.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.service_test.project.Databases.DB_Data;
import com.example.service_test.project.Models.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EmployeesService extends Service {
    private Context context;
    private DB_Data db;
    private List<Employee> employees;
    private List<Employee> testEmployees;


    public EmployeesService(Context context){
        super();
        this.context = context;
        db = new DB_Data(context);

    }

    // Attribut de type IBinder
    private final IBinder mBinder = new MonBinder();

    // Le Binder est représenté par une classe interne
    public class MonBinder extends Binder {
        // Le Binder possède une méthode pour renvoyer le Service
        EmployeesService getService() {
            return EmployeesService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void GetDataFromServer() {

        Thread t = new Thread() {

            @Override
            public void run() {

                db = new DB_Data(context);

                BufferedReader bufferedReader;
                String string;
                StringBuilder stringBuffer = new StringBuilder();

                HttpURLConnection myURLConnection = null;
                try {

                    //Configuration de la toAccueilConnectee
                    URL url = new URL("https://dummy.restapiexample.com/api/v1/employees");
                    myURLConnection = (HttpURLConnection) url.openConnection();
                    myURLConnection.setRequestMethod("GET");
                    Log.d("tag","MyService - url " + url );

                    myURLConnection.setConnectTimeout(1500);
                    myURLConnection.setReadTimeout(1500);

                    int status  = myURLConnection.getResponseCode();
                    Log.d("tag","MyService - status"+status);

                    if ( status > 299 ){

                        bufferedReader = new BufferedReader(new InputStreamReader(myURLConnection.getErrorStream()));
                        while((string = bufferedReader.readLine()) != null){
                            stringBuffer.append(string);
                        }
                        bufferedReader.close();

                    }else {
                        bufferedReader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                        while((string = bufferedReader.readLine()) != null){
                            stringBuffer.append(string);
                        }
                        bufferedReader.close();
                    }

                    //  parse(stringBuffer.toString());

                    JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    // creation bdd

                    employees = new ArrayList<Employee>();
                    for ( int i = 0 ; i < jsonArray.length() ; i++ ) {

                        JSONObject jsonObjectFinal = jsonArray.getJSONObject(i);

                        //                      Log.d("tag","data - jsonObject " + jsonObject + "data - jsonObjectsss " + jsonObjects );


                        String id = jsonObjectFinal.getString("id");
                        String employee_name = jsonObjectFinal.getString("employee_name");
                        String employee_salary = jsonObjectFinal.getString("employee_salary");
                        String employee_age = jsonObjectFinal.getString("employee_age");
                        String profile_image = jsonObjectFinal.getString("profile_image");

                        byte[] b = profile_image.getBytes(StandardCharsets.UTF_8);

                        Employee data = new Employee(id,employee_name,employee_salary,employee_age,b);

                        Log.d("tag","Employees name before DB_SQLite - data " + data.getEmployee_name() );

                        employees.add(data);

                        // System.out.println(id + " <> " + userId + " <> " + title + "  " );

                    }

                    Log.d("tag","Employees before DB_SQLite - size " + employees.size() );


                    //   ------  Implementation et la verification de la bdd  :
                    db.open();
                    testEmployees = db.getAllBonAchats();
                    if ( testEmployees.size() < 24 ){ db.insertEnregistrementBonAchat(employees); }
                    db.close();

                    //    sendBroadcast("Data-recuperation-is-DONE");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        };

        t.start();

    }

}
