package ru.forwardmobile.tforwardpayment.payment;

import java.util.Collection;

/**
 *
 * @author Vasiliy Vanin
 */
public interface IProvider {
    public Integer              getId();
    public Double               getMaxSumm();
    public Double               getMinSumm();
    public Collection<IField>   getFields();
}
