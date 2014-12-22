package ru.forwardmobile.tforwardpayment.reports;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.forwardmobile.tforwardpayment.AbstractBaseActivity;
import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.PaymentDaoFactory;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Отображение списка платежей
 * Created by Василий Ванин on 26.09.2014.
 */
public class PaymentReportActivity extends AbstractBaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist);

        ListView paymentList = (ListView) findViewById(R.id.listView);

        IPaymentDao paymentDao = PaymentDaoFactory.getPaymentDao(this);

        final PaymentReportAdapter paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(), this);

        paymentList.setAdapter(paymentReportAdapter);

        paymentList.setOnItemClickListener(this);

        /* paymentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Log.d("PaymentItemClick", "position: " + i);


                /*LinearLayout currentLayout = (LinearLayout) view;
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentReportActivity.this);

                // = Оператор (psTitle)
                TextView textView = (TextView) currentLayout.findViewById(R.id.payment_row_ps);
                String operator = "Оператор: " + textView.getText().toString();

                // = Номер счета
                textView = (TextView) currentLayout.findViewById(R.id.payment_row_fields);
                String num = "Счет: " + textView.getText().toString();

                // = Сумма принятая/к зачислению
                textView = (TextView) currentLayout.findViewById(R.id.payment_row_amount);
                String sum = "Принято/К зачислению: \n" + textView.getText().toString();

                // = Дата совершения платежа
                textView = (TextView) currentLayout.findViewById(R.id.pay_date_begin);
                String dateBegin = "Отправлен: " + textView.getText().toString();

                // = Дата завершения платежа
                textView = (TextView) currentLayout.findViewById(R.id.pay_date_end);
                String dateEnd = "Завершен: " + textView.getText().toString();

                // = Статус платежа
                textView = (TextView) currentLayout.findViewById(R.id.pay_status);
                String stat = "Статус: " + textView.getText().toString();

                builder.setTitle("Подробная информация:");
                builder.setMessage(operator +"\n" + num + "\n" + sum + "\n" + dateBegin + "\n" + dateEnd + "\n" + stat);

                builder.show();

                Toast.makeText(getApplicationContext(), operator, Toast.LENGTH_SHORT).show();*/
         //   }
        //});

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("PaymentItemClick", "position: " + i);
        Toast.makeText(this, "PaymentItemClick", Toast.LENGTH_SHORT).show();
    }


}
