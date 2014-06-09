package ru.forwardmobile.tforwardpayment.spp;

import ru.forwardmobile.tforwardpayment.spp.impl.PaymentQueueImpl;

/**
 * DI - container for queue
 * Created by vaninv on 09.06.2014.
 */
public class PaymentQueueWrapper {

    private static final IPaymentQueue queue = new PaymentQueueImpl();

    private PaymentQueueWrapper(){}

    public static IPaymentQueue getQueue() {
        return queue;
    }
}
