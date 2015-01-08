package ru.forwardmobile.tforwardpayment.spp;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.spp.impl.DefaultPaymentImpl;

/**
 * Created by Василий Ванин on 11.09.2014.
 */
public class PaymentFactory {
    public static IPayment getPayment(Context context) {
        return new DefaultPaymentImpl(context);
    }
}
