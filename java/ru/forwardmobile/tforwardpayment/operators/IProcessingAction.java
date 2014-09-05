package ru.forwardmobile.tforwardpayment.operators;

import java.util.Collection;

/**
 * @author Василий Ванин
 */
public interface IProcessingAction {
    
    public Double                           getAmountValue();
    public boolean                          isWithCheck();
    public Integer                          getPsId();
    public Collection<IRequestProperty>     getRequestProperties();
}
