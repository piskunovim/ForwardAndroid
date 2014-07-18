package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

/**
 * Created by PiskunovI on 15.07.14.
 */
public class MainActivityFlat extends ActionBarActivity {

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

      /*  String[] countries = getResources().
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

        textSearchClick();
    }

    private void initialize(String message) {

        Log.d(LOG_TAG, message);
        Log.v(LOG_TAG, "Loading settings");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.readSettings();
        dbHelper.close();
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

        SQLiteOpenHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT name,id FROM " + DatabaseHelper.P_TABLE_NAME + " WHERE name LIKE '"+ operator +"'", null);

        if (cursor.moveToFirst()) {
            //Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex("name")));

            do{
             operatorArray.add(cursor.getString(cursor.getColumnIndex("name")));
            }while (cursor.moveToNext());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, operatorArray);
        searchListView.setAdapter(adapter);

        } else {
        Log.d(LOG_TAG, "Table got 0 rows");
        }
        /*try {
            cursor.moveToNext();
            Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex("id")));
        } finally {
            cursor.close();
            db.close();
            dbHelper.close();
        }
*/

        /*while(cursor.isAfterLast() == false){
            int nameColIndex = cursor.getColumnIndex("name");
            operatorArray.add(cursor.getString(nameColIndex));
            cursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, operatorArray);
        searchListView.setAdapter(adapter);*/

    }

}
