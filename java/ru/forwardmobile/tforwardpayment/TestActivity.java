package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Created by vaninv on 30.05.2014.
 */
public class TestActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        try {
        /*    IPayment payment = PaymentFactory.getPayment(1, 500.00, Arrays.asList(BaseField.fieldInfo("target", "test")));
                     payment.setStartDate(new Date());

            IPaymentDao paymentDao = new PaymentDaoImpl(new DatabaseHelper(this).getWritableDatabase());

            paymentDao.save(payment);
            Log.i("TESTTFWD", "ID Is: " + payment.getId());*/

            IPayment payment = new PaymentDaoImpl(new DatabaseHelper(this).getWritableDatabase()).find(1);
            Log.i("TESTFWD", payment.getTarget().getValue());

        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
