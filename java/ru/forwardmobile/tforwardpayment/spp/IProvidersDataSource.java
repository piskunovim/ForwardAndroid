package ru.forwardmobile.tforwardpayment.spp;

import java.util.List;

/**
 * Created by Василий Ванин on 04.09.2014.
 */
public interface IProvidersDataSource {
    public List<IProviderMenuItem> getMenuItems(int parent);
    public List<IProviderMenuItem> getItemsByName(String name);
    public List<IProviderMenuItem> getFullList();
    public IProvider               getById(Integer id);
}
