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
    private Integer transactionId;
    private Integer errorCode;
    private Date    finishDate;
    private Integer status;
    private int     tryCount;
    private String  errorDescription;


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
        return transactionId;
    }

    @Override
    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
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
        return errorCode;
    }

    @Override
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
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
        return finishDate;
    }

    @Override
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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
        return status;
    }

    @Override
    public void setStatus(Integer status) {

        // Сбрасываем счетчик повторов при изменении статуса
        if(this.status != status) {
            tryCount = 0;
        }

        this.status = status;

        if ( ( this.status == IPayment.DONE ) ||
             ( this.status == IPayment.CANCELLED ) ||
             ( this.status == IPayment.FAILED ) ) {
            this.setFinishDate(new java.util.Date());
        } else
        if ( this.status == IPayment.NEW ) {
            this.setFinishDate(null);
        }

    }

    @Override
    public boolean isDelayed() {
        return false;
    }


    @Override
    public boolean isPreparedForCancelling() {
        return false;
    }

    @Override
    public void delay(int interval) {

    }

    @Override
    public void errorDelay() {

    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public boolean getActive() {
        return false;
    }

    @Override
    public void setSended(boolean sended) {

    }

    @Override
    public boolean getSended() {
        return false;
    }

    @Override
    public void setDateOfProcess(Date dateOfProcess) {

    }

    @Override
    public Date getDateOfProcess() {
        return null;
    }

    @Override
    public void incTryCount() {

    }

    @Override
    public Integer getTryCount() {
        return null;
    }

    @Override
    public void incErrorRepeatCount() {

    }

    @Override
    public int getErrorRepeatCount() {
        return 0;
    }


}
