package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.math.BigDecimal;
import java.util.Objects;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.dealer.DealerDataSource;
import ru.forwardmobile.tforwardpayment.dealer.DealerInfo;
import ru.forwardmobile.tforwardpayment.dealer.IDealerBalance;
import ru.forwardmobile.tforwardpayment.reports.PaymentReportActivity;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;

/**
 * Created by piskunovi on 16.12.2014.
 */
public class MainPageActivity extends AbstractBaseActivity {

        final static String LOG_TAG = "TFORWARD.MainPageActivity";

        //количество страниц в блоке информационных сообщений
        static final int PAGE_COUNT = 3;

        ViewPager pager;
        PagerAdapter pagerAdapter;

        LinearLayout enterPaymentsBtn, enterNotificationsBtn, enterSettingsBtn, enterReportPaymentsBtn;

        // 16.12.14 некто пришел и закомментировал эту строчку. Видимо она совсем не нужна...может удалить?
        // DealerDataSource dt = new DealerDataSource(this);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main_page);

            Log.d(LOG_TAG, "Initialize MainPageActivity");

            // сброс индикатора страниц #информационного_блока_сообщений
            ClearDots();

            ViewGroup view = (ViewGroup) findViewById(R.id.activity_main_page_container);

            final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

            // метод заполняет информационный блок дилера данными
            final DealerInfo dealerInfo = new DealerInfo(view, this);

            // почему-то это закомментировали, можно удалить
            //dealerInfo.getDealerInfo(); // запрашиваем информацию

            dealerInfo.getBlockInfo();  // выводим в блок

            //SwipeRefresh
            swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
            swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeView.setRefreshing(true);
                    Log.d(LOG_TAG, "Обновление Информации Агента");
                    ( new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeView.setRefreshing(false);
                            dealerInfo.getDealerInfo();
                            dealerInfo.getBlockInfo();
                        }
                    }, 3000);
                }
            });

            // = кнопка "Проведение платежей"
            enterPaymentsBtn = (LinearLayout) findViewById(R.id.payments_button);

            enterPaymentsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 enterPayments(view);
                }
            });

            // = кнопка "Отчет по платежам"
            enterReportPaymentsBtn = (LinearLayout) findViewById(R.id.report_payments_button);

            enterReportPaymentsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterReportPayments(view);
                }
            });

            // = кнопка "Уведомления"
            enterNotificationsBtn = (LinearLayout) findViewById(R.id.notifications_button);

            enterNotificationsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterNotifications(view);
                }
            });

            // = кнопка "Настройки"
            enterSettingsBtn = (LinearLayout) findViewById(R.id.settings_button);

            enterSettingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterSettings(view);
                }
            });


            // #информационный_блок_сообщений
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

    /*
      Переход на соответствующие Activity
     */

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


    public void enterReportPayments(View view)
    {
        Intent intent = new Intent(this,PaymentReportActivity.class);
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

    /*
      Заполнение информационного блока дилера
    */
    /*public void dealerInfoBlock(){

        String dealersName = "Отсутствует",
               dealersBalance = "Отсутствует",
               dealersMayExpend = "Отсутствует",
               dealersCredit = "Отсутствует";

        ViewGroup view =  (ViewGroup) findViewById(R.id.activity_main_page_container);

        // включает методы заполнения компонентов необходимыми данными
        DealerInfo dealerInfo = new DealerInfo(view);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.DEALER_TABLE_NAME , null);

        if (c.moveToFirst()) {
          dealersName = c.getString(c.getColumnIndex("name"));
          dealersBalance = c.getString(c.getColumnIndex("balance"));
          dealersMayExpend = c.getString(c.getColumnIndex("may_expend"));
          dealersCredit = c.getString(c.getColumnIndex("credit"));
          Log.d(LOG_TAG," dealersNameInBase = " + dealersName + ", dealersBalanceInBase = " + dealersBalance);
          Log.d(LOG_TAG," dealersMayExpend = " + dealersMayExpend + ", dealersCredit = " + dealersCredit);
        } else
            Log.d(LOG_TAG, "0 rows");

        c.close();

        dealerInfo.setDealerShotBlock(dealersName,dealersBalance, dealersMayExpend, dealersCredit);
    }*/

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

    /*
     Изменяеняем состояние индикатора
     */
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

        stopPaymentQueue();

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
    }


}




