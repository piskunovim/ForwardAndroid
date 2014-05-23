
package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 *
 * @author Vasiliy Vanin
 */
public abstract class BaseField extends LinearLayout implements IField {

    final LayoutParams DEFAULT_LAYOUT_PARAMS =
            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    
    final String name;
    final String label;
    
    final TextView labelView;          
          
    public BaseField(Context context, String name, String label) {
        super(context);
        
        this.name  = name;
        this.label = label;
        
        labelView = new TextView(context);
        labelView.setLayoutParams(DEFAULT_LAYOUT_PARAMS);
        labelView.setText(label);        
        
        this.addView(labelView);
        this.setOrientation( LinearLayout.VERTICAL );
        this.setLayoutParams( DEFAULT_LAYOUT_PARAMS );
    }

    public String getName() {
        return name;
    }

    public View getView() {
        return this;
    }
}
