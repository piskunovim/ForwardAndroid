package ru.forwardmobile.tforwardpayment.messages;

import java.util.ArrayList;

/**
 * Created by PiskunovI on 03.12.2014.
 */
public class NotifyObject {
    String id;
    String data;
    String text;
}

class NotifyList{

    ArrayList<NotifyObject> items = new ArrayList<NotifyObject>();

    NotifyList(ArrayList<NotifyObject> result)
    {
        this.items = result;
    }
}
