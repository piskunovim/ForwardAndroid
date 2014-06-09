package ru.forwardmobile.tforwardpayment.payment.impl;

import android.content.Context;
import android.widget.EditText;
/**
 * @author Vasiliy Vanin
 */
public class TextFieldImpl extends BaseField { 

    
    EditText valueView;
    
    public TextFieldImpl(Context ctx, String name, String label) {
        super(ctx, name, label);

        valueView = new EditText(ctx);
        valueView.setLayoutParams(DEFAULT_LAYOUT_PARAMS);
        valueView.setText(name);
        
        this.addView(valueView);
    }
    
    public String getValue() {
        return valueView.getText()
                .toString();
    }
}
