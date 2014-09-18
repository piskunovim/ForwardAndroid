package ru.forwardmobile.tforwardpayment.app;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.MainAccessActivity;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

/**
 * Вспомогательный класс для чтения и записи настроек
 * при загрузке/завершении приложения
 * Created by Василий Ванин on 18.09.2014.
 */
public class SettingsLoader implements MainAccessActivity.onLoadListener {

    @Override
    public void beforeApplicationStart(Context context) {

        // Данный метод просто читает настройки из базы
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.readSettings();
        dbHelper.close();
    }
}
