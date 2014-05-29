package ru.forwardmobile.tforwardpayment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;


public class MainActivity extends FragmentActivity implements OnClickListener {

    final String LOG_TAG = "TFORWARD.MainActivity";


    Button btnEnter, btnRead, btnReadF;
    EditText etName, etPass;
    //Context context;

    SQLiteOpenHelper        dbHelper;
    TPostData               pd;
    TLoginForm              LoginFrag;
    THomeActivity           HomeFrag;


    ArrayList<String> operatorgroup = new ArrayList<String>();
   // ContentValues cv;
   // SQLiteDatabase db;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFrag = new TLoginForm();
        HomeFrag = new THomeActivity();

        getSupportFragmentManager().beginTransaction()
            .add(R.id.frgmCont, LoginFrag)
            .commit();

        dbHelper = new DatabaseHelper(this);
    }


    public void SingIn(String pointid, String password){

        pd = new TPostData();
        pd.pointID = pointid;
        pd.password = password;

        try{

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.frgmCont, new TEmptyFrag())
                .commit();

            TParseOperators parse = new TParseOperators();
            String responseStr = pd.execute().get();

            parse.GetXMLSettings(responseStr, dbHelper);
            Log.d(LOG_TAG,responseStr);

        }
        catch(Exception e)
        {
            // Log.d(LOG_TAG,e.getMessage());
            e.printStackTrace();
        }


        ListView listContent = (ListView)findViewById(R.id.listView);

        GenerateListView("pg","name", listContent);

        listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT id FROM pg WHERE TRIM(name) = '"+name.trim()+"'", null);
                c.moveToNext();
                Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id + ", name = " + name + ", gid = "+ c.getString(c.getColumnIndex("id")));
                OperatorsListView(c.getString(c.getColumnIndex("id")));//, oplistContent);
            }
        });

        listContent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT id FROM pg WHERE TRIM(name) = '"+name.trim()+"'", null);
                Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id + ", name = " + name + ", gid = "+ c.getString(c.getColumnIndex("id")));
                OperatorsListView(c.getString(c.getColumnIndex("id")));//, oplistContent);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(LOG_TAG, "itemSelect: nothing");
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ListView listContent = (ListView)findViewById(R.id.listView);
        if (item.getTitle().equals("Назад"))
        {
          GenerateListView("pg","name", listContent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // закрываем подключение к БД
        dbHelper.close();
    }



    public void GenerateListView(String table, String column, ListView listContent){
        Log.d(LOG_TAG, "--- table: "+ table + " ---");
        Log.d(LOG_TAG, "--- column: "+ column + " ---");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "--- Got rows in pg table: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        operatorgroup.clear();
        Cursor listpgc = db.query(table, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (listpgc.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int nameColIndex = listpgc.getColumnIndex(column);




            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,  ", "+column+" = " + listpgc.getString(nameColIndex));
                operatorgroup.add(listpgc.getString(nameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (listpgc.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, operatorgroup);
            listContent.setAdapter(adapter);
        } else
            Log.d(LOG_TAG, "Got 0 rows");

        listpgc.close();
        dbHelper.close();


    }

    public void OperatorsListView(String gid){//, ListView listContent){
        Log.d(LOG_TAG, "Start OperatorsListView");
        operatorgroup.clear();
        ListView listContent = (ListView)findViewById(R.id.listView);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor listpc = db.query("p", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (listpc.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            do {
                int gidColIndex = listpc.getColumnIndex("gid");
                int nameColIndex = listpc.getColumnIndex("name");
                Log.d(LOG_TAG, gid);
                Log.d(LOG_TAG, listpc.getString(listpc.getColumnIndex("gid")));
                Log.d(LOG_TAG, listpc.getString(listpc.getColumnIndex("name")));
                // получаем значения по номерам столбцов и пишем все в лог
                if (gid.equals(listpc.getString(listpc.getColumnIndex("gid"))))
                {
                 Log.d(LOG_TAG, listpc.getString(nameColIndex));
                 operatorgroup.add(listpc.getString(nameColIndex));
                }
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (listpc.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, operatorgroup);
            listContent.setAdapter(adapter);
            listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String name = (String) parent.getItemAtPosition(position);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cr = db.rawQuery("SELECT id FROM p WHERE TRIM(name) = '"+name.trim()+"'", null);
                    cr.moveToNext();
                    Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id + ", name = " + name + ", gid = "+ cr.getString(cr.getColumnIndex("id")));
                    //
                    //! Здесь будем передавать id
                    //
                    // cr.getString(cr.getColumnIndex("id"));
                    Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                    startActivity(intent);
                }
            });

        } else
            Log.d(LOG_TAG, "Got 0 rows");
        listpc.close();
        dbHelper.close();

    }
}