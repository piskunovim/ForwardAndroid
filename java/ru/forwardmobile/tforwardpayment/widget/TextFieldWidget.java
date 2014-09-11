package ru.forwardmobile.tforwardpayment.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.operators.IValue;
import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 * Created by Василий Ванин on 11.09.2014.
 */
public class TextFieldWidget extends FieldWidget  {

    protected final ViewGroup.LayoutParams DEFAULT_PARAMS =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    protected EditText valueView = null;

    public TextFieldWidget(IField field, Context ctx) {
        super(field, ctx);

        setLayoutParams(DEFAULT_PARAMS);
        setOrientation(VERTICAL);

        createLabel();
        createValueView();
        createComment();
    }

    @Override
    public void setValue(String value) {
        valueView.setText(value);
    }

    protected void createLabel() {
        TextView labelView = new TextView(getContext());
                 labelView.setLayoutParams(DEFAULT_PARAMS);
                 labelView.setText(field.getName());

        addView(labelView);
    }

    protected void createComment() {
        TextView commentView = new TextView(getContext());
        commentView.setLayoutParams(DEFAULT_PARAMS);
        commentView.setText(field.getComment());
        commentView.setTextSize(10f);
        commentView.setPadding(10,0,0,0);

        addView(commentView);
    }

    protected void createValueView() {
        valueView = new EditText(getContext());
        valueView.setLayoutParams(DEFAULT_PARAMS);

        addView(valueView);
    }

    @Override
    public IValue getValue() {
        return new IValue() {
            @Override
            public String getDisplayValue() {
                return valueView.getText().toString();
            }

            @Override
            public String getValue() {
                return valueView.getText().toString();
            }
        };
    }
}
