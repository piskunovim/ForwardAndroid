package ru.forwardmobile.tforwardpayment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.reports.TBalanceReportScreenImpl;

import java.util.ArrayList;

public class MainListActivity extends ActionBarActivity {

    final static String LOG_TAG = "TTestActivity.MainListActivity";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

    SQLiteOpenHelper dbHelper;
    ArrayList<String> operatorgroup = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlist);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        ListView listContent = (ListView)findViewById(R.id.listView);

        Log.d(LOG_TAG, message);
        dbHelper = new DatabaseHelper(this);

        if (!message.equals("true"))
        {
        //dbHelper = new DatabaseHelper(this);
        TParseOperators parse = new TParseOperators();
        parse.GetXMLSettings(message, dbHelper);

        }

        GenerateListView("pg", "name", listContent);




        listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT id FROM pg WHERE TRIM(name) = '"+name.trim()+"'", null);
                c.moveToNext();
                Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id + ", name = " + name + ", gid = "+ c.getString(c.getColumnIndex("id")));

                OpenOperatorActivity(c.getString(c.getColumnIndex("id")));
                //OperatorsListView(c.getString(c.getColumnIndex("id")));//, oplistContent);
            }

        });
    }

    public void OpenOperatorActivity(String id){
        Log.d(LOG_TAG, "OpenOperatorActivity id: " + id);
        Intent intent = new Intent(this, OperatorsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        // запуск activity
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            return true;
        }
        if (id == R.id.action_report) {
            CharSequence reports[] = new CharSequence[] {"Баланс", "Платежи", "Поиск платежа"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Выберите отчет:");
            builder.setItems(reports, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on reports[which]
                   if (which == 0){
                   Intent intent = new Intent(MainListActivity.this, TBalanceReportScreenImpl.class);
                   MainListActivity.this.startActivity(intent);
                   }
                }
            });
            builder.show();
            return true;
        }
        if (id == R.id.settings) {
            return true;
        }
        if (id == R.id.payment) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

  /*  public void OperatorsListView(String gid){//, ListView listContent){
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

                    //PaymentActivity pa= new PaymentActivity();

                    //pa.SetOperatorId(Integer.parseInt(cr.getString(cr.getColumnIndex("id"))));
                    //pa.operator_id = cr.getColumnIndex("id");
                    //pa.ShowOperatorId();
                    //
                    //! Здесь будем передавать id
                    //
                    // cr.getString(cr.getColumnIndex("id"));
                    //Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                    //intent.putExtra("psid",Integer.parseInt(cr.getString(cr.getColumnIndex("id"))));
                    //startActivity(intent);
                }
            });

        } else
            Log.d(LOG_TAG, "Got 0 rows");
        listpc.close();
        dbHelper.close();

    }*/

}
