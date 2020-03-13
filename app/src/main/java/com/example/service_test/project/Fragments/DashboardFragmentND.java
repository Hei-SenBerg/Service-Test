package com.example.service_test.project.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.service_test.R;

public class DashboardFragmentND extends Fragment {

    private TextView pagetitle, pagesubtitle;
    private ImageView packagePlace;
    private SeekBar packageRange;
    private Button btntake ;
    private Animation atg, packageimg;
    private int i = 0 ;

    public DashboardFragmentND() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // db = new DB_Data(getContext());

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.dashboard_frag_2, container, false);

        btntake = (Button) view.findViewById(R.id.btntake) ;

        atg = AnimationUtils.loadAnimation(getActivity(), R.anim.atg);
        packageimg = AnimationUtils.loadAnimation(getActivity(), R.anim.packagimg);

        pagetitle = view.findViewById(R.id.pagetitle);
        pagesubtitle = view.findViewById(R.id.pagesubtitle);

        packagePlace = view.findViewById(R.id.packagePlace);

        packageRange = view.findViewById(R.id.packageRange);

        packageRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 33){
                    pagetitle.setText("Starter Guy");
                    pagesubtitle.setText("The simply text be dummies too good and easier");
                    packagePlace.setImageResource(R.drawable.icstarter);

                    // pass animation
                    packagePlace.startAnimation(packageimg);
                    pagetitle.startAnimation(atg);
                    pagesubtitle.startAnimation(atg);
                }
                else if(progress == 66){
                    pagetitle.setText("Business Player");
                    pagesubtitle.setText("The simply text be dummies too good and easier");
                    packagePlace.setImageResource(R.drawable.icbusinessplayer);

                    // pass animation
                    packagePlace.startAnimation(packageimg);
                    pagetitle.startAnimation(atg);
                    pagesubtitle.startAnimation(atg);
                }
                else if(progress == 100){
                    pagetitle.setText("Legend of VIP");
                    pagesubtitle.setText("The simply text be dummies too good and easier");
                    packagePlace.setImageResource(R.drawable.icvip);

                    // pass animation
                    packagePlace.startAnimation(packageimg);
                    pagetitle.startAnimation(atg);
                    pagesubtitle.startAnimation(atg);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        btntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if( i == 0 ) {

                     btntake.setText("Next");
                     packageRange.setProgress(0);
                     i = 33 ;

                 }else if ( i == 33 ) {

                     packageRange.setProgress(33);
                     i = 66 ;

                }else if ( i == 66 ) {

                     packageRange.setProgress(66);
                     i = 100 ;

                 }else if ( i == 100 ) {

                     packageRange.setProgress(100);
                     btntake.setText("Do it again !");
                     i = 0 ;

                 }

            }
        });

        return view ;
    }


}
