package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.Date;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        try {
          IPayment payment = PaymentFactory.getPayment(1, 500.00, Arrays.asList(BaseField.fieldInfo("target", "test")));
                     payment.setStartDate(new Date());

            payment.setId(1);
            payment.setStartDate(new Date());

            IPaymentDao paymentDao = new PaymentDaoImpl(new DatabaseHelper(this).getWritableDatabase());

            paymentDao.save(payment);
            Log.i("TESTTFWD", "ID Is: " + payment.getId());

            IPayment payment2 = new PaymentDaoImpl(new DatabaseHelper(this).getWritableDatabase()).find(1);
            Log.i("TESTFWD", payment2.getTarget().getValue());
            Log.i("TESTFWD", payment2.getStartDate().toString());
            Log.i("TESTFWD", String.valueOf(payment2.getFinishDate()));

        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
