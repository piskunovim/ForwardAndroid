package ru.forwardmobile.tforwardpayment.spp.impl;

import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;

/**
 * Created by vaninv on 14.07.2014.
 */
public abstract class MenuItem implements IProviderMenuItem {

    public static IProviderMenuItem getInstance(final int id, final String title) {
        return new IProviderMenuItem() {
            @Override
            public Integer getId() {
                return id;
            }

            @Override
            public String getName() {
                return title;
            }

            @Override
            public boolean isGroup() {
                return false;
            }

            @Override
            public String toString(){
                return getName();
            }
        };
    }

    public String toString(){
        return getName();
    }
}
