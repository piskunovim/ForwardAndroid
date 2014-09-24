package ru.forwardmobile.tforwardpayment.spp;

import ru.forwardmobile.tforwardpayment.operators.IValue;

/**
 * Поле провайдера
 * Created by Василий Ванин on 26.08.2014.
 */
public interface IField {

    public static final String TYPE_MASKED = "masked";

    public Integer      getId();
    public String       getType();
    public String       getComment();
    public String       getName();
    public IValue       getValue();
    public void         setValue(String value);
    public void         setId(Integer id);
    public void         setName(String name);
}
