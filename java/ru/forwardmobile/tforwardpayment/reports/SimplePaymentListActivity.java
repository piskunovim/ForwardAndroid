package ru.forwardmobile.tforwardpayment.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ru.forwardmobile.tforwardpayment.DataEntryActivity;
import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.PaymentQueueWrapper;
import ru.forwardmobile.util.http.Dates;

/**
 * Created by Василий Ванин on 25.07.2014.
 */
public class SimplePaymentListActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final Integer UNPROCESSED_PAYMENT_LIST_TYPE = 0;
    public static final Integer ALL_PAYMENT_LIST_TYPE = 1;
    public static final String  LIST_TYPE   = "payment-list-type";


    ListView                listView = null;
    PaymentListDataSource dataSource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.simple_payment_list);

        listView    = (ListView) findViewById(R.id.payment_list_view);
        listView.setOnItemClickListener(this);

        dataSource  = new PaymentListDataSource(this);

        showPaymentList(getIntent().getIntExtra(LIST_TYPE, 0));
    }

    private void showPaymentList(int intExtra) {

        if( intExtra == UNPROCESSED_PAYMENT_LIST_TYPE ) {
            listView.setAdapter(PaymentAdapterFactory.getAdapter(dataSource.getUnprocessed(), this));
        } else
        if( intExtra == ALL_PAYMENT_LIST_TYPE) {
            listView.setAdapter(PaymentAdapterFactory.getAdapter(dataSource.getAll(), this));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        showFullPaymentInfo((PaymentInfo) listView.getAdapter().getItem(i));
    }

    /**
     * Показывает полную информацию о платеже
     * @param payment
     */
    protected void showFullPaymentInfo(final PaymentInfo payment) {

        StringBuilder builder = new StringBuilder();
        builder.append("Оператор: " + payment.getPsName() + "\n")
               .append(payment.getTarget().getLabel() + ": "
                        + payment.getTarget().getValue() + "\n")
               .append("Принято / К зачислению: \n"
                        + payment.getValue() + "/" + payment.getFullValue() + " руб.\n")
               .append("Создан: " + Dates.Format(payment.getStartDate(), "dd.MM.yy HH:mm:ss") + "\n");

        if(payment.getFinishDate() != null) {
            builder.append("Завершен: " + Dates.Format(payment.getFinishDate(), "dd.MM.yy HH:mm:ss") + "\n");
        }

        builder.append("Состояние: " + payment.getStatusName() + "\n");
        if(payment.getErrorCode() > 0) {
            builder.append("Ошибка: " + payment.getErrorDescription() + "\n");
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Информация о платеже");
        alert.setMessage(builder.toString());

        if( payment.getStatus() == IPayment.CANCELLED || payment.getStatus() == IPayment.FAILED ) {
            alert.setNegativeButton("Операции", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    createPaymentManagementMenu(payment);


                }
            });
        }

        alert.setPositiveButton("Закрыть", null);
        alert.show();
    }


    protected void createPaymentManagementMenu(final PaymentInfo payment) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,
                new String[]{"Корректировать","Повторить отправку"});

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Операции с платежем");
        alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {

                    Intent intent = new Intent(SimplePaymentListActivity.this, DataEntryActivity.class);
                           intent.putExtra(DataEntryActivity.PAYMENT_PARAMETER, payment.getId());

                    startActivity(intent);
                    SimplePaymentListActivity.this.finish();
                } else
                if(i == 1){
                    try {

                        PaymentQueueWrapper.getQueue().processPayment(payment);
                        Toast.makeText(SimplePaymentListActivity.this,"Платеж поставлен в очередь!", Toast.LENGTH_LONG)
                                .show();
                    } catch(Exception ex) {
                        Toast.makeText(SimplePaymentListActivity.this,"Ошибка постановки в очередь: "
                                + ex.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });

        alert.setPositiveButton("Назад", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showFullPaymentInfo(payment);
            }
        });

        alert.setNegativeButton("Закрыть",null);
        alert.show();
    }
}
