package ru.forwardmobile.tforwardpayment.network;

import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.util.http.IRequest;

/**
 * Created by vaninv on 06.06.2014.
 */
public interface IPaymentRequest extends IRequest {
    /** @return int - index of payment */
    public int addPayment(IPayment payment);
}
