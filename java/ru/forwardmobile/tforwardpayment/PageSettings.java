package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ru.forwardmobile.tforwardpayment.actions.UpdateApplicationFromLocalRepo;
import ru.forwardmobile.tforwardpayment.actions.UpdateCheckTask;
import ru.forwardmobile.tforwardpayment.files.FileOperationsImpl;
import ru.forwardmobile.tforwardpayment.operators.GetOperatorsXML;
import ru.forwardmobile.tforwardpayment.operators.OperatorsEntityManagerFactory;
import ru.forwardmobile.tforwardpayment.settings.GroupSettingsItems;
import ru.forwardmobile.tforwardpayment.settings.SettingsItems;
import ru.forwardmobile.tforwardpayment.settings.TimeClass;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Created by PiskunovI on 28.08.2014.
 */
public class PageSettings extends ActionBarActivity implements View.OnClickListener {

    final static String LOG_TAG = "TFORWARD.PageSettings";
    SettingsItems testBtn, testText, testEditable, operatorsBtn,passwordWidget,updateCheckBtn,testEditablePhone,sendLogBtn;


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
        //Version
        updateCheckBtn = new SettingsItems(this);
        //Send Log File
        sendLogBtn = new SettingsItems(this);
        //EditView
        //testEditablePhone = new SettingsItems(this);


        //Просто добавим какой-нибудь TextView
        testText.createTextView(this, "Настройки");
        someSetting.addItem(testText, viewGroup);


        // Для добавления поля ввода с заголовком используем createEditSettings,
        // но если заголовок не нужен, используем createEditText
        testEditable.createTextView(this, "Агент: " + Settings.get(this, Settings.DEALERS_NAME));
        someSetting.addItem(testEditable, viewGroup);

        testEditable.createTextView(this, "Номер точки: " + Settings.get(this, Settings.POINT_ID));
        someSetting.addItem(testEditable, viewGroup);

        passwordWidget = new SettingsItems(this);
        passwordWidget.createEditSettings(this, "Пароль:", "******");
        passwordWidget.getEditText().setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordWidget.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        someSetting.addItem(passwordWidget, viewGroup);

        testEditablePhone = new SettingsItems(this);
        testEditablePhone.createEditPhone(this);
        someSetting.addItem(testEditablePhone,viewGroup);

        //Создадим кнопку, чтобы сохранить изменения
        testBtn.createButton(this,"Сохранить");
        someSetting.addItem(testBtn, viewGroup);

        operatorsBtn.createButton(this,"Обновить operators.xml");
        someSetting.addItem(operatorsBtn, viewGroup);

        updateCheckBtn.createButton(this,"Проверить обновление");
        someSetting.addItem(updateCheckBtn, viewGroup);
        updateCheckBtn.getButton()
                .setOnClickListener(this);

        sendLogBtn.createButton(this,"Отправить Log");
        someSetting.addItem(sendLogBtn,viewGroup);
        sendLogBtn.getButton().setOnClickListener(this);


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

                Settings.setAuthenticationPass(password, PageSettings.this);
                Toast.makeText(PageSettings.this, "Пароль успешно изменен!", Toast.LENGTH_LONG).show();
            }
        });


        operatorsBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Загрузка operators.xml
                GetOperatorsXML getOperators = new GetOperatorsXML(PageSettings.this, new ITaskListener() {
                    @Override
                    public void onTaskFinish(Object result) {
                        Integer status = (Integer) result;

                        if (status == 1)
                        {
                            OperatorsEntityManagerFactory.getManager(PageSettings.this, true);
                            Toast.makeText(PageSettings.this,"Operators.xml загружен успешно!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(PageSettings.this,"Ошибка загрузки operators.xml", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                getOperators.execute();

                //GetOperatorsXML getOperators = new GetOperatorsXML(PageSettings.this);
                //getOperators.execute();
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


    @Override
    public void onClick(View view) {
        if(updateCheckBtn.getButton().equals(view)) {
            checkForUpdate();
        }
        else if(sendLogBtn.getButton().equals(view)){
            sendLogFile();
        }
    }

    private void checkForUpdate() {
        AsyncTask checkTask = new UpdateCheckTask(this, new ITaskListener() {
            @Override
            public void onTaskFinish(Object result) {
                // Ошибка запроса
                if(result == null) {
                    Toast.makeText(PageSettings.this, "Ошибка запроса актуальной версии, пожалуйста, повторите запрос позже.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Map info = (Map) result;
                Integer code = Integer.valueOf((String) info.get("version"));

                AlertDialog.Builder builder = new AlertDialog.Builder(PageSettings.this);
                if(code > Settings.VERSION_CODE) {
                    builder.setTitle("Доступно обновление!");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            PageSettings.this,
                            android.R.layout.select_dialog_item);

                    arrayAdapter.add("Обновить с ForwardMobile");
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            updateFromLocalRepository();
                        }
                    });
                    builder.setPositiveButton("Пропустить", null);
                } else {
                    builder.setTitle("Обновление не требуется");
                    builder.setMessage("Установлена актуальная версия приложения.");
                    builder.setPositiveButton("Ок", null);
                }

                builder.show();
            }
        });

        checkTask.execute();
    }

    private void updateFromLocalRepository() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Обратите внимание!");
        builder.setMessage("На ваше устройство будет загружена новая версия приложения для приема платежей. " +
                "По окончании загрузки запустится мастер установки. Ваши данные не будут затронуты при обновлении.");
        builder.setPositiveButton("Хорошо", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                AsyncTask task = new UpdateApplicationFromLocalRepo(PageSettings.this, new ITaskListener() {
                    @Override
                    public void onTaskFinish(Object result) {
                        Integer statusCode = (Integer) result;
                        if(statusCode == 1) {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(getFilesDir() + "/" +  "app.apk")),
                                    "application/vnd.android.package-archive");
                            startActivity(intent);

                        } else {
                            Toast.makeText(PageSettings.this, "При обновлении произошла ошибка. Попробуйте позже", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });

                task.execute();
            }
        });

        builder.setNegativeButton("Не сейчас", null);
        builder.show();
    }

    private void sendLogFile(){
        Log.d(LOG_TAG, "Sending " + new TimeClass().getCurrentDateString() + ".txt log-file.");

        try {
            FileOperationsImpl foi = new FileOperationsImpl(this);
            foi.sendFile(new TimeClass().getCurrentDateString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}