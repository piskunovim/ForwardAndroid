package ru.forwardmobile.tforwardpayment.payment.impl;

import android.content.Context;
import android.widget.LinearLayout;
import java.util.Collection;
import java.util.HashSet;
import ru.forwardmobile.tforwardpayment.payment.IField;

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
    
    public ProviderImpl(Context context, Integer id, Double maxSumm, Double minSumm) {
        super(context);
        
        setOrientation( LinearLayout.VERTICAL );
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        this.id = id;
        this.maxSumm = maxSumm;
        this.minSumm = minSumm;
    }
    
    
    public void addField(IField field) {
        fields.add(field);
        addView(field.getView());
    }
}
