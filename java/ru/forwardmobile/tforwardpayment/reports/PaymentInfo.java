package ru.forwardmobile.tforwardpayment.reports;

import ru.forwardmobile.tforwardpayment.spp.impl.PaymentPojoImpl;

/**
 * Created by Василий Ванин on 04.07.2014.
 */
public class PaymentInfo extends PaymentPojoImpl {

    private String psName  = null;
    private Integer status = 0;

    public PaymentInfo(Integer psid, Double value, Double fullValue) {
        super(psid, value, fullValue);
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getPsName() {
        return psName;
    }

    // Переопределяем метод setStatus(), чтобы не выполнялось никаких действий,
    // связанных с изменением статуса
    public void setStatus(Integer status) {
        this.status = status;
    }

    // теперь суперкласс получит именно это значение статуса
    public Integer getStatus() {
        return this.status;
    }


}
