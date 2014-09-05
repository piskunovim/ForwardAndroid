package ru.forwardmobile.tforwardpayment.operators;


/**
 * @author Василий Ванин
 */
public interface IProcessor {

    /** @return Integer идентификатор Оператора из operators.xml */
    public Integer getProviderId();
    
    /** @return boolean разрешен ли offline режим */
    public boolean isOffline();
    
    public IProcessingAction    getCheckAction();
    
    public IProcessingAction    getPaymentAction();
    
    public IProcessingAction    getStatusAction();
}
