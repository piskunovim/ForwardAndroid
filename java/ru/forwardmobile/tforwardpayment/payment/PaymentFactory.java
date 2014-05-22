package ru.forwardmobile.tforwardpayment.payment;

import java.util.Collection;
import ru.forwardmobile.tforwardpayment.payment.impl.PaymentImpl;

/**
 * @author Vasiliy Vanin
 */
public class PaymentFactory {
    
    public IPayment getPayment(Integer psid, Double value, Double fullValue, Collection<IField> fields) {
        return new PaymentImpl(psid, value, fullValue, fields);
    }    
}
