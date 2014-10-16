package ru.forwardmobile.tforwardpayment.spp.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IProvidersDataSource;
import ru.forwardmobile.tforwardpayment.spp.ProvidersDataSourceFactory;

/**
 * Created by vaninv on 11.09.2014.
 */
public class DefaultPaymentImpl extends AbstractPaymentImpl {

    private Integer id = null;

    private String title;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    private Integer transactionId = null;
    @Override
    public Integer getTransactionId() {
        return transactionId;
    }

    @Override
    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    private Collection<IField> fields = new ArrayList<IField>();
    @Override
    public Collection<IField> getFields() {
        return fields;
    }

    private Integer psid = null;
    @Override
    public Integer getPsid() {
        return psid;
    }

    private Double value = 0d;
    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    private Double fullValue = 0d;
    @Override
    public Double getFullValue() {
        return fullValue;
    }

    private Integer errorCode = null;
    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    private String errorDescription = null;
    @Override
    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    private Date startDate = new Date();
    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    private Date finishDate = null;
    @Override
    public Date getFinishDate() {
        return finishDate;
    }

    @Override
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public IField getTarget() {
        for(IField field: fields) {
            if(100 == field.getId())
                return field;
        }
        return null;
    }

    private Integer status = 0;
    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    private boolean preparedForCancelling = false;
    @Override
    public boolean isPreparedForCancelling() {
        return preparedForCancelling;
    }

    @Override
    public void delay(int interval) {
        setDateOfProcess(new Date(System.currentTimeMillis() +  (long)interval * 1000));
    }

    @Override
    public void errorDelay() {
        incTryCount();
        if ( tryCount >= TSettings.getInt(TSettings.MAXIMUM_TRY_COUNT, 10)) {
            setStatus(IPayment.FAILED);
            setErrorDescription("Состояние платежа не определено. " + getErrorDescription());
        } else {
            delay(TSettings.getInt(TSettings.QUEUE_ERROR_DELAY, 60));
        }
    }


    private boolean delayed = false;
    @Override
    public boolean isDelayed() {
        return delayed;
    }

    @Override
    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    private boolean active = false;
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean getActive() {
        return active;
    }

    private boolean sent = false;
    @Override
    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public boolean getSent() {
        return sent;
    }

    private Date dateOfProcess = null;
    @Override
    public void setDateOfProcess(Date dateOfProcess) {
        this.dateOfProcess = dateOfProcess;
    }

    @Override
    public Date getDateOfProcess() {
        return dateOfProcess;
    }

    private Integer tryCount = 0;
    @Override
    public void incTryCount() {
        tryCount++;
    }

    @Override
    public Integer getTryCount() {
        return tryCount;
    }

    @Override
    public void setTryCount(Integer count) {
        this.tryCount = count;
    }

    private Integer errorRepeatCount = 0;
    @Override
    public void incErrorRepeatCount() {
        errorRepeatCount++;
    }

    @Override
    public int getErrorRepeatCount() {
        return errorRepeatCount;
    }

    @Override
    public String getPsTitle() {
        return this.title;
    }

    @Override
    public void setPsTitle(String title) {
        this.title = title;
    }


    @Override
    public void setPsid(Integer psid) {
        this.psid = psid;
    }

    @Override
    public void setFullValue(Double fullValue) {
        this.fullValue = fullValue;
    }

    @Override
    public void setFields(Collection<IField> fields) {
        this.fields = fields;
    }

    @Override
    public IField getField(Integer id) {
        for (IField field: fields) {
            if(id == field.getId()) {
                return field;
            }
        }
        return null;
    }
}
