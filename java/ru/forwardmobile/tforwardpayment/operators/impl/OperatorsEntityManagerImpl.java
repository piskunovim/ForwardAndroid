package ru.forwardmobile.tforwardpayment.operators.impl;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ru.forwardmobile.tforwardpayment.operators.IOperatorsDataSource;
import ru.forwardmobile.tforwardpayment.operators.IProvider;
import ru.forwardmobile.tforwardpayment.operators.OperatorsXmlReader;
import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;
import ru.forwardmobile.tforwardpayment.spp.impl.MenuItem;

/**
 * Created by Василий Ванин on 03.09.2014.
 */
public class OperatorsEntityManagerImpl implements OperatorsXmlReader.IOperatorsXmlListener, IOperatorsDataSource {

    private final HashMap<Integer, IProvider> providers
            = new HashMap<Integer, IProvider>();

    private final HashMap<Integer, IProviderMenuItem> itemsMap
            = new HashMap<Integer, IProviderMenuItem>();

    private final Stack<MenuGroupImpl> queue
            =  new Stack<MenuGroupImpl>();

    private final Collection<IProviderMenuItem> mainMenu
            = new ArrayList<IProviderMenuItem>();



    private MenuGroupImpl currentGroup = null;

    @Override
    public void onNewOperator(IProvider provider) {
        providers.put(provider.getId(), provider);
    }

    @Override
    public void onGroupStart(String name, Integer id) {

        MenuGroupImpl group = new MenuGroupImpl(name, id);
        if(currentGroup != null) {
            currentGroup.addItem(group);
            queue.push(currentGroup);
        }

        currentGroup = group;
    }

    @Override
    public void onGroupEnd() {
        itemsMap.put(currentGroup.getId(), currentGroup);
        if(queue.size() > 0) {
            currentGroup = queue.pop();
        } else {
            mainMenu.add(currentGroup);
            currentGroup = null;
        }
    }

    @Override
    public void onOperatorMenuItem(Integer id) {
        IProvider provider = providers.get(id);
        if(provider != null) {
            IProviderMenuItem item = MenuItem.getInstance(id, provider.getName());
            currentGroup.addItem(item);
        }
    }

    public Collection<IProviderMenuItem> getMainMenu() {
        return mainMenu;
    }

    @Override
    public List<IProviderMenuItem> getMenuItems(int parent) {

        MenuGroupImpl group = (MenuGroupImpl) itemsMap.get(new Integer(parent));
        return group.getItems();
    }

    @Override
    public List<IProviderMenuItem> getItemsByName(String name) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<IProviderMenuItem> getFullList() {
        return Collections.EMPTY_LIST;
    }

    private class MenuGroupImpl implements IProviderMenuItem {

        final String name;
        final Integer id;
        final List<IProviderMenuItem> items
                = new ArrayList<IProviderMenuItem>();

        public MenuGroupImpl(String name, Integer id) {
            this.name   = name;
            this.id     = id;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isGroup() {
            return true;
        }

        protected void addItem(IProviderMenuItem item) {
            items.add(item);
        }

        protected List<IProviderMenuItem> getItems() {
            return Collections.unmodifiableList(items);
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
