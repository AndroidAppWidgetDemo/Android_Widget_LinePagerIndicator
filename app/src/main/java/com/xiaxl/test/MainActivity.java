package com.xiaxl.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xiaxl.test.widget.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    View tab01;
    View tab02;


    LinePagerIndicator mLinePagerIndicator;

    ViewPager viewPager;


    private List<LinePagerIndicator.PositionData> mPositionDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        initUI();
        //
        mLinePagerIndicator.setTabViews(tab01, tab02);

    }


    private void initUI() {
        tab01 = findViewById(R.id.tab_01_view);
        tab02 = findViewById(R.id.tab_02_view);

        //
        mLinePagerIndicator = (LinePagerIndicator) findViewById(R.id.LinePagerIndicator);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                mLinePagerIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_01_view:
                break;
            case R.id.tab_02_view:
                break;
        }
    }


    /**
     * 最热、最新、关注 tab的Adapter
     */
    public class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        // 2个tab
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TestFragment.newInstance();
                case 1:
                    return TestFragment.newInstance();
                default:
                    return TestFragment.newInstance();
            }
        }
    }
}
