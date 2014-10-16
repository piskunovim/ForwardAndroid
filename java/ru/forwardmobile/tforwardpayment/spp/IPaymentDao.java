package ru.forwardmobile.tforwardpayment.spp;

import org.bouncycastle.util.IPAddress;

import java.util.Collection;
import java.util.Date;

/**
 * Created by vaninv on 30.05.2014.
 */
public interface IPaymentDao {
    public void                     save(IPayment payment);
    public IPayment                 find(Integer id);
    public IPayment                 findByTransaction(Integer transactid);
    public Collection<IPayment>     getCollection();
    public Collection<IPayment>     getUnprocessed();
    public Collection<IPayment>     getPayments(Date startDate, Date finishDate);
    public Collection<IPayment>     getPayments();
    public void                     delete(IPayment payment);
    public void                     close();

}
