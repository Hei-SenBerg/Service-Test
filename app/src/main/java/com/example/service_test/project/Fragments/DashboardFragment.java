package com.example.service_test.project.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_test.R;
import com.example.service_test.project.Activities.Activity_03_Menu;
import com.example.service_test.project.Adapters.MyAdapter;
import com.example.service_test.project.Databases.DB_Data;
import com.example.service_test.project.Models.Employee;
import com.example.service_test.project.Services.EmployeesService;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DB_Data db;
    private List<Employee> employees;
    private MyAdapter listAdapter;
    private RecyclerView recycler;
    private EmployeesService myService;
    private TextView nameuser, walletuser, review, network, plugins, myapps, mainmenus,
            pagetitle, pagesubtitle;

    private Button btnguide;
    private Animation atg, atgtwo, atgthree;
    private ImageView imageView3;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // db = new DB_Data(getContext());

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.dashboard_fragment, container, false);

        atg = AnimationUtils.loadAnimation(getActivity(), R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(getActivity(), R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(getActivity(), R.anim.atgthree);

        nameuser = view.findViewById(R.id.nameuser);
        walletuser = view.findViewById(R.id.walletuser);

        imageView3 = view.findViewById(R.id.imageView3);

        review = view.findViewById(R.id.review);
        network = view.findViewById(R.id.network);
        plugins = view.findViewById(R.id.plugins);
        myapps = view.findViewById(R.id.myapps);
        mainmenus = view.findViewById(R.id.mainmenus);

        pagetitle = view.findViewById(R.id.pagetitle);
        pagesubtitle = view.findViewById(R.id.pagesubtitle);

        btnguide = view.findViewById(R.id.btnguide);

        btnguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (( Activity_03_Menu )getActivity()).selectFragment(1);
            }
        });

        // pass an animation
        imageView3.startAnimation(atg);

        pagetitle.startAnimation(atgtwo);
        pagesubtitle.startAnimation(atgtwo);

        btnguide.startAnimation(atgthree);

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
