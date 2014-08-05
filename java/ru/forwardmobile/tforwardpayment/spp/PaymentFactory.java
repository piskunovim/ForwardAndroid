package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentPojoImpl;

/**
 * DI контейнер для платежей
 * @author Vasiliy Vanin
 */
public class PaymentFactory {

    public static IPayment getPayment() {
        return new PaymentPojoImpl();
    }
    
    public static IPayment getPayment(Integer psid, Double value, Double fullValue, Collection<IFieldInfo> fields) {
        PaymentPojoImpl payment = new PaymentPojoImpl(psid, value, fullValue);
        for(IFieldInfo field: fields)
            payment.addField(field);
        
        return payment;
    }
    
}
