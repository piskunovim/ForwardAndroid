package ru.forwardmobile.tforwardpayment.spp;

/**
 * Очередь платежей
 * Created by vaninv on 03.06.2014.
 */
public interface IQueue {
    public void     addPayment(IPayment payment) throws Exception;
    public Integer  getActiveCount();
}
