package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;

/**
 * Created by vaninv on 30.05.2014.
 */
public interface IPaymentDao {
    public void                     save(IPayment payment);
    public IPayment                 find(Integer id);
    public IPayment                 findByTransaction(Integer transactid);
    public Collection<IPayment>     getCollection();
}
