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

            @Override
            public int hashCode() {
                return id;
            }

            @Override
            public boolean equals(Object o) {
                return o != null && o.hashCode() == this.hashCode();
            }
        };
    }

    public String toString(){
        return getName();
    }
}
