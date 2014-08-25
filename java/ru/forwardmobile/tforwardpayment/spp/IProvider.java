package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;

/**
 *
 * @author Vasiliy Vanin
 * @deprecated
 */
public interface IProvider extends IProviderMenuItem {
    public Double               getMaxSumm();
    public Double               getMinSumm();
    public Collection<IField>   getFields();
}
