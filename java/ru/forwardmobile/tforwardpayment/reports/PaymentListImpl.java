package ru.forwardmobile.tforwardpayment.reports;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.MainActivity;
import ru.forwardmobile.tforwardpayment.PaymentActivity;
import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

public class PaymentListImpl extends ActionBarActivity {

    String LOG_TAG = "TForwardPayment.PaymentListImpl";

    SQLiteOpenHelper dbHelper;
    ArrayList<String> payinfo = new ArrayList<String>();
    ListView lvCustomList;
    TextView listAmount;
    TextView listEmpty;
    String ControlMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist);

        Intent intent = getIntent();
        ControlMessage = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        dbHelper = new DatabaseHelper(this);
        lvCustomList = (ListView) findViewById(R.id.listView);
        listAmount = (TextView) findViewById(R.id.pay_amount);
        listEmpty = (TextView) findViewById(R.id.emptyList);
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
        int valueSum, fullValueSum;
            valueSum = 0;
            fullValueSum = 0;
        String query = "select pay.id, pay.psid, pay.fields, pay.value, pay.fullValue, pay.errorCode, pay.errorDescription, pay.startDate, pay.status, pay.processDate, p.name from payments pay left join p on p.id = pay.psid";

        Cursor c1 = dbHelper.getReadableDatabase().rawQuery(query, null);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {

                if (ControlMessage.equals("0") || c1.getString(c1.getColumnIndex("status")).equals("3"))
                {
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
                    paymentListItems.setProcessDate(c1.getString(c1
                            .getColumnIndex("processDate")));
                    paymentListItems.setValue(c1.getString(c1
                            .getColumnIndex("value")));
                    paymentListItems.setFullValue(c1.getString(c1
                            .getColumnIndex("fullValue")));
                    paymentListItems.setErrorCode(c1.getString(c1
                            .getColumnIndex("errorCode")));
                    paymentListItems.setErrorDescription(c1.getString(c1
                            .getColumnIndex("errorDescription")));
                    /*paymentListItems.setValueSum(c1.getString(c1
                            .getColumnIndex("value")));
                    paymentListItems.setFullValueSum(c1.getString(c1
                            .getColumnIndex("fullValue")));*/
                    valueSum += Integer.parseInt(c1.getString(c1.getColumnIndex("value")))/100;
                    fullValueSum += Integer.parseInt(c1.getString(c1.getColumnIndex("fullValue")));

                    paymentList.add(paymentListItems);


                } while (c1.moveToNext());
                }
                else{
                     listEmpty.setVisibility(View.VISIBLE);
                }
                listAmount.setText("Зачислено: " + valueSum + " руб. ; Получено: " + fullValueSum + " руб.");
            }

        }
        c1.close();

        PaymentListAdapterImpl contactListAdapter = new PaymentListAdapterImpl(
                PaymentListImpl.this, paymentList);
        lvCustomList.setAdapter(contactListAdapter);

            lvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    // When clicked, show a toast with the TextView text
                    LinearLayout par = (LinearLayout) view;
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentListImpl.this);
                    //Оператор
                    TextView t = (TextView) par.findViewById(R.id.pay_psid);
                    String operator = "Оператор: " + t.getText().toString();
                    //Счет
                    t = (TextView) par.findViewById(R.id.pay_fields);
                    String num = "Счет: " + t.getText().toString();
                    //Суммы
                    t = (TextView) par.findViewById(R.id.pay_sum);
                    String sum = "Принято/К зачислению: \n" + t.getText().toString();
                    //Дата отправки платежа
                    t = (TextView) par.findViewById(R.id.pay_date_begin);
                    String dateBegin = "Отправлен: " + t.getText().toString();
                    //Дата завершения платежа
                    t = (TextView) par.findViewById(R.id.pay_date_end);
                    String dateEnd = "Завершен: " + t.getText().toString();
                    //Ошибка (в формате : "Код - Текст ошибки")
                    t = (TextView) par.findViewById(R.id.pay_error_code);
                    String errorText = "Ошибка: " + t.getText().toString();
                    t = (TextView) par.findViewById(R.id.pay_error_description);
                    errorText += " - " + t.getText().toString();
                    t = (TextView) par.findViewById(R.id.pay_status);
                    //Статус
                    String stat = "Статус: " + t.getText().toString();
                    String statCh = t.getText().toString();
                    builder.setTitle("Подробная информация:");
                    builder.setMessage(operator +"\n" + num + "\n" + sum + "\n" + dateBegin + "\n" + dateEnd + "\n" + errorText + "\n" + stat);
                    if (!statCh.equals("Проведен")){
                        builder.setPositiveButton("Отправить повторно", null);
                        builder.setNegativeButton("Отменить",null);
                    }

                    builder.show();

                   // Toast.makeText(getApplicationContext(), t.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
