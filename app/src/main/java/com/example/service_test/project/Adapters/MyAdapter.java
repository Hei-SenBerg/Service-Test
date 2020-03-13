package com.example.service_test.project.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_test.R;
import com.example.service_test.project.Activities.Activity_03_Menu;
import com.example.service_test.project.Classes.Employee;
import com.example.service_test.project.Databases.DB_Data;


import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Employee employee ;
    private List<com.example.service_test.project.Models.Employee> employees ;

    private DB_Data db;
    private ImageView camera_galerie;

    private Bitmap bitmap ;
    private Uri filePath;



    // List to store all the contact details
    private static Context mContext;
    public static int mPos = -1 ;
    private Bitmap i = Activity_03_Menu.getBitmapImage();


    // Counstructor for the Class
    public MyAdapter(List<com.example.service_test.project.Models.Employee> employees, Context context) {
        super();
        this.employees = employees;
        this.mContext = context;

    }


    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.employee_list_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return employees == null? 0: employees.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        final com.example.service_test.project.Models.Employee contact = employees.get(position);

        Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getImage(), 0, contact.getImage().length);

        // Set the data to the views here
        holder.setEmployeeId(contact.getId());
        holder.setEmployeeName(contact.getEmployee_name());
        holder.setEmployeeSalary(contact.getSalary());
        holder.setEmployeeage(contact.getAge());
        holder.setImageProfile(bitmap);

    }

    // This is your ViewHolder class that helps to populate data to the view
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txthisId;
        private TextView txtName;
        private TextView txtsalary;
        private TextView txtage;

        private ImageView galerie;
        private ImageView image;
        private Employee employee;

        public MyViewHolder(View itemView) {
            super(itemView);

            txthisId = itemView.findViewById(R.id.hisId);
            txtName = itemView.findViewById(R.id.name);
            txtsalary = itemView.findViewById(R.id.salary);
            txtage = itemView.findViewById(R.id.age);

            galerie = itemView.findViewById(R.id.galerie);
            image = itemView.findViewById(R.id.img);

            galerie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mPos = getAdapterPosition();

                    Log.d("tag,","getAdapterPosition"+mPos);

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    (( Activity ) mContext).startActivityForResult(intent,1);
                }
            });

        }

        public void setEmployeeId(String name) {
            txthisId.setText(name);
        }

        public void setEmployeeName(String name) {
            txtName.setText(name);
        }

        public void setEmployeeSalary(String name) {
            txtsalary.setText(name);
        }

        public void setEmployeeage(String number) {
            txtage.setText(number);
        }

        public void setImageProfile(Bitmap i) {
            image.setImageBitmap(i);
        }

    }
}


