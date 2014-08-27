package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.widget.TextView;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

/**
 * Created by PiskunovI on 15.07.14.
 */
public class MainActivityFlat extends AbstractBaseActivity {

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

      /* String[] countries = getResources().
getStringArray(R.array.list_of_countries);
ArrayAdapter adapter = new ArrayAdapter
(this,android.R.layout.simple_list_item_1,countries);


actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

actv.setAdapter(adapter);
*/

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if(message != null) {
            initialize(message);
        }

        //textSearchClick();
//        menuGroupClick();

        // Payment queue start
        //startPaymentQueue();
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

    public void menuGroupClick(){
        Button button01,button02,button03, button04, button05, button06, button07, button08, button09;
/*        button01 = (Button) findViewById(R.id.Button01);
        button02 = (Button) findViewById(R.id.Button02);
        button03 = (Button) findViewById(R.id.Button03);
        button04 = (Button) findViewById(R.id.Button04);
        button05 = (Button) findViewById(R.id.Button05);
        button06 = (Button) findViewById(R.id.Button06);
        button07 = (Button) findViewById(R.id.Button07);
        button08 = (Button) findViewById(R.id.Button08);
        button09 = (Button) findViewById(R.id.Button09);

        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(101);
            }
        });

        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(102);
            }
        });

        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(103);
            }
        });

        button04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(105);
            }
        });

        button05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(104);
            }
        });

        button06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(106);
            }
        });

        button07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(107);
            }
        });

        button08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(108);
            }
        });

        button09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroup(109);
            }
        });*/
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

    /*@Override
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
    }*/

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
}
