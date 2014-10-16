package ru.forwardmobile.tforwardpayment.reports;

import android.os.Bundle;
import android.widget.ListView;

import ru.forwardmobile.tforwardpayment.AbstractBaseActivity;
import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.PaymentDaoFactory;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Отображение списка платежей
 * Created by Василий Ванин on 26.09.2014.
 */
public class PaymentReportActivity extends AbstractBaseActivity {

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

        PaymentReportAdapter paymentReportAdapter = new PaymentReportAdapter(paymentDao.getPayments(), this);

        paymentList.setAdapter(paymentReportAdapter);

    }
}
