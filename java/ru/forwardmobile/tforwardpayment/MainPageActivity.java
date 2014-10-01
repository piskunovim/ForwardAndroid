package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.math.BigDecimal;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.dealer.DealerDataSource;
import ru.forwardmobile.tforwardpayment.dealer.DealerInfo;
import ru.forwardmobile.tforwardpayment.dealer.IDealerBalance;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;

/**
 * Created by gorbovi on 19.08.2014.
 */
public class MainPageActivity extends AbstractBaseActivity implements IDealerBalance {

        final static String LOG_TAG = "TFORWARD.MainPageActivity";
        static final int PAGE_COUNT = 3;

        ViewPager pager;
        PagerAdapter pagerAdapter;


        Button button;
        String message;


        ViewGroup view;
        DealerDataSource dt = new DealerDataSource(this);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main_page);
            ClearDots();


            button          = (Button)      findViewById(R.id.access_button);

            message         = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
            Log.d(LOG_TAG, "Initialize MainPageActivity");
            Log.d(LOG_TAG, "dealersName");

            view =  (ViewGroup) findViewById(R.id.activity_main_page_container);

            DealerInfo dealerInfo = new DealerInfo(view);
            dealerInfo.setDealerShotBlock(dt.dealersName,dt.dealersBalance, dt.dealersCredit, dt.dealersRealMoney);

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


    public void enterMonitoring(View view)
    {
        Intent intent = new Intent(this,PageMonitoring.class);
        startActivity(intent);
    }

    public void enterNotifications(View view)
    {
        Intent intent = new Intent(this,PageNotifications.class);
        startActivity(intent);
    }

    public void enterSettings(View view)
    {
        Intent intent = new Intent(this,PageSettings.class);
        startActivity(intent);
    }

    @Override
    public void onTaskFinish(Object result) {
        if(result != null && result instanceof IResponseSet) {

            IResponseSet responseSet = (IResponseSet) result;

            double balance  = 0;
            double limit    = 0;
            double total    = 0;

            String balanceToView = "";
            String creditToView = "";
            String totalToView = "";

            try {
                // разбираем ответ
                ICommandResponse response = (ICommandResponse) responseSet.getResponses().get(0);
                Log.d(LOG_TAG, response.toString());
                balance = Double.parseDouble(response.getParam("balance"));
                limit = Double.parseDouble(response.getParam("limit"));
                total = balance + limit;

                DealerInfo dealerInfo = new DealerInfo(view);
                // Показываем результат
                if (balance != 0)
                    balanceToView = String.valueOf(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                if (limit != 0)
                    creditToView = String.valueOf(new BigDecimal(limit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                if (total != 0)
                    if (total > 0)
                        totalToView = String.valueOf(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    else
                        totalToView = "Прием платежей невозможен!";
                dealerInfo.setDealerShotBlock("Иванов Иван Иванович",balanceToView,creditToView,totalToView);
            } catch (Exception ex) {
                // Ошибка разбора
            }
        } else {
            // Ошибка отправки запроса
        }
    }

    @Override
    public void getBalance() {

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

        //= Заполнение слайдера =//
        public void sliderFill(){

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

    private void stopPaymentQueue() {
        Log.i(LOG_TAG,"Deactivating payment queue...");
        stopService(new Intent(this,TPaymentService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopPaymentQueue();
        DatabaseHelper helper = new DatabaseHelper(this);
        helper.saveSettings();
        helper.close();

        this.finish();
    }
}




