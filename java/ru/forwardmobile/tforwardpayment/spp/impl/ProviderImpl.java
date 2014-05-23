package ru.forwardmobile.tforwardpayment.spp.impl;

import android.app.Activity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Collection;
import java.util.HashSet;
import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 * Самая простая реализация провайдера,
 * не умеет делать проверку с исключением
 * @author Vasiliy Vanin
 */
public class ProviderImpl extends LinearLayout {

    final Integer               id;
    final Double                maxSumm;
    final Double                minSumm;
    final Collection<IField>    fields = new HashSet<IField>();
    final LinearLayout          fieldsLayout;
    final EditText              value;
    final EditText              fullValue;
    
    public ProviderImpl(Activity context, Integer id, Double maxSumm, Double minSumm ) {
        super(context);
        
        setOrientation( LinearLayout.VERTICAL );
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        this.id = id;
        this.maxSumm = maxSumm;
        this.minSumm = minSumm;
        
        this.fieldsLayout = new LinearLayout(context);
        this.fieldsLayout.setOrientation(LinearLayout.VERTICAL);
        this.fieldsLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        addView(fieldsLayout);
                
        this.value = new EditText(context);
        this.fullValue = new EditText(context);
        
        TextView valueTitle = new TextView(context);
        valueTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        valueTitle.setText("Сумма к зачислению:");
        
        addView(valueTitle);
        addView(value);
        
        TextView fullValueTitle = new TextView(context);
        fullValueTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        fullValueTitle.setText("Сумма с клиента:");
        
        addView(fullValueTitle);
        addView(fullValue);
    }
    
    
    public void addField(IField field) {
        fields.add(field);
        fieldsLayout.addView(field.getView());
    }
}
