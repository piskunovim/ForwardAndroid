package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;

/**
 * @author Vasiliy Vanin
 */
public interface IPayment {
    public Collection<IField>   getFields();
    public Integer              getPsid();
    public Double               getValue();
    public Double               getFullValue();
}
