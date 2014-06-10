package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;

/**
 * @author Vasiliy Vanin
 */
public interface IPayment {
    
    public static final int NEW       = 0;
    public static final int CHECKED   = 1;
    public static final int COMMITED  = 2;
    public static final int DONE      = 3;
    public static final int FAILED    = 4;
    public static final int CANCELLED = 5;    
    
    
    public Collection<IFieldInfo>   getFields();
    public Integer                  getPsid();
    public Double                   getValue();
    public Double                   getFullValue();
}
