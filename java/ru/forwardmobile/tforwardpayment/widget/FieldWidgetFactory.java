package ru.forwardmobile.tforwardpayment.widget;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 * Фабрика для Виджетов полей ввода
 * Created by Василий Ванин on 11.09.2014.
 */
public class FieldWidgetFactory {

    public static FieldWidget createWidget(IField field, Context ctx) {
        return new TextFieldWidget(field, ctx);
    }
}
