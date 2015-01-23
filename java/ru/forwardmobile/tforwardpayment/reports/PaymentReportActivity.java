package ru.forwardmobile.tforwardpayment.reports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
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
import java.util.Date;
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
    EditText startEditText, finishEditText;
    boolean[] filterParams = {true,true,true,true};

    Calendar calendar = Calendar.getInstance();
    Date startDate,finishDate;

    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist);

        final ListView paymentList = (ListView) findViewById(R.id.listView);

        Button fullList = (Button) findViewById(R.id.fullList);

        Button dateList = (Button) findViewById(R.id.dateList);

        final DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate("startEditText");

                if (finishDate != null){
                    IPaymentDao paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                    final PaymentReportAdapter paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(startDate,finishDate), PaymentReportActivity.this);

                    paymentList.setAdapter(paymentReportAdapter);

                    paymentList.setOnItemClickListener(PaymentReportActivity.this);
                }
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
                updateDate("finishEditText");
                if (startDate != null){
                    IPaymentDao paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                    final PaymentReportAdapter paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(startDate,finishDate), PaymentReportActivity.this);

                    paymentList.setAdapter(paymentReportAdapter);

                    paymentList.setOnItemClickListener(PaymentReportActivity.this);
                }
            }

        };

        startEditText = (EditText) findViewById(R.id.startDate);
        startEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "startDate clicked");
                new DatePickerDialog(PaymentReportActivity.this, sDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                if (startEditText.length() != 0 && finishEditText.length() != 0){
                    IPaymentDao paymentDao = PaymentDaoFactory.getPaymentDao(PaymentReportActivity.this);

                    final PaymentReportAdapter paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(startDate,finishDate), PaymentReportActivity.this);

                    paymentList.setAdapter(paymentReportAdapter);

                    paymentList.setOnItemClickListener(PaymentReportActivity.this);
                }
            }
        });

        finishEditText = (EditText) findViewById(R.id.finishDate);
        finishEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "finishtDate clicked");
                new DatePickerDialog(PaymentReportActivity.this, fDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                if (startEditText.length() != 0 && finishEditText.length() != 0){

                }

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
        final boolean[] mCheckedItems = filterParams;
        final String[] itemsList = {"Полный список","Проведенные","В обработке","Завершены ошибкой"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите настройки отображения")
                .setCancelable(false)

                .setMultiChoiceItems(itemsList, mCheckedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {

                                if (which == 0) {
                                    for (int i = 0; i < itemsList.length; i++) {
                                      ListView list = ((AlertDialog) dialog).getListView();
                                      list.setItemChecked(i,isChecked);

                                      mCheckedItems[i] = isChecked;
                                    }
                                }
                                else{
                                    mCheckedItems[which] = isChecked;
                                }
                            }
                })

                        // Добавляем кнопки
                .setPositiveButton("Готово",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                filterParams = mCheckedItems;
                                StringBuilder state = new StringBuilder();
                                for (int i = 0; i < itemsList.length; i++) {
                                    state.append("" + itemsList[i]);
                                    if (mCheckedItems[i])
                                        state.append(" выбран\n");
                                    else
                                        state.append(" не выбран\n");
                                }
                                Toast.makeText(getApplicationContext(),
                                        state.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        })

                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();

                            }
                        }).show();


        /*new AlertDialog.Builder(this)
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
                */
    }

    //StartLabel

    private void updateDate(String field) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (field.equals("startEditText")) {
            startDate = calendar.getTime();
            startEditText.setText(sdf.format(calendar.getTime()));
        }
        else if (field.equals("finishEditText")) {
            finishDate = calendar.getTime();
            finishEditText.setText(sdf.format(calendar.getTime()));
        }
        else{
            Toast.makeText(this,"Ошибка поля ввода",Toast.LENGTH_SHORT).show();
        }

    }

}
