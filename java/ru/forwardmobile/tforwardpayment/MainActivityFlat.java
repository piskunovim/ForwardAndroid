package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.reports.PaymentAdapter;

/**
 * Created by PiskunovI on 15.07.14.
 */
public class MainActivityFlat extends AbstractBaseActivity implements View.OnClickListener {

    final static String LOG_TAG = "TFORWARD.MainActivityFlat";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";
    private AutoCompleteTextView textSearch;
    private LinearLayout linearLayout01,linearLayout02,linearLayout03;
    private Button cancelButton;
    private ListView searchListView;
    private boolean onSearch = false;
    private ArrayAdapter<String> adapterSearch;
    String afterTextChanged = "";
    String beforeTextChanged = "";
    String onTextChanged = "";
    ArrayList<String> operatorArray = new ArrayList<String>();

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "MainActivityFlat resumed");
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if (message != null) {
            initialize(message);
        }

        initButtons();
    }

    private void initialize(String message) {

        Log.d(LOG_TAG, message);
        Log.v(LOG_TAG, "Loading settings");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.readSettings();
        dbHelper.close();
    }

    public void enterDealerInfo(View view)
    {
        Intent intent = new Intent(this,DealerInfoActivity.class);
        startActivity(intent);
    }


    public void textSearchClick(){
        textSearch = (AutoCompleteTextView) findViewById(R.id.inputSearch);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        searchListView = (ListView) findViewById(R.id.listViewSearch);
        linearLayout01 = (LinearLayout) findViewById(R.id.LinearLayout01);
        linearLayout02 = (LinearLayout) findViewById(R.id.LinearLayout02);
        linearLayout03 = (LinearLayout) findViewById(R.id.LinearLayout03);

        textSearch.setOnClickListener(new View.OnClickListener() {
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

        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                beforeTextChanged = textSearch.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                onTextChanged = textSearch.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                afterTextChanged = textSearch.getText().toString();
                searchOperator(afterTextChanged);
                //afterTextChanged = textSearch.getText().toString();
                /*Toast.makeText(MainActivityFlat.this, "before: " + beforeTextChanged
                    + '\n' + "on: " + onTextChanged
                    + '\n' + "after: " + afterTextChanged
                    , Toast.LENGTH_SHORT).show();*/
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSearch){
                    cancelButton.setVisibility(View.GONE);
                    searchListView.setVisibility(View.GONE);
                    linearLayout01.setVisibility(View.VISIBLE);
                    linearLayout02.setVisibility(View.VISIBLE);
                    linearLayout03.setVisibility(View.VISIBLE);
                    onSearch=false;
                }
            }
        });

    }

    public void searchOperator(String operator)
    {
        operator = "%" + operator + "%";
        Cursor cursor;
        operatorArray.clear();

        /*SQLiteOpenHelper dbHelper = new DatabaseHelper(getApplicationContext());
SQLiteDatabase db = dbHelper.getReadableDatabase();

db.rawQuery("SELECT name FROM " + DatabaseHelper.P_TABLE_NAME + " WHERE name LIKE '"+ operator +"'", null);
*/
        cursor = initializeDB(0,operator);

        if (cursor.moveToFirst()) {
            //Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex("name")));

            do{
                operatorArray.add(cursor.getString(cursor.getColumnIndex("name")));
            }while (cursor.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, operatorArray);
            searchListView.setAdapter(adapter);

            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView c = (TextView) view;
                    String text = c.getText().toString();
                    Cursor cursorID = initializeDB(1,text);
                    if (cursorID.moveToFirst()) {

                        Log.i(LOG_TAG, "Starting payment to " + text);
                        startPayment(Integer.parseInt(cursorID.getString(cursorID.getColumnIndex("id"))));

                    }
                }
            });

        } else {
            Log.d(LOG_TAG, "Table got 0 rows");
        }


    }

    private Cursor initializeDB(Integer state, String searchString) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if (state == 0) {
            return db.rawQuery("SELECT name FROM " + DatabaseHelper.P_TABLE_NAME + " WHERE name LIKE '" + searchString + "'", null);
        }
        else {
            return db.rawQuery("SELECT id FROM " + DatabaseHelper.P_TABLE_NAME + " WHERE name LIKE '" + searchString + "'", null);
        }
    }

    public void startPayment(Integer id){
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("psid", id);
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

        stopPaymentQueue();
        DatabaseHelper helper = new DatabaseHelper(this);
        helper.saveSettings();
        helper.close();
    }

    private void startPaymentQueue() {
        Log.i(LOG_TAG, "Starting payment queue...");
        startService(new Intent(this, TPaymentService.class));
    }

    private void stopPaymentQueue() {
        Log.i(LOG_TAG,"Deactivating payment queue...");
        stopService(new Intent(this,TPaymentService.class));
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
