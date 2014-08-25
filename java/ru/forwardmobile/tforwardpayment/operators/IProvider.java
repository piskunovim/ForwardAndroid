package ru.forwardmobile.tforwardpayment.operators;

import java.util.Collection;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;

/**
 * @author Василий Ванин
 */
public interface IProvider {
    
    /** @return int Идентификатор оператора в operators.xml */
    public Integer  getId();
    
    /** @return String Название провайдера, как оно отображается в чеках, меню и т.д. */
    public String   getName();
    
    /** @return Collection Поля платежа */
    public Collection<IFieldInfo> getFields();
    
    /** @return double Максимальновозможная сумма платежа */
    public Double   getMaxLimit();
    
    /** @return double Минимальновозможная сумма платежа */
    public Double   getMinLimit();

}
