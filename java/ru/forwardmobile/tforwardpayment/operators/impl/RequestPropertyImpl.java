package ru.forwardmobile.tforwardpayment.operators.impl;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import ru.forwardmobile.tforwardpayment.operators.IRequestProperty;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * @author Василий Ванин
 */
public class RequestPropertyImpl implements IRequestProperty {

    private String                              name;
    private String                              field;
    private Collection<IRequestPropertyItem>    items =
                        new ArrayList<IRequestPropertyItem>();

    public void setName(String name) {
        this.name = name;
    }

    public void setRefField(String field) {
        this.field = field;
    }

    public void setItems(Collection<IRequestPropertyItem> items) {
        this.items = items;
    }
    
    public void addItem(final String type, final String value) {
        items.add(new IRequestPropertyItem() {

            @Override
            public String getType() {
                return type;
            }

            @Override
            public String getValue() {
                return value;
            }
            
            @Override
            public String toString() {
                return type + "-" + value;
            }
        });
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRefField() {
        return field;
    }

    @Override
    public Collection<IRequestPropertyItem> getItems() {
        return items;
    }

    @Override
    public String toXml(IPayment payment) {
        
        // Если не содержит дополнительных свойств
        if( getItems().isEmpty() ) {

            IField paymentField = payment.getField(Integer.valueOf(field));
            return exportField(paymentField.getName(), paymentField.getValue().getValue());

        }   else {
            StringBuilder fields = new StringBuilder();
            Iterator<IRequestPropertyItem> iterator = getItems().iterator();
            String tmpFieldName  = null;

            while(iterator.hasNext()) {
                
                // Получаем свойство, и смотрим что с ним делать
                IRequestPropertyItem item = iterator.next();
                if(IRequestProperty.FIXED_TEXT_ITEM_TYPE . equals(item.getType())) {
                    
                    // Пара ключ-значение указывается через двоеточие
                    String[] typeValue = item.getValue().split("\\:");
                    
                    // Если пара полная, поле экспортируется сразу
                    if(typeValue.length == 2 && typeValue[1].length() > 0) {
                        fields.append( exportField(typeValue[0], typeValue[1]));
                    } else {
                        tmpFieldName = typeValue[0];
                    }

                } else if(IRequestProperty.FIELD_REF_ITEM_TYPE . equals(item.getType())) {
                    IField paymentField = payment.getField(Integer.valueOf(item.getValue()));
                    fields.append( exportField(tmpFieldName, paymentField.getValue().getValue()));
                }
            }
            
            return fields.toString();
        }
    }
    
    protected String exportField(final String name, final String value) {
        return "<f n=\"" + name + "\">" + value + "</f>";
    }
    
}
