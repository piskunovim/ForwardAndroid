package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;
import ru.forwardmobile.tforwardpayment.spp.impl.BaseField;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Created by vaninv on 30.05.2014.
 */
public class TestActivity extends Activity {

    final static String LOGGER_TAG = "TFORWARD.PAYMENT";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        IPayment payment = PaymentFactory.getPayment(1, 5.00, 5.00, Arrays.asList(BaseField.fieldInfo("target","9182074447")));
        IPaymentDao dao = new PaymentDaoImpl(new DatabaseHelper(this).getWritableDatabase());

        dao.save(payment);

    }

}
