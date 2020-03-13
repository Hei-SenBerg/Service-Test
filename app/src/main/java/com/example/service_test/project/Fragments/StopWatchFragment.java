package com.example.service_test.project.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.service_test.R;
import com.example.service_test.project.Activities.Activity_03_Menu;

public class StopWatchFragment extends Fragment {

    private TextView tvSplash, tvSubSplash ;
    private Button btnget;
    private ImageView ivSplash;
    private Animation atg, btgone, btgtwo ;


    public StopWatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // db = new DB_Data(getContext());

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.stop_watch_layout, container, false);

        tvSplash = (TextView) view.findViewById( R.id.tvSplash ) ;
        tvSubSplash = (TextView) view.findViewById( R.id.tvSubSplash ) ;
        btnget = (Button) view.findViewById( R.id.btnget ) ;
        ivSplash = (ImageView) view.findViewById( R.id.ivSplash ) ;

        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity_03_Menu)getActivity()).selectFragment(6);
            }
        });

        // load animations :
        atg = AnimationUtils.loadAnimation( getActivity(), R.anim.atg ) ;
        btgone = AnimationUtils.loadAnimation( getActivity(), R.anim.atgthree ) ;
        btgtwo = AnimationUtils.loadAnimation( getActivity(), R.anim.atgtwo ) ;

        // passing animation
        ivSplash.startAnimation( atg );
        tvSplash.startAnimation( btgone );
        tvSubSplash.startAnimation( btgtwo );
/*
        // import font :
        Typeface MLight = Typeface.createFromAsset( getActivity().getAssets(), "font/ml.ttf" ) ;
        Typeface MMedium = Typeface.createFromAsset( getActivity().getAssets(), "font/mm.ttf" ) ;
        Typeface MRegular = Typeface.createFromAsset( getActivity().getAssets(), "font/mr.ttf" ) ;

        // customize font
        tvSplash.setTypeface( MRegular );
        tvSubSplash.setTypeface( MLight );
        btnget.setTypeface( MMedium );
 */

        return view ;
    }

}