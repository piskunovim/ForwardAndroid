package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;

import ru.forwardmobile.tforwardpayment.operators.IProcessor;

/**
 *
 * @author Vasiliy Vanin
 */
public interface IProvider  {

    /** @return int Идентификатор оператора в operators.xml */
    public Integer  getId();

    /** @return String Название провайдера, как оно отображается в чеках, меню и т.д. */
    public String   getName();

    /** @return Collection Поля платежа */
    public Collection<IField> getFields();

    /** @return double Максимальновозможная сумма платежа */
    public Double   getMaxLimit();

    /** @return double Минимальновозможная сумма платежа */
    public Double   getMinLimit();

    /** @return IProcessor Данные для обеспечения механизма проведения платежа */
    public IProcessor getProcessor();
}
