package ru.forwardmobile.tforwardpayment.reports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.forwardmobile.tforwardpayment.AbstractBaseActivity;
import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.PaymentDaoFactory;


/**
 * Отображение списка платежей
 * Created by Василий Ванин on 26.09.2014.
 */
public class PaymentReportActivity extends AbstractBaseActivity implements AdapterView.OnItemClickListener {

    String LOG_TAG = "PaymentReportActivity";
    EditText startDate, finishDate;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist);

        final DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartLabel();
            }

        };

        final DatePickerDialog.OnDateSetListener fDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFinishLabel();
            }

        };

        final ListView paymentList = (ListView) findViewById(R.id.listView);

        Button fullList = (Button) findViewById(R.id.fullList);

        Button dateList = (Button) findViewById(R.id.dateList);

        startDate = (EditText) findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "startDate clicked");
                new DatePickerDialog(PaymentReportActivity.this, sDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        finishDate = (EditText) findViewById(R.id.finishDate);
        finishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "finishtDate clicked");
                new DatePickerDialog(PaymentReportActivity.this, fDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        onFilterHide();

        IPaymentDao paymentDao = PaymentDaoFactory.getPaymentDao(this);

        final PaymentReportAdapter paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(), this);

        paymentList.setAdapter(paymentReportAdapter);

        paymentList.setOnItemClickListener(this);

        fullList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPaymentsDialog();

                /*
                IPaymentDao paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                final PaymentReportAdapter paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(), PaymentReportActivity.this);

                paymentList.setAdapter(paymentReportAdapter);

                paymentList.setOnItemClickListener(PaymentReportActivity.this);
                */
            }
        });

        dateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilterShow();
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("PaymentItemClick", "position: " + i);

                LinearLayout currentLayout = (LinearLayout) view;
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
                textView = (TextView) currentLayout.findViewById(R.id.payment_row_start);
                String dateBegin = "Отправлен: " + textView.getText().toString();

                // = Дата завершения платежа
                textView = (TextView) currentLayout.findViewById(R.id.payment_row_end);
                String dateEnd = "Завершен: " + textView.getText().toString();

                // = Статус платежа
                textView = (TextView) currentLayout.findViewById(R.id.payment_row_status);
                String stat = "Статус: " + textView.getText().toString();

                builder.setTitle("Подробная информация:");
                builder.setMessage(operator +"\n" + num + "\n" + sum + "\n" + dateBegin + "\n" + dateEnd + "\n" + stat);

                builder.show();
    }

    public void onFilterShow(){
        findViewById(R.id.dateFilter).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonPanel).setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 8.0f));
        findViewById(R.id.listViewContainer).setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 2.0f));

    }

    public void onFilterHide(){
        findViewById(R.id.dateFilter).setVisibility(View.GONE);
        findViewById(R.id.buttonPanel).setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 8.0f));
        findViewById(R.id.listViewContainer).setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));

    }

    private void openPaymentsDialog()
    {
        String[] a = {"Полный список","Проведенные","В обработке","Завершены ошибкой"};

        new AlertDialog.Builder(this)
                .setTitle("Все отчеты")
                .setItems(a,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                onFilterHide();

                                final ListView paymentList = (ListView) findViewById(R.id.listView);

                                IPaymentDao paymentDao;

                                final PaymentReportAdapter paymentReportAdapter;

                                if (i == 0) {
                                    Toast.makeText(PaymentReportActivity.this, "Полный список", Toast.LENGTH_SHORT).show();

                                    paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                                    paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(), PaymentReportActivity.this);

                                    paymentList.setAdapter(paymentReportAdapter);

                                    paymentList.setOnItemClickListener(PaymentReportActivity.this);

                                } else if (i == 1) {
                                    Toast.makeText(PaymentReportActivity.this, "Проведенные", Toast.LENGTH_SHORT).show();

                                    paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                                    paymentReportAdapter = new PaymentReportAdapter(paymentDao.getDone(), PaymentReportActivity.this);

                                    paymentList.setAdapter(paymentReportAdapter);

                                    paymentList.setOnItemClickListener(PaymentReportActivity.this);

                                } else if (i == 2) {
                                    Toast.makeText(PaymentReportActivity.this, "В обработке", Toast.LENGTH_SHORT).show();
                                    paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                                    paymentReportAdapter = new PaymentReportAdapter(paymentDao.getUnprocessed(), PaymentReportActivity.this);

                                    paymentList.setAdapter(paymentReportAdapter);

                                    paymentList.setOnItemClickListener(PaymentReportActivity.this);
                                } else if (i == 3) {
                                    Toast.makeText(PaymentReportActivity.this, "Завершены ошибкой", Toast.LENGTH_SHORT).show();

                                    paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                                    paymentReportAdapter = new PaymentReportAdapter(paymentDao.getFailed(), PaymentReportActivity.this);

                                    paymentList.setAdapter(paymentReportAdapter);

                                    paymentList.setOnItemClickListener(PaymentReportActivity.this);
                                }

                            }
                        })
                .show();
    }

    //StartLabel

    private void updateStartLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateFinishLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        finishDate.setText(sdf.format(calendar.getTime()));
    }

}
