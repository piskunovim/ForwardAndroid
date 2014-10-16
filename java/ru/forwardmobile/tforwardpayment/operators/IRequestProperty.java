package ru.forwardmobile.tforwardpayment.operators;

import java.util.Collection;

import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * @author Василий Ванин
 */
public interface IRequestProperty {
    
    public static final String  TARGET_REQUEST_PROPERTY = "NUMBER";
    public static final String  FIXED_TEXT_ITEM_TYPE    = "fixed-text";
    public static final String  FIELD_REF_ITEM_TYPE     = "field-ref";
    
    public String                               getName();
    public String                               getRefField();
    public Collection<IRequestPropertyItem>     getItems();
    public String                               toXml(IPayment payment);
    
    public interface IRequestPropertyItem {
        public String getType();
        public String getValue();
    }
}
