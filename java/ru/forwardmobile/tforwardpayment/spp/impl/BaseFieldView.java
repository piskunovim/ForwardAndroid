
package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.spp.IFieldView;

/**
 *
 * @author Vasiliy Vanin
 */
public abstract class BaseFieldView extends LinearLayout implements IFieldView {

    public final LayoutParams DEFAULT_LAYOUT_PARAMS =
            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    
    final String name;
    final String label;

    final TextView labelView;          
          
    public BaseFieldView(Context context, String name, String label) {
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

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public View getView() {
        return this;
    }
    
    public static IFieldView fieldInfo(final String name, final String value, final String label) {
        return new IFieldView() {

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }

            public String getLabel() {
                return label;
            }

            @Override
            public View getView() {
                throw new UnsupportedOperationException("Use FieldFactory to create viewable fields.");
            }

            @Override
            public void setValue(String value) {
                throw new UnsupportedOperationException("Use FieldFactory to create editable fields.");
            }
        };
    }
}
