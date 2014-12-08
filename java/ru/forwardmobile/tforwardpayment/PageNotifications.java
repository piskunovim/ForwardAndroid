package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.messages.MessageParser;

/**
 * Created by PiskunovI on 28.08.2014.
 */
public class PageNotifications  extends ActionBarActivity {

    final static String LOG_TAG = "TFORWARD.PageNotifications";

    ArrayList<String> arrayListDate = new ArrayList<String>();
    ArrayList<String> arrayListMessages = new ArrayList<String>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notify);

        Log.d(LOG_TAG, "Initialize PageNotifications");

        listView = (ListView)findViewById(R.id.listView);

        MessageParser messageParser = new MessageParser(this);
        messageParser.execute();

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Cursor c = db.query(dbHelper.MESSAGES_TABLE_NAME, null, null, null, null, null, null);
        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.MESSAGES_TABLE_NAME + " ORDER BY regDate DESC", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int typeIndex = c.getColumnIndex("type");
            int dateColIndex = c.getColumnIndex("regDate");
            int messageColIndex = c.getColumnIndex("messageText");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                if (c.getString(typeIndex).equals("2")) {
                    arrayListDate.add(c.getString(dateColIndex)); //what a hell -_-
                    arrayListMessages.add(c.getString(messageColIndex));
                    Log.d(LOG_TAG,
                            "date = " + c.getString(dateColIndex) +
                                    ", message = " + c.getString(messageColIndex));
                }
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        Log.d(LOG_TAG, "arrayListDate.length = " + arrayListDate.size() +"; arrayListMessages = " + arrayListMessages.size());
        NotifyArrayAdapter adapter = new NotifyArrayAdapter(PageNotifications.this, arrayListDate.toArray(new String[arrayListDate.size()]), arrayListMessages.toArray(new String[arrayListMessages.size()]));

        listView.setAdapter(adapter);

        applyFonts( findViewById(R.id.notifyActivityContainer) ,null);

    }

    public class NotifyArrayAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] itemsDate;
        private final String[] itemsText;

        public NotifyArrayAdapter(Activity context, String[] date, String[] text) {
            super(context, R.layout.push_list_item, text);
            this.context = context;
            this.itemsDate = date;
            this.itemsText = text;
        }

        @Override
        public View getView(int position, View rowView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            if(rowView == null) {
                rowView = inflater.inflate(R.layout.push_list_item, null, true);
            }

            //Log.d(LOG_TAG, "Дата: " + itemsDate[position]);
            //Log.d(LOG_TAG, "Сообщение: " + itemsText[position]);

            TextView itemDate = (TextView) rowView.findViewById(R.id.itemData);
            TextView itemText = (TextView) rowView.findViewById(R.id.itemText);

            itemDate.setText("Дата: " + itemsDate[position]);
            itemText.setText("Сообщение: " + itemsText[position]);
            return rowView;
        }
    }

    protected  void applyFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "meVe0se2.ttf");

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFonts(child, fontToSet);
                }
            } else if (v instanceof TextView) {
                ((TextView)v).setTypeface(fontToSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

}