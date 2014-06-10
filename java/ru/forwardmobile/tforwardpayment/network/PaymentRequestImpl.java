package ru.forwardmobile.tforwardpayment.network;

import java.util.ArrayList;
import java.util.List;

import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.util.http.RequestImpl;

/**
 * Created by Vasiliy Vanin on 06.06.2014.
 */
public class PaymentRequestImpl extends RequestImpl implements IPaymentRequest {

    final List<IPayment> payments = new ArrayList<IPayment>();

    @Override
    public int addPayment(IPayment payment) {
        payments.add(payment);
        return payments.indexOf(payment);
    }


}
