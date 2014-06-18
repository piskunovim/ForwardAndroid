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
    ListView lvCustomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist);

        dbHelper = new DatabaseHelper(this);
        lvCustomList = (ListView) findViewById(R.id.listView);

        showList();

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


    private void showList() {

        try{
        ArrayList<PaymentListItemsImpl> paymentList = new ArrayList<PaymentListItemsImpl>();
        paymentList.clear();

        String ps;
        String query = "select pay.id, pay.psid, pay.fields, pay.value, pay.fullValue, pay.errorCode, pay.errorDescription, pay.startDate, pay.status, pay.processDate, p.name from payments pay left join p on p.id = pay.psid";
        Cursor c1 = dbHelper.getReadableDatabase().rawQuery(query, null);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    PaymentListItemsImpl paymentListItems = new PaymentListItemsImpl();


                    paymentListItems.setPsid(c1.getString(c1
                            .getColumnIndex("name")));
                    paymentListItems.setFields(c1.getString(c1
                            .getColumnIndex("fields")));
                    paymentListItems.setStatus(c1.getString(c1
                            .getColumnIndex("status")));
                    paymentListItems.setStartDate(c1.getString(c1
                            .getColumnIndex("startDate")));
                    //paymentListItems.setProcessDate(c1.getString(c1
                    //        .getColumnIndex("processDate")));
                    paymentListItems.setValue(c1.getString(c1
                            .getColumnIndex("value")));
                    paymentListItems.setFullValue(c1.getString(c1
                            .getColumnIndex("fullValue")));
                    paymentList.add(paymentListItems);


                } while (c1.moveToNext());
            }

        }
        c1.close();

        PaymentListAdapterImpl contactListAdapter = new PaymentListAdapterImpl(
                PaymentListImpl.this, paymentList);
        lvCustomList.setAdapter(contactListAdapter);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
