package ru.forwardmobile.tforwardpayment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import ru.forwardmobile.tforwardpayment.settings.GroupSettingsItems;
import ru.forwardmobile.tforwardpayment.settings.SettingsItems;

/**
 * Created by PiskunovI on 28.08.2014.
 */
public class PageSettings extends ActionBarActivity {

    final static String LOG_TAG = "TFORWARD.PageSettings";
    SettingsItems testBtn, testText, testEditable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Log.d(LOG_TAG, "Initialize PageSettings");

        //Наш основной LinearLayout
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.settingsLayout);

        //Инициализируем экземпляр для компановки содержимого
        GroupSettingsItems someSetting = new GroupSettingsItems(this);

        //TextView
        testText = new SettingsItems(this);
        //EditView
        testEditable = new SettingsItems(this);
        //Button
        testBtn = new SettingsItems(this);


        //Просто добавим какой-нибудь TextView
        testText.createTextView(this, "Настройки");
        someSetting.addItem(testText, viewGroup);


        // Для добавления поля ввода с заголовком используем createEditSettings,
        // но если заголовок не нужен, используем createEditText
        testEditable.createEditSettings(this, "Агент:","Иванов Иван Иванович");
        someSetting.addItem(testEditable, viewGroup);

        testEditable.createEditSettings(this, "Номер точки:","1197");
        someSetting.addItem(testEditable, viewGroup);

        testEditable.createEditSettings(this, "Пароль:","******");
        someSetting.addItem(testEditable, viewGroup);

        //Создадим кнопку, чтобы сохранить изменения
        testBtn.createButton(this,"Сохранить");
        someSetting.addItem(testBtn,viewGroup);


        // Изменяем шрифт (если требуется)
        // applyFonts( findViewById(R.id.some_id) ,null);

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