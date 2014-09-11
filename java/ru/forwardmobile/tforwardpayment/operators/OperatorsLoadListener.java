package ru.forwardmobile.tforwardpayment.operators;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.MainAccessActivity;

/**
 * Created by vaninv on 10.09.2014.
 */
public class OperatorsLoadListener implements MainAccessActivity.onLoadListener {

    @Override
    public void beforeApplicationStart(Context context) {
        // вот и все
        OperatorsEntityManagerFactory.getManager(context);
    }
}
