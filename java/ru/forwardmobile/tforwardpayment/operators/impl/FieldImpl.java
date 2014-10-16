package ru.forwardmobile.tforwardpayment.operators.impl;

import ru.forwardmobile.tforwardpayment.operators.IValue;
import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 * Created by Василий Ванин on 26.08.2014.
 */
public class FieldImpl implements IField {

    private Integer id;
    private String  type;
    private String  comment;
    private String  name;
    private String  value;
    private String  mask;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IValue getValue() {
        return new IValue() {
            @Override
            public String getDisplayValue() {
                return value;
            }

            @Override
            public String getValue() {
                return value;
            }
        };
    }
}
