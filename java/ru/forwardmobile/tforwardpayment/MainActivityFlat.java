package ru.forwardmobile.tforwardpayment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.dealer.DealerDataSource;
import ru.forwardmobile.tforwardpayment.dealer.DealerInfo;
import ru.forwardmobile.tforwardpayment.files.FileOperationsImpl;
import ru.forwardmobile.tforwardpayment.settings.TimeClass;

/**
 * Created by PiskunovI on 15.07.14.
 */
public class MainActivityFlat extends AbstractBaseActivity implements View.OnClickListener {

    final static String LOG_TAG = "TFORWARD.MainActivityFlat";

    ViewGroup view;
    //DealerDataSource dt = new DealerDataSource(this);

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "MainActivityFlat resumed");
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);

        Calendar c = Calendar.getInstance();

        try {
            FileOperationsImpl foi = new FileOperationsImpl(this);
            foi.writeToFile(new TimeClass().getFullCurrentDateString() + ": MainActivityFlat started \n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        view = (ViewGroup) findViewById(R.id.activity_flat_page_container);

        DealerInfo dealerInfo = new DealerInfo(view, this);

        dealerInfo.getBlockInfo();

        initButtons();


    }

    public void enterDealerInfo(View view)
    {
        Intent intent = new Intent(this,DealerInfoActivity.class);
        startActivity(intent);
    }


    private void openGroup(Integer groupNumber){
        Log.d(LOG_TAG, "Opening group number: " + Integer.toString(groupNumber));
        Intent intent = new Intent(this, OperatorsMenuActivity.class);
        intent.putExtra("gid", groupNumber);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initButtons() {
        findViewById(R.id.main_buton_attractions).setOnClickListener(this);
        findViewById(R.id.main_button_credit).setOnClickListener(this);
        findViewById(R.id.main_button_emoney).setOnClickListener(this);
        findViewById(R.id.main_button_internet).setOnClickListener(this);
        findViewById(R.id.main_button_jkh).setOnClickListener(this);
        findViewById(R.id.main_button_land_line).setOnClickListener(this);
        findViewById(R.id.main_button_mobile).setOnClickListener(this);
        findViewById(R.id.main_button_other).setOnClickListener(this);
        findViewById(R.id.main_button_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Log.i("onClick", "Clicked: " + view.getId());

        Integer id = view.getId();
        switch (id) {
            case R.id.main_button_emoney:       openGroup(EMONEY_GROUP_ID); break;
            case R.id.main_button_credit:       openGroup(CREDIT_GROUP_ID); break;
            case R.id.main_buton_attractions:   openGroup(ATTRACTIONS_GROUP_ID); break;
            case R.id.main_button_internet:     openGroup(INTERNET_GROUP_ID); break;
            case R.id.main_button_jkh:          openGroup(JKH_GROUP_ID); break;
            case R.id.main_button_land_line:    openGroup(LAND_LINE_GROUP_ID); break;
            case R.id.main_button_mobile:       openGroup(MOBILE_GROUP_ID); break;
            case R.id.main_button_other:        openGroup(OTHERS_GROUP_ID); break;
            case R.id.main_button_tv:           openGroup(TV_GROUP_ID); break;
            default: break;
        };
    }


    protected final Integer MOBILE_GROUP_ID         = 101;
    protected final Integer TV_GROUP_ID             = 102;
    protected final Integer LAND_LINE_GROUP_ID      = 103;
    protected final Integer CREDIT_GROUP_ID         = 104;
    protected final Integer INTERNET_GROUP_ID       = 105;
    protected final Integer JKH_GROUP_ID            = 106;
    protected final Integer ATTRACTIONS_GROUP_ID    = 107;
    protected final Integer EMONEY_GROUP_ID         = 108;
    protected final Integer OTHERS_GROUP_ID         = 109;
}
