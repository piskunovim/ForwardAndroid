package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author vaninv
 */
public class EnumFieldViewImpl extends BaseFieldView {

    private final Spinner spinner;
    private ArrayAdapter<EnumItem> adapter;

    public EnumFieldViewImpl(Context ctx, String name, String label, String opts) {
        super(ctx, name, label);
        
        adapter = new ArrayAdapter<EnumItem>(ctx, android.R.layout.simple_spinner_item);
        
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

    @Override
    public void setValue(String value) {
        Integer index = adapter.getPosition(new EnumItem(new String[]{
            value,""
        }));
        spinner.setSelection(index);
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
        public boolean equals(Object o) {

            if(o instanceof EnumItem) {
                return o != null && o.toString().equals(this.toString());
            }
            return false;
        }
    }
}
