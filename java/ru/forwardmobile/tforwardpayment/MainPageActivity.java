package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gorbovi on 19.08.2014.
 */
public class MainPageActivity extends FragmentActivity {

        final static String LOG_TAG = "TFORWARD.MainPageActivity";
        public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";
        static final int PAGE_COUNT = 3;

        ViewPager pager;
        PagerAdapter pagerAdapter;


        Button button;
        String message;
        private boolean isFirstRun = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main_page);
            ClearDots();


            button          = (Button)      findViewById(R.id.access_button);

            message         = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);

            Log.d(LOG_TAG, "Initialize MainPageActivity");



            pager = (ViewPager) findViewById(R.id.pager);
            pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);

            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    Log.d(LOG_TAG, "onPageSelected, position = " + position);
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

            applyFonts(findViewById(R.id.activity_main_page_container), null);

           /* LinearLayout DealerInfo = (LinearLayout) findViewById(R.id.dealerInfo);
            DealerInfo.OnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!onSearch){
                        cancelButton.setVisibility(View.VISIBLE);
                        searchListView.setVisibility(View.VISIBLE);
                        linearLayout01.setVisibility(View.GONE);
                        linearLayout02.setVisibility(View.GONE);
                        linearLayout03.setVisibility(View.GONE);
                        onSearch=true;
                    }
                }
            });
           */


        }

    public void enterDealerInfo(View view)
    {
         Intent intent = new Intent(this,DealerInfoActivity.class);
         startActivity(intent);
    }

    public void enterPayments(View view)
    {
      Intent intent = new Intent(this,MainActivityFlat.class);
        startActivity(intent);
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
            firstDot.setBackgroundColor(Color.parseColor("#E2AB5C"));
        }
        else if(page == 1)
        {
            ClearDots();
            LinearLayout secondDot = (LinearLayout) findViewById(R.id.secondDot);
            secondDot.setBackgroundColor(Color.parseColor("#E2AB5C"));
        }
        else{
            ClearDots();
            LinearLayout thirdDot = (LinearLayout) findViewById(R.id.thirdDot);
            thirdDot.setBackgroundColor(Color.parseColor("#E2AB5C"));
        }

    }

    private void ClearDots()
    {
        LinearLayout f,s,t;

        f = (LinearLayout) findViewById(R.id.firstDot);
        f.setBackgroundColor(Color.parseColor("#cccccc"));

        s = (LinearLayout) findViewById(R.id.secondDot);
        s.setBackgroundColor(Color.parseColor("#cccccc"));

        t = (LinearLayout) findViewById(R.id.thirdDot);
        t.setBackgroundColor(Color.parseColor("#cccccc"));
    }

    protected  void applyFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "Magistral.TTF");

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFonts(child, fontToSet);
                }
            } else if (v instanceof TextView) {
                ((TextView)v).setTypeface(fontToSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        onExit();
                    }
                }).create().show();
    }

    private void onExit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT", "true");
        startActivity(intent);

        this.finish();
    }

}




