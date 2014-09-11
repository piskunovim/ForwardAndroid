package ru.forwardmobile.tforwardpayment.spp;

import ru.forwardmobile.tforwardpayment.spp.impl.DefaultPaymentImpl;

/**
 * Created by Василий Ванин on 11.09.2014.
 */
public class PaymentFactory {
    public static IPayment getPayment() {
        return new DefaultPaymentImpl();
    }
}
