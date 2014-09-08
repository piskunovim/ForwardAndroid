package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;
import android.widget.EditText;
/**
 * @author Vasiliy Vanin
 */
public class TextFieldViewImpl extends BaseFieldView {

    
    EditText valueView;
    
    public TextFieldViewImpl(Context ctx, String name, String label) {
        super(ctx, name, label);

        valueView = new EditText(ctx);
        valueView.setLayoutParams(DEFAULT_LAYOUT_PARAMS);
        
        this.addView(valueView);
    }
    
    public String getValue() {
        return valueView.getText()
                .toString();
    }


    @Override
    public void setValue(String value) {
        valueView.setText(value);
    }
}
