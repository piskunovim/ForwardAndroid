package ru.forwardmobile.tforwardpayment.spp.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * POJO реализация платежа
 * @author Vasiliy Vanin
 */
public class PaymentPojoImpl implements IPayment {
    
    final Double                    value;
    final Double                    fullValue;
    final Integer                   psId;
    final Collection<IFieldInfo>    fields = new HashSet<IFieldInfo>();

    private Integer id;
    private Date    startDate;

    public PaymentPojoImpl(Integer psid, Double value, Double fullValue) {
        this.psId       = psid;
        this.value      = value;
        this.fullValue  = fullValue;
    }    

    public void addField(IFieldInfo field) {
        fields.add(field);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getTransactionId() {
        return null;
    }

    @Override
    public void setTransactionId(Integer transactionId) {

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

    @Override
    public Integer getErrorCode() {
        return null;
    }

    @Override
    public void setErrorCode(Integer errorCode) {

    }

    @Override
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getFinishDate() {
        return null;
    }

    @Override
    public void setFinishDate(Date finishDate) {

    }

    @Override
    public IFieldInfo getTarget() {

        for(IFieldInfo field: fields) {
            if("target" . equals( field.getName() )) {
                return field;
            }
        }
        return null;
    }

    @Override
    public Integer getStatus() {
        return null;
    }

    @Override
    public void setStatus(Integer status) {

    }
}
