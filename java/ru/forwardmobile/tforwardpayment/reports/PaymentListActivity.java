package ru.forwardmobile.tforwardpayment.reports;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

/**
 *
 */
public class PaymentListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public static final String TYPE_EXTRA_PARAMETER = "tpx17u";

    public static final String LIST_UNPROCESSED = "unprocessed";
    public static final String LIST_ALL         = "all";

    String LOG_TAG = "TForwardPayment.PaymentListActivity";

    SQLiteOpenHelper dbHelper;

    PaymentListDataSource dataSource;

    ListView lvCustomList;
    TextView listAmount;
    TextView listEmpty;
    String   listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist);


        dbHelper = new DatabaseHelper(this);

        lvCustomList = (ListView) findViewById(R.id.listView);
        listAmount = (TextView) findViewById(R.id.pay_amount);
        listEmpty = (TextView) findViewById(R.id.emptyList);

        //dataSource = new PaymentListDataSource(this);
        //createView( getPayments() );
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

    private  void createView(List<PaymentInfo> items) {



    }

    private List<PaymentInfo> getPayments() {

        String type = getIntent().getStringExtra(TYPE_EXTRA_PARAMETER);

        // Case unprocessed
        if( LIST_UNPROCESSED . equals( type ) )
            return dataSource.getUnprocessed();

        // By default NullObject Pattern.
        else
            // Returns an empty list, so user will see an empty screen
            return Collections.EMPTY_LIST;
    }

    /** @deprecated  */
    private void showList() {

        try{
            ArrayList<PaymentListItem> paymentList = new ArrayList<PaymentListItem>();
            paymentList.clear();

            String ps;
            int valueSum, fullValueSum;
                valueSum = 0;
                fullValueSum = 0;
            String query = "select pay.id, pay.psid, pay.fields, pay.value, pay.fullValue, pay.errorCode, pay.errorDescription, pay.startDate, pay.status, pay.processDate, p.name from payments pay left join p on p.id = pay.psid";

            Cursor c1 = dbHelper.getReadableDatabase().rawQuery(query, null);

            if ( c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {

                    if (listType.equals("0") || c1.getString(c1.getColumnIndex("status")).equals("3"))
                    {
                        do {
                            PaymentListItem paymentListItems = new PaymentListItem();

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
                            paymentListItems.setErrorCode(c1.getInt(c1
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

            PaymentAdapter contactListAdapter
                = new PaymentAdapter(PaymentListActivity.this, paymentList);
            lvCustomList.setAdapter(contactListAdapter);
            lvCustomList.setOnItemClickListener( this );

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // When clicked, show a toast with the TextView text
        LinearLayout par = (LinearLayout) view;
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentListActivity.this);
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
        //t = (TextView) par.findViewById(R.id.pay_error_description);
        //errorText += " - " + t.getText().toString();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        dataSource.close();
    }
}
