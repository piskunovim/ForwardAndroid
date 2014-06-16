package ru.forwardmobile.tforwardpayment.reports;

/**
 * Created by PiskunovI on 16.06.14.
 */
public class PaymentListItemsImpl {

    String psid,
           transactid,
           fields,
           value,
           fullValue,
           errorCode,
           errorDescription,
           startDate,
           status,
           processDate;


    //psid
    public String getPsid() {
        return psid;
    }

    public void setPsid(String psid) {
        this.psid = psid;
    }

    //transactid
    public String getTransactid() {
        return transactid;
    }

    public void setTransactid(String transactid) {
        this.transactid = transactid;
    }

    //fields
    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    //value
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //fullValue
    public String getFullValue() {
        return fullValue;
    }

    public void setFullValue(String fullValue) {
        this.fullValue = fullValue;
    }

    //errorCode
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    //errorDescription
    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    //startDate
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    //status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //processDate
    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

}
