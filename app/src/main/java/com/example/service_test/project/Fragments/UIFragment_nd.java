package com.example.service_test.project.Fragments;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_test.R;
import com.example.service_test.project.Adapters.MyAdapter;
import com.example.service_test.project.Databases.DB_Data;
import com.example.service_test.project.Models.Employee;
import com.example.service_test.project.Services.EmployeesService;

import java.util.ArrayList;
import java.util.List;

public class UIFragment_nd extends Fragment implements RatingBar.OnRatingBarChangeListener {

    private DB_Data db;
    private List<Employee> employees;
    private MyAdapter listAdapter;
    private RecyclerView recycler;
    private EmployeesService myService;
    private RatingBar ratingBar ;

    public UIFragment_nd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // db = new DB_Data(getContext());

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.uifragment_second, container, false);

        ratingBar = (RatingBar) view.findViewById(R.id.tb);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.golden), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.gris_claire), PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(this);

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

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        Toast.makeText(getContext(),"Thanx for rating this app " + rating,Toast.LENGTH_SHORT) ;

    }
}
