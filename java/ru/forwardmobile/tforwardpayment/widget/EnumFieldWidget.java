package ru.forwardmobile.tforwardpayment.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ru.forwardmobile.tforwardpayment.operators.IValue;
import ru.forwardmobile.tforwardpayment.operators.impl.EnumFieldImpl;
import ru.forwardmobile.tforwardpayment.spp.IField;

/**
 * Created by vaninv on 27.01.2015.
 */
public class EnumFieldWidget extends FieldWidget  {

    protected final ViewGroup.LayoutParams DEFAULT_PARAMS =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    protected final String[] values;
    protected final String[] labels;

    protected Spinner valueView;


    public EnumFieldWidget(IField field, Context ctx) {
        super(field, ctx);

        setLayoutParams(DEFAULT_PARAMS);
        setOrientation(VERTICAL);

        Map<String, String> collection = ((EnumFieldImpl) field).getValuesCollection();

        values = new String[collection.size()];
        labels = new String[collection.size()];

        int i = 0;
        for(String key: collection.keySet()) {
            Log.i("ENUMWIDGET", key + ":" + collection.get(key));
            values[i] = key;
            labels[i] = collection.get(key);
            i++;
        }

        createLabel();
        createValueView();
        createComment();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, labels);
                             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        valueView = new Spinner(getContext());
        valueView.setLayoutParams(DEFAULT_PARAMS);
        valueView.setAdapter(adapter);
        addView(valueView);
    }


    @Override
    public void setValue(String value) {
        int i = 0;
        int selection = -1;
        for(String key: values) {
            if(key.equals(value)) {
                selection = i;
                break;
            }
            i++;
        }
        if(selection >= 0) {
            valueView.setSelection(selection);
        }
    }

    @Override
    public IValue getValue() {

        int selection = valueView.getSelectedItemPosition();
        final String selectedKey = values[selection];
        final String selectedValue = labels[selection];

        return new IValue() {
            @Override
            public String getDisplayValue() {
                return selectedValue;
            }

            @Override
            public String getValue() {
                return selectedKey;
            }
        };
    }

}
