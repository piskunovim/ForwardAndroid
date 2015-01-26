package ru.forwardmobile.tforwardpayment.widget;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.operators.IValue;
import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 * Created by PiskunovI on 26.01.2015.
 */
public class EnumFieldWidget extends FieldWidget {

    public EnumFieldWidget(IField field, Context ctx) {
        super(field, ctx);
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public IValue getValue() {
        return null;
    }
}
