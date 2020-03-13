package com.example.service_test.project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.service_test.R;

public class Activity_05_Home extends AppCompatActivity {
    ImageView linearLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_05__home);

        linearLayout = (ImageView) findViewById(R.id.test);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_05_Home.this, Activity_02_Register.class));
            }
        });

    }
}
