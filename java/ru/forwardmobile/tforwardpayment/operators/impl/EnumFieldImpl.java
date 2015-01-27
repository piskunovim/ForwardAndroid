package ru.forwardmobile.tforwardpayment.operators.impl;

import java.util.Map;

import ru.forwardmobile.tforwardpayment.operators.IValue;

/**
 * Created by Василий Ванин on 27.01.2015.
 */
public class EnumFieldImpl extends FieldImpl {

    private final Map<String, String> values;

    public EnumFieldImpl(Map<String, String> values) {
        this.values = values;
    }

    public IValue getValue() {

        return new IValue() {
            @Override
            public String getDisplayValue() {
                return values.get(value);
            }

            @Override
            public String getValue() {
                return value;
            }
        };
    }

    public Map<String, String> getValuesCollection() {
        return values;
    }
}
