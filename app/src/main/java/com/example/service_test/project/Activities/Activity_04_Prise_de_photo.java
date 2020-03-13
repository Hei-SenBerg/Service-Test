package com.example.service_test.project.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.service_test.R;
import com.example.service_test.project.Adapters.MyAdapter;

import java.io.IOException;

public class Activity_04_Prise_de_photo extends AppCompatActivity {

    final static int SELECT_PICTURE = 1;
    private ImageView image;
    private Bitmap bitmap ;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_04__prise_de_photo);

        image = ( ImageView ) findViewById(R.id.imageView1);

        //Lier l'evenement btGalleryClick au boutton

        ((Button) findViewById(R.id.btGallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data!= null && data.getData() != null ){

            filePath = data.getData();

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}

