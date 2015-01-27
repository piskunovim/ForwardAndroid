package ru.forwardmobile.tforwardpayment.spp;

import java.util.Map;

import ru.forwardmobile.tforwardpayment.operators.impl.EnumFieldImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.FieldImpl;

/**
 * Created by Василий Ванин on 23.09.2014.
 */
public class FieldFactory {
    public  static  IField getField(String type, String comment, String mask, Map<String, String> values) {

        if( IField.TYPE_ENUM . equals(type) && values != null) {
            IField field = new EnumFieldImpl(values);
            field.setComment(comment);
            field.setType(type);
            return field;
        }
        // else
        // if( IField.TYPE_INTEGER . equals(type) { Реализация для Integer }
        else {

            return new FieldImpl(mask,comment);
        }
    }
}
