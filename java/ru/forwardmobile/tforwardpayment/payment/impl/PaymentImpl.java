package ru.forwardmobile.tforwardpayment.payment.impl;

import java.util.Collection;
import java.util.Collections;
import ru.forwardmobile.tforwardpayment.payment.IField;
import ru.forwardmobile.tforwardpayment.payment.IPayment;

/**
 * @author Vasiliy Vanin
 */
public class PaymentImpl implements IPayment {

    private final Collection<IField>   fields;
    private final Integer       psid;
    private final Double        value;
    private final Double        fullValue;
    
        
    public PaymentImpl(Integer psid, Double value, Double fullValue, Collection<IField> fields) {
        this.psid  = psid;
        this.value = value;
        this.fullValue = fullValue;
        this.fields = fields;
    }
    
    public Collection<IField> getFields() {
        return Collections.unmodifiableCollection(fields);
    }

    public Integer getPsid() {
        return psid;
    }

    public Double getValue() {
        return value;
    }

    public Double getFullValue() {
        return fullValue;
    }
}
