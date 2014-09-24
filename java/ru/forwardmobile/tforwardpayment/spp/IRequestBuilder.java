package ru.forwardmobile.tforwardpayment.spp;

import ru.forwardmobile.tforwardpayment.operators.IProcessingAction;

/**
 * Created by vaninv on 19.09.2014.
 */
public interface IRequestBuilder {
    public String buildRequest(IProcessingAction action, IPayment payment);
}
