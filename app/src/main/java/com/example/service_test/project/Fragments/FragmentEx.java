package com.example.service_test.project.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_test.R;
import com.example.service_test.project.Adapters.MyAdapter;
import com.example.service_test.project.Databases.DB_Data;
import com.example.service_test.project.Models.Employee;
import com.example.service_test.project.Services.EmployeesService;

import java.util.ArrayList;
import java.util.List;

public class FragmentEx extends Fragment {

    private DB_Data db;
    private List<Employee> employees;
    private MyAdapter listAdapter;
    private RecyclerView recycler;
    private EmployeesService myService;

    public FragmentEx() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // db = new DB_Data(getContext());

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragmentex, container, false);

        return view ;
    }

    private void getDataListview(){

        myService = new EmployeesService(getContext());
        myService.GetDataFromServer();

        employees =new ArrayList<>();
        employees.clear();

        db.open();
        employees = db.getAllBonAchats();
        db.close();


        Log.d( "tag","DemoFragment - employees List -  size " + employees.size());

        listAdapter = new MyAdapter(employees, getContext());
        recycler.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

    }

}
