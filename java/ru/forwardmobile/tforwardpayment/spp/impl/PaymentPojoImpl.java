package ru.forwardmobile.tforwardpayment.spp.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * POJO реализация провайдера
 * @author Vasiliy Vanin
 */
public class PaymentPojoImpl implements IPayment {
    
    final Double                    value;
    final Double                    fullValue;
    final Integer                   psId;
    final Collection<IFieldInfo>    fields = new HashSet<IFieldInfo>();
    
    public PaymentPojoImpl(Integer psid, Double value, Double fullValue) {
        this.psId       = psid;
        this.value      = value;
        this.fullValue  = fullValue;
    }    

    public void addField(IFieldInfo field) {
        fields.add(field);
    }
    
    public Collection<IFieldInfo> getFields() {
        return Collections.unmodifiableCollection(fields);
    }

    public Integer getPsid() {
        return psId;
    }

    public Double getValue() {
        return value;
    }

    public Double getFullValue() {
        return fullValue;
    }
    
}
