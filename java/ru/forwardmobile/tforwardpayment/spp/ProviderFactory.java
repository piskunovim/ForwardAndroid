package ru.forwardmobile.tforwardpayment.spp;

import android.content.Context;
import android.database.Cursor;

import java.util.Arrays;
import java.util.Collection;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.impl.EnumFieldViewImpl;
import ru.forwardmobile.tforwardpayment.spp.impl.ProviderPojoImpl;
import ru.forwardmobile.tforwardpayment.spp.impl.TextFieldViewImpl;

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
                    new ProviderPojoImpl(psCursor.getString(0), id, psCursor.getDouble(1), psCursor.getDouble(2));
            
                fieldCursor = helper.getProviderFields(id);
                while(fieldCursor.moveToNext()) {
                    
                    IFieldView field = null;
                    int fieldType = fieldCursor.getInt(4);
                    switch( fieldType ) {
                        case IFieldView.TEXT_FIELD_TYPE:
                            field = new TextFieldViewImpl(ctx, fieldCursor.getString(0), fieldCursor.getString(2));
                            break;
                        case IFieldView.ENUMERATION_FIELD_TYPE:
                            field = new EnumFieldViewImpl(ctx, fieldCursor.getString(0), fieldCursor.getString(2), fieldCursor.getString(3));
                            break;
                        default:
                            field = new TextFieldViewImpl(ctx, fieldCursor.getString(0), fieldCursor.getString(2));
                            //throw new UnsupportedOperationException("Unsupported field type " + fieldType);
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
    
    // Провайдер для тестов
    public static IProvider mockProvider(final Context ctx) {
        return new IProvider() {
            
            public Integer getId() {
                return 1;
            }
            
            public Double getMaxSumm() {
                return 5000.00;
            }
            
            public Double getMinSumm() {
                return 10.00;
            }

            public String getName(){ return "test";}

            public boolean isGroup(){ return false;}

            public Collection<IFieldView> getFields() {
                return Arrays.asList(
                    (IFieldView) new TextFieldViewImpl(ctx, "target", "Номер телефона")
                );
            }
        };
    }
}
