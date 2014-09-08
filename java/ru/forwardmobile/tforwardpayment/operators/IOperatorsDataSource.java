package ru.forwardmobile.tforwardpayment.operators;

import java.util.List;

import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;

/**
 * Created by Василий Ванин on 04.09.2014.
 */
public interface IOperatorsDataSource {
    public List<IProviderMenuItem> getMenuItems(int parent);
    public List<IProviderMenuItem> getItemsByName(String name);
    public List<IProviderMenuItem> getFullList();
}
