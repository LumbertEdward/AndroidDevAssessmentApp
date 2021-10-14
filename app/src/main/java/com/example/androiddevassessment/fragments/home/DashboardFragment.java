package com.example.androiddevassessment.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androiddevassessment.R;
import com.example.androiddevassessment.adapters.MyPagerAdapter;

import io.ak1.BubbleTabBar;
import io.ak1.OnBubbleClickListener;

public class DashboardFragment extends Fragment {
    private ViewPager2 viewPager2;
    private MyPagerAdapter myPagerAdapter;
    private BubbleTabBar bubbleTabBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        viewPager2 = view.findViewById(R.id.viewPager);
        myPagerAdapter = new MyPagerAdapter(this);
        bubbleTabBar = view.findViewById(R.id.bubbleTabBar);

        myPagerAdapter.addFragments(new HomeFragment());
        myPagerAdapter.addFragments(new SettingsFragment());
        myPagerAdapter.addFragments(new SearchFragment());
        myPagerAdapter.addFragments(new ProfileFragment());

        viewPager2.setAdapter(myPagerAdapter);

        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                switch (i){
                    case R.id.home:
                        viewPager2.setCurrentItem(0, true);
                        break;
                    case R.id.settings:
                        viewPager2.setCurrentItem(1, true);
                        break;
                    case R.id.search:
                        viewPager2.setCurrentItem(2, true);
                        break;
                    case R.id.profile:
                        viewPager2.setCurrentItem(3, true);
                        break;
                }
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bubbleTabBar.setSelected(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        return view;
    }
}