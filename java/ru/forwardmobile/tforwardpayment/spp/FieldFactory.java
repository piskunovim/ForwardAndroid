package ru.forwardmobile.tforwardpayment.spp;

import ru.forwardmobile.tforwardpayment.operators.impl.FieldImpl;

/**
 * Created by Василий Ванин on 23.09.2014.
 */
public class FieldFactory {
    public  static  IField getField() {
        return new FieldImpl();
    }
}
