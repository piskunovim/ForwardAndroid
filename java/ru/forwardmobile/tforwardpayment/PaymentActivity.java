package ru.forwardmobile.tforwardpayment;

import android.util.Log;

/**
 * Created by PiskunovI on 27.05.14.
 */
public class PaymentActivity {

    final String LOG_TAG = "TFORWARD.PaymentActivity";
    int operator_id;



    public void SetOperatorId(int id){
        operator_id = id;
        Log.d(LOG_TAG, Integer.toString(operator_id));
    }
}
