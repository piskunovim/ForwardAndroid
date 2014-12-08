package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by PiskunovI on 03.12.2014.
 */
public class PushNotifyActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
    }

    public class NotifyArrayAdapter extends ArrayAdapter<String> {


        TextView itemData = (TextView) findViewById(R.id.itemData);
        TextView itemText = (TextView) findViewById(R.id.itemText);

        private final Activity context;
        private final String[] itemsData;
        private final String[] itemsText;

        public NotifyArrayAdapter(Activity context, String[] data, String[] text) {
            super(context, R.layout.push_list_item, text);
            this.context = context;
            this.itemsData = data;
            this.itemsText = text;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.push_list_item, null, true);
            itemData.setText("Дата: " + itemsData[position]);
            itemText.setText("Сообщение: " + itemsText[position]);
            return rowView;
        }
    }

}


