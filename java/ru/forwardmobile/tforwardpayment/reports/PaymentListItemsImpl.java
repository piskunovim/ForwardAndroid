package ru.forwardmobile.tforwardpayment.reports;


import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import 	java.util.Date;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.util.http.Dates;

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

    SQLiteOpenHelper dbHelper;


    //psid
    public String getPsid() {
        return psid;
    }

    public void setPsid(String psid) { this.psid = psid; }

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
        value = value.substring(0,value.length()-2);
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
        Date d = new Date(Long.parseLong(startDate));
        Dates sd = new Dates();
        startDate = sd.Format(d, "yyyy-MM-dd HH:mm:ss");
        this.startDate = startDate;
    }

    //status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        if (status.equals("0")){ status = "Новый"; }
        if (status.equals("1")){ status = "В обработке"; }
        if (status.equals("2")){ status = "В обработке"; }
        if (status.equals("3")){ status = "Проведен"; }
        if (status.equals("4")){ status = "Ошибка"; }
        if (status.equals("5")){ status = "Отменен"; }

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
