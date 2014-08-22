package ru.forwardmobile.tforwardpayment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by PiskunovI on 22.08.2014.
 */
/**
 * @deprecated
 */
public class MainFooter  extends FragmentActivity {

    /*static final String TAG = "myLogs";
    static final int PAGE_COUNT = 3;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ClearDots();
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
            }


            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                SlideIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

    private void SlideIndicator(int page){
        Log.d("Sliding: ", Integer.toString(page));
        if (page == 0){
            ClearDots();
            LinearLayout firstDot = (LinearLayout) findViewById(R.id.firstDot);
            firstDot.setBackgroundColor(Color.parseColor("#3498db"));
        }
        else if(page == 1)
        {
            ClearDots();
            LinearLayout secondDot = (LinearLayout) findViewById(R.id.secondDot);
            secondDot.setBackgroundColor(Color.parseColor("#3498db"));
        }
        else{
            ClearDots();
            LinearLayout thirdDot = (LinearLayout) findViewById(R.id.thirdDot);
            thirdDot.setBackgroundColor(Color.parseColor("#3498db"));
        }

    }

    private void ClearDots()
    {
        LinearLayout f,s,t;

        f = (LinearLayout) findViewById(R.id.firstDot);
        f.setBackgroundColor(Color.WHITE);

        s = (LinearLayout) findViewById(R.id.secondDot);
        s.setBackgroundColor(Color.WHITE);

        t = (LinearLayout) findViewById(R.id.thirdDot);
        t.setBackgroundColor(Color.WHITE);
    }*/
}
