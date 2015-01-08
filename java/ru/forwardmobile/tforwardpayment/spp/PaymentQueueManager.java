package ru.forwardmobile.tforwardpayment.spp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.forwardmobile.tforwardpayment.MainAccessActivity;
import ru.forwardmobile.tforwardpayment.TPaymentService;

/**
 * Created by PiskunovI on 09.09.2014.
 */
public class PaymentQueueManager implements MainAccessActivity.onLoadListener {

    String LOG_TAG = "PaymentQueueManager";

    @Override
    public String beforeApplicationStart(Context context) {
        startPaymentQueue(context);
        return null;
    }

    private void startPaymentQueue(Context context) {
        Log.i(LOG_TAG, "Starting payment queue...");
        context.startService(new Intent(context, TPaymentService.class));
    }

    private void stopPaymentQueue(Context context) {
        Log.i(LOG_TAG,"Deactivating payment queue...");
        context.stopService(new Intent(context,TPaymentService.class));
    }
}
