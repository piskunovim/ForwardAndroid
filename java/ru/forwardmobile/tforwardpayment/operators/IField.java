package ru.forwardmobile.tforwardpayment.operators;

/**
 * Поле провайдера
 * Created by Василий Ванин on 26.08.2014.
 */
public interface IField {
    public Integer      getId();
    public String       getType();
    public String       getComment();
    public String       getName();
    public IValue       getValue();
}
