package ru.forwardmobile.tforwardpayment.operators.impl;

import java.util.ArrayList;
import java.util.Collection;

import ru.forwardmobile.tforwardpayment.operators.IProcessingAction;
import ru.forwardmobile.tforwardpayment.operators.IRequestProperty;

/**
 * @author Василий Ванин
 */
public class ProcessingActionImpl implements IProcessingAction {


    private Double  amountValue     = 0d;
    private boolean withCheck       = false;
    private Integer psId            = null;
    
    private Collection<IRequestProperty> requestProperties = 
                new ArrayList<IRequestProperty>();
    
    @Override
    public Double getAmountValue() {
        return amountValue;
    }

    @Override
    public boolean isWithCheck() {
        return withCheck;
    }

    @Override
    public Integer getPsId() {
        return psId;
    }

    @Override
    public Collection<IRequestProperty> getRequestProperties() {
        return requestProperties;
    }

    public void setAmountValue(Double amountValue) {
        this.amountValue = amountValue;
    }

    public void setWithCheck(boolean withCheck) {
        this.withCheck = withCheck;
    }

    public void setPsId(Integer psId) {
        this.psId = psId;
    }

    public void setRequestProperties(Collection<IRequestProperty> requestProperties) {
        this.requestProperties = requestProperties;
    }
    
}
