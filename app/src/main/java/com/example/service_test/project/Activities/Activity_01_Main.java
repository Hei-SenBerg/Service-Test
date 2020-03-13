package com.example.service_test.project.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.service_test.R;

public class Activity_01_Main extends AppCompatActivity {
    private Context mContext;
    private Boolean bCharged = false ;
    private Handler handler = new Handler();
    private ProgressBar mProgressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_01_main);

        mContext = Activity_01_Main.this;

        //Définition de la couleur de la progressbar
        final float[] roundedCorners = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
        ShapeDrawable pgDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null, null));
        pgDrawable.getPaint().setColor(ContextCompat.getColor(this, R.color.colorAccent));
        ClipDrawable progress = new ClipDrawable(pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setProgressDrawable(progress);

        //Gestion de lancement next activite
        new Thread(new Runnable() {

            int i = 0;
            int progressStatus = 0;

            public void run() {
                while (progressStatus < 100) {
                    progressStatus += doWork();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        public void run() {
                            mProgressBar.setProgress(progressStatus);
                            i++;
                        }
                    });
                }

                bCharged = true;
                if ( bCharged ) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Activity_01_Main.this, Activity_00_Permissions.class));
                        }
                    });
                }

            }
            private int doWork() {

                return i * 3;
            }

        }).start();


    }

    public void nextActivity(){
        Intent intent = new Intent(mContext, Activity_00_Permissions.class);
        startActivity(intent);
    }

    }

