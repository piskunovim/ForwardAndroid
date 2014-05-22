package ru.forwardmobile.tforwardpayment.payment.impl;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author vaninv
 */
public class EnumFieldImpl extends BaseField {

    private final Spinner spinner;

    public EnumFieldImpl(Context ctx, String name, String label, String opts) {
        super(ctx, name, label);
        
        ArrayAdapter<EnumItem> adapter =
                new ArrayAdapter<EnumItem>(ctx, android.R.layout.simple_spinner_item);
        
        String[] items = opts.split("\\|");
        for(String pair: items) {
            String[] value = pair.split("\\=");
            if(value.length == 2) {
                adapter.add(new EnumItem(value));
            }
        }
        
        spinner = new Spinner(ctx);
        spinner.setAdapter(adapter);
        spinner.setLayoutParams(DEFAULT_LAYOUT_PARAMS);

        this.addView(spinner);
    }

    public String getValue() {
        EnumItem item = (EnumItem) spinner.getSelectedItem();
        return item.value;
    }

    static class EnumItem {
        private String value = null;
        private String title = null;
        
        public EnumItem(String[] item) {
            this.value = item[0];
            this.title = item[1].replaceAll("&quot;", "\"");
        }
        
        public String toString() {
            return title;
        }
    }


}
