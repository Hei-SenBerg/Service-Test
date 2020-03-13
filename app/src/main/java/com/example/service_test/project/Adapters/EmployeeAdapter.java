package com.example.service_test.project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.service_test.R;
import com.example.service_test.project.Databases.DB_Data;
import com.example.service_test.project.Models.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter  extends BaseAdapter  {
    Context context;
    List<Employee> employees ;
    TextView textView ;
    Employee employee;
    String string;
    public ArrayList<Employee> orig;

    private DB_Data db;

    public EmployeeAdapter ( Context context, List<Employee> employees ){
        super();
        this.context = context ;
        this.employees = employees ;
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        db = new DB_Data(context);
        convertView = LayoutInflater.from(context).inflate(R.layout.list_employee, parent, false);

        TextView id = convertView.findViewById(R.id.sid);
        TextView name = convertView.findViewById(R.id.name);
        TextView salary = convertView.findViewById(R.id.salary);
        TextView age = convertView.findViewById(R.id.age);

        id.setText("Id : " + employees.get(position).getId() );
        name.setText(("Nom : " + employees.get(position).getEmployee_name()));
        salary.setText(("Salaire : " + employees.get(position).getSalary()));
        age.setText("Age : " + employees.get(position).getAge());

        return convertView;
    }

}
