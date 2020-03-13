package com.example.service_test.project.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.service_test.project.Fragments.DashboardFragment;
import com.example.service_test.project.Fragments.DashboardFragmentND;
import com.example.service_test.project.Fragments.DemoFragment;
import com.example.service_test.project.Fragments.FragmentEx;
import com.example.service_test.project.Fragments.StopWatchFragment;
import com.example.service_test.project.Fragments.StopWatchFragmentW;
import com.example.service_test.project.Fragments.UIFragment;
import com.example.service_test.project.Fragments.UIFragment_nd;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] childFragments;

    public ViewPagerAdapter( FragmentManager fm )
    {
        super(fm);

        childFragments = new Fragment[] {
                new DashboardFragment(),    //0
                new DashboardFragmentND(),  //1
                new FragmentEx(),           //2
                new UIFragment(),           //3
                new UIFragment_nd(),        //4
                new StopWatchFragment(),    //5
                new StopWatchFragmentW(),   //6
                new DemoFragment()          //7
        };

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return childFragments[position] ;
    }

    @Override
    public int getCount() {
        return childFragments.length ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        /*
        position = position + 1 ;
        return "Fragment "+ position ;
         */

        String title = getItem(position).getClass().getName();  // title = " fragment class name "   <<<<<<<<<---------- --------- ----------- --------- |||
        return title.subSequence(title.lastIndexOf(".") + 1, title.length());

    }
}