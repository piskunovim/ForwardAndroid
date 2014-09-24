package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.IProvidersDataSource;
import ru.forwardmobile.tforwardpayment.operators.OperatorsEntityManagerFactory;
import ru.forwardmobile.tforwardpayment.spp.PaymentDaoFactory;

/**
 * Created by Василий Ванин on 30.05.2014.
 */
public class TestActivity extends Activity {

    final static String LOGGER_TAG = "TFORWARD.PAYMENT";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        Log.i(LOGGER_TAG, "OnCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOGGER_TAG, "OnStart()");

        IPaymentDao dao = PaymentDaoFactory.getPaymentDao(this);
        IPayment payment = dao.find(2);

        Log.i(LOGGER_TAG, "Target: " + payment.getTarget().getValue().getValue());
        Log.i(LOGGER_TAG, "Processed: " + payment.getDateOfProcess());


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOGGER_TAG, "OnResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOGGER_TAG, "OnRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOGGER_TAG, "onStop()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOGGER_TAG, "onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(LOGGER_TAG, "OnRestoreInstanceState()");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }
}
