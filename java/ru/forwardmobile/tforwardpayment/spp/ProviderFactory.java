package ru.forwardmobile.tforwardpayment.spp;

import android.content.Context;
import android.database.Cursor;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.impl.EnumFieldImpl;
import ru.forwardmobile.tforwardpayment.spp.impl.ProviderPojoImpl;
import ru.forwardmobile.tforwardpayment.spp.impl.TextFieldImpl;

/**
 * Фабрика для создания операторов 
 * @author Vasiliy Vanin
 */
public class ProviderFactory {
    
    public static IProvider getProvider(Integer id, Context ctx) {
        
        DatabaseHelper helper = new DatabaseHelper(ctx);
        Cursor  psCursor = null;
        Cursor  fieldCursor = null;
        
        try {
            psCursor =  helper.getProvider(id);
            if(psCursor.moveToFirst()) {
        
                ProviderPojoImpl provider =
                    new ProviderPojoImpl(psCursor.getString(1), id, psCursor.getDouble(2), psCursor.getDouble(3));
            
                fieldCursor = helper.getProviderFields(id);
                while(fieldCursor.moveToNext()) {
                    
                    IField field = null;
                    int fieldType = fieldCursor.getInt(4);
                    
                    switch( fieldType ) {
                        case IField.TEXT_FIELD_TYPE: 
                            field = new TextFieldImpl(ctx, fieldCursor.getString(0), fieldCursor.getString(2));
                            break;
                        case IField.ENUMERATION_FIELD_TYPE: 
                            field = new EnumFieldImpl(ctx, fieldCursor.getString(0), fieldCursor.getString(2), fieldCursor.getString(3));
                            break;
                        default:
                            throw new UnsupportedOperationException("Unsupported field type " + fieldType);
                    }
                    
                    provider.addField(field);
                }            
                
                return provider;
            }
        } finally {
            if(null != fieldCursor) fieldCursor.close();
            if(null != psCursor) psCursor.close();
            helper.close();
        }
        
        // Провайдер не найден
        return null;
    }
}
