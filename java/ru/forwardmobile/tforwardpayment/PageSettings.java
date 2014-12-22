package ru.forwardmobile.tforwardpayment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.forwardmobile.tforwardpayment.operators.GetOperatorsXML;
import ru.forwardmobile.tforwardpayment.settings.GroupSettingsItems;
import ru.forwardmobile.tforwardpayment.settings.SettingsItems;

/**
 * Created by PiskunovI on 28.08.2014.
 */
public class PageSettings extends ActionBarActivity {

    final static String LOG_TAG = "TFORWARD.PageSettings";
    SettingsItems testBtn, testText, testEditable, operatorsBtn,passwordWidget;


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
        //Operators
        operatorsBtn = new SettingsItems(this);



        //Просто добавим какой-нибудь TextView
        testText.createTextView(this, "Настройки");
        someSetting.addItem(testText, viewGroup);


        // Для добавления поля ввода с заголовком используем createEditSettings,
        // но если заголовок не нужен, используем createEditText
        testEditable.createTextView(this, "Агент: Иванов Иван Иванович");
        someSetting.addItem(testEditable, viewGroup);

        testEditable.createTextView(this, "Номер точки: 1197");
        someSetting.addItem(testEditable, viewGroup);

        passwordWidget = new SettingsItems(this);
        passwordWidget.createEditSettings(this, "Пароль:", "******");
        passwordWidget.getEditText().setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordWidget.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        someSetting.addItem(passwordWidget, viewGroup);


        //Создадим кнопку, чтобы сохранить изменения
        testBtn.createButton(this,"Сохранить");
        someSetting.addItem(testBtn, viewGroup);

        operatorsBtn.createButton(this,"Обновить operators.xml");
        someSetting.addItem(operatorsBtn, viewGroup);



        testBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordWidget.getEditText().getText().toString();

                if(password == null || password.length() == 0) {
                    Toast.makeText(PageSettings.this, "Вы не ввели пароль!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if(password.contains("*")) {
                    Toast.makeText(PageSettings.this, "Пароль не изменен! Строка содержит недопустимые символы.", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                TSettings.setAuthenticationPass(password, PageSettings.this);
                Toast.makeText(PageSettings.this, "Пароль успешно изменен!", Toast.LENGTH_LONG).show();
            }
        });


        operatorsBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetOperatorsXML getOperators = new GetOperatorsXML(PageSettings.this);
                getOperators.execute();
            }
        });

        /*testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PageSettings.this, testEditable.getSetEditText(), Toast.LENGTH_LONG).show();
                   Log.d(LOG_TAG, "Наш пароль: " + testEditable.getSetEditText());
            }
        });*/


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