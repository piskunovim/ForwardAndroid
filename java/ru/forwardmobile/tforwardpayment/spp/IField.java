package ru.forwardmobile.tforwardpayment.spp;

import android.view.View;

/**
 *
 * @author Vasiliy Vanin
 */
public interface IField {
    
    public static final int TEXT_FIELD_TYPE = 1;
    public static final int ENUMERATION_FIELD_TYPE = 2;
    
    public String getName();
    public String getValue();
    public View   getView();
}
