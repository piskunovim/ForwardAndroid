package ru.forwardmobile.tforwardpayment.spp.impl;

/**
 * Created by vaninv on 14.07.2014.
 */
public class GroupMenuItem extends MenuItem {

    private final Integer id;
    private final String name;

    public GroupMenuItem(Integer id, String name) {
        this.id     = id;
        this.name   = name;
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

    public String toString() {
        return super.toString();
    }
}
