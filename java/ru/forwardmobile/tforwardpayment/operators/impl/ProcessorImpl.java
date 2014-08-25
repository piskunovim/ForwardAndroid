package ru.forwardmobile.tforwardpayment.operators.impl;

import ru.forwardmobile.tforwardpayment.operators.IProcessingAction;
import ru.forwardmobile.tforwardpayment.operators.IProcessor;

/**
 * @author Василий Ванин
 */
public class ProcessorImpl implements IProcessor  {
    
    private Integer     providerId  = 0;
    private boolean     offline     = false;
    
    private IProcessingAction checkAction       = null;
    private IProcessingAction paymentAction     = null;
    private IProcessingAction getStatusAction   = null;
    
    @Override
    public Integer getProviderId() {
        return providerId;
    }

    @Override
    public boolean isOffline() {
        return offline;
    }

    @Override
    public IProcessingAction getCheckAction() {
        return checkAction;
    }

    @Override
    public IProcessingAction getPaymentAction() {
        return paymentAction;
    }

    @Override
    public IProcessingAction getStatusAction() {
        return getStatusAction;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public void setCheckAction(IProcessingAction checkAction) {
        this.checkAction = checkAction;
    }

    public void setPaymentAction(IProcessingAction paymentAction) {
        this.paymentAction = paymentAction;
    }

    public void setGetStatusAction(IProcessingAction getStatusAction) {
        this.getStatusAction = getStatusAction;
    }

}
