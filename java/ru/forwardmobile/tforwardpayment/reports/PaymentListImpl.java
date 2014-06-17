package ru.forwardmobile.tforwardpayment.reports;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.PaymentActivity;
import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

public class PaymentListImpl extends ActionBarActivity {

    String LOG_TAG = "TForwardPayment.PaymentListImpl";

    SQLiteOpenHelper dbHelper;
    ArrayList<String> payinfo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist);

        dbHelper = new DatabaseHelper(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.payment_list_impl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void PayinfoListView(String gid){
        Log.d(LOG_TAG, "Start PayinfoListView");
        payinfo.clear();
        ListView listContent = (ListView)findViewById(R.id.listView);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor listpc = db.query("payments", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (listpc.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            do {
                int psidColIndex = listpc.getColumnIndex("psid");
                int transactidColIndex = listpc.getColumnIndex("transactid");
                int fieldsColIndex = listpc.getColumnIndex("fields");
                int valueColIndex = listpc.getColumnIndex("value");
                int fullvalColIndex = listpc.getColumnIndex("fullValue");
                int errorCodeColIndex = listpc.getColumnIndex("errorCode");
                int errorDescriptionColIndex = listpc.getColumnIndex("errorDescription");
                int startDateColIndex = listpc.getColumnIndex("startDate");
                int statusColIndex = listpc.getColumnIndex("status");
                int processDateColIndex = listpc.getColumnIndex("processDate")
                Log.d(LOG_TAG, listpc.getString(psidColIndex));
                Log.d(LOG_TAG, listpc.getString(transactidColIndex));
                // получаем значения по номерам столбцов и пишем все в лог
                if (gid.equals(listpc.getString(listpc.getColumnIndex("gid"))))
                {
                    Log.d(LOG_TAG, listpc.getString(nameColIndex));
                    payinfo.add(listpc.getString(nameColIndex));
                }
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (listpc.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, operatorgroup);
            listContent.setAdapter(adapter);
        } else
            Log.d(LOG_TAG, "Got 0 rows");
        listpc.close();
        dbHelper.close();

    }*/


}
