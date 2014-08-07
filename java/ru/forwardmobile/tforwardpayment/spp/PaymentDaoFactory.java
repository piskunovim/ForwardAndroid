package ru.forwardmobile.tforwardpayment.spp;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Created by Василий Ванин on 06.08.2014.
 */
public class PaymentDaoFactory {

    public static IPaymentDao getPaymentDao(Context ctx) {
        return new PaymentDaoImpl(ctx);
    }
}
