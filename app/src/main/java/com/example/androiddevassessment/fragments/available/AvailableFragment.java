package com.example.androiddevassessment.fragments.available;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.androiddevassessment.R;
import com.example.androiddevassessment.adapters.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AvailableFragment extends Fragment {
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private MyPagerAdapter myPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available, container, false);
        viewPager2 = view.findViewById(R.id.viewPagerAvailable);
        tabLayout = view.findViewById(R.id.tabBot);
        myPagerAdapter = new MyPagerAdapter(this);
        myPagerAdapter.addFragments(new BestMatchFragment());
        myPagerAdapter.addFragments(new HighestPriceFragment());
        myPagerAdapter.addFragments(new LowestPriceFragment());

        viewPager2.setAdapter(myPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Best Match");
                        break;
                    case 1:
                        tab.setText("Highest Price");
                        break;
                    case 2:
                        tab.setText("Lowest Price");
                        break;
                }
            }
        }).attach();
        return view;
    }
}