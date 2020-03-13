package com.example.service_test.project.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.service_test.R;

public class StopWatchFragmentW extends Fragment {

    private Button btnstart, btnstop ;
    private ImageView icanchor ;
    private Animation roundingalone ;
    private Chronometer timerHere ;

    public StopWatchFragmentW() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.stop_watch_w_layout, container, false);

        btnstart = (Button) view.findViewById(R.id.btnstart) ;
        btnstop = (Button) view.findViewById(R.id.btnstop) ;
        icanchor = (ImageView) view.findViewById(R.id.icanchor) ;
        timerHere = (Chronometer) view.findViewById(R.id.timerHere) ;

        // create Optional animation :
        btnstop.setAlpha(0);

        // load animations :
        roundingalone = AnimationUtils.loadAnimation( getActivity(), R.anim.roundingalone );

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing animation :
                icanchor.startAnimation( roundingalone );
                btnstop.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnstart.animate().alpha(0).setDuration(300).start();

                //start time :
                timerHere.setBase(SystemClock.elapsedRealtime());
                timerHere.start();

            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundingalone.reset();
                icanchor.clearAnimation();
                timerHere.stop();
                // timerHere.resetPivot();
                btnstart.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnstop.animate().alpha(0).setDuration(300).start();
            }
        });

        return view ;
    }

}