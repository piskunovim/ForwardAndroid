package ru.forwardmobile.tforwardpayment.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import ru.forwardmobile.tforwardpayment.operators.IValue;
import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 *
 * @author Vasiliy Vanin
 */
public abstract class FieldWidget extends LinearLayout {

     protected final IField field;

     public FieldWidget(IField field, Context ctx) {
         super(ctx);
         this.field = field;
     }

     public IField getField() {
         return field;
     };

     public abstract void   setValue(String value);
     public abstract IValue getValue();
}
