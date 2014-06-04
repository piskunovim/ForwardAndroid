package ru.forwardmobile.tforwardpayment.spp.impl;

import java.util.LinkedHashSet;

import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IQueue;

/**
 * Created by vaninv on 03.06.2014.
 */
public class QueueImpl extends LinkedHashSet<IPayment> implements IQueue  {

    @Override
    public void addPayment(IPayment payment) throws Exception {
        add(payment);
    }

    @Override
    public Integer getActiveCount() {
        return null;
    }
}
