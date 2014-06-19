package ru.forwardmobile.tforwardpayment.spp.impl;

import android.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * POJO реализация платежа
 * @author Vasiliy Vanin
 */
public class PaymentPojoImpl implements IPayment {
    final String                    LOGGER_TAG = "TFORWARD.PAYMENTIMPL";
    private Double                    value;
    private Double                    fullValue;
    private Integer                   psId;
    private Collection<IFieldInfo>    fields = new HashSet<IFieldInfo>();

    private Integer id = null;
    private Date    startDate;
    private Integer transactionId;
    private Integer errorCode;
    private Date    finishDate;
    private Date    dateOfProcess;
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
    public void    setPsid(Integer psId) {
        this.psId = psId;
    }

    public Double getValue() {
        return value;
    }
    public void   setValue(Double value) {
        this.value = value;
    }
    public void   setValue(Integer value) {
        setValue(value/100);
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
    public boolean isPreparedForCancelling() {
        return false;
    }

    @Override
    public void delay(int interval) {
        Log.d(LOGGER_TAG, "#" + getId().toString() + " delayed to " + interval + " sec., tryCount " + tryCount);
        setDateOfProcess(new Date(System.currentTimeMillis() +  (long) interval * 1000));
    }

    @Override
    public void errorDelay() {
        incTryCount();
        if ( tryCount >= TSettings.getInt(TSettings.MAXIMUM_TRY_COUNT, 100)) {
            Log.w(LOGGER_TAG, "#" + id.toString() + ": To many tries (" + tryCount + "). Processing finished.");
            setStatus(IPayment.FAILED);
            setErrorDescription("Состояние платежа не определено. " + getErrorDescription());
        } else {
            delay(TSettings.getInt(TSettings.QUEUE_ERROR_DELAY, 60));
        }
    }

    @Override
    public boolean isDelayed() {
        return false;
    }

    @Override
    public void setDelayed(boolean delayed) {

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
        this.dateOfProcess = dateOfProcess;
    }

    @Override
    public Date getDateOfProcess() {
        return dateOfProcess;
    }

    @Override
    public void incTryCount() {

    }

    @Override
    public Integer getTryCount() {
        return null;
    }

    @Override
    public void setTryCount(Integer count) {

    }

    @Override
    public void incErrorRepeatCount() {

    }

    @Override
    public int getErrorRepeatCount() {
        return 0;
    }

    @Override
    public String getStatusName() {
        return null;
    }
}
