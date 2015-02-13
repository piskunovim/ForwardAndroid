package ru.forwardmobile.tforwardpayment.files;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.settings.TimeClass;

/**
 * Created by PiskunovI on 12.02.2015.
 */
 public class LogFile {

   static public void setToLog(Context context, String logMessage){

       SQLiteDatabase db = null;

       try {
           ContentValues cv = new ContentValues();

           DatabaseHelper dbHelper = new DatabaseHelper(context);

           // подключаемся к БД
           db = dbHelper.getWritableDatabase();

           Long time = new Date().getTime();

           cv.put("stamp", time);
           cv.put("message", logMessage);

           db.insert(DatabaseHelper.LOGS_TABLE_NAME, null, cv);
           cv.clear();
       }
       catch (Exception ex){
           ex.printStackTrace();
       }

        /* Вариант работы логирования в файловой системе
           FileOperationsImpl foi = null;


        try {
            foi = new FileOperationsImpl(context);
            foi.writeToFile(new TimeClass().getFullCurrentDateString() + " " + logMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }
}
