package ru.forwardmobile.tforwardpayment.spp;

import android.view.View;

/**
 *
 * @author Vasiliy Vanin
 */
public interface IField extends IFieldInfo {
    
    public static final int TEXT_FIELD_TYPE = 1;
    public static final int ENUMERATION_FIELD_TYPE = 2;

    public View   getView();
}
