package ru.forwardmobile.tforwardpayment.reports;

import android.os.Bundle;
import android.widget.ListView;

import ru.forwardmobile.tforwardpayment.AbstractBaseActivity;
import ru.forwardmobile.tforwardpayment.R;

/**
 * Created by Василий Ванин on 25.07.2014.
 */
public class SimplePaymentListActivity extends AbstractBaseActivity {

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
        dataSource  = new PaymentListDataSource(this);

        showPaymentList(getIntent().getIntExtra(LIST_TYPE, 0));
    }

    private void showPaymentList(int intExtra) {

        if( intExtra == UNPROCESSED_PAYMENT_LIST_TYPE ) {
            listView.setAdapter(new PaymentListAdapter(dataSource.getUnprocessed(), this));
        } else
        if( intExtra == ALL_PAYMENT_LIST_TYPE) {
            listView.setAdapter(new PaymentListAdapter(dataSource.getAll(), this));
        }
    }

}
