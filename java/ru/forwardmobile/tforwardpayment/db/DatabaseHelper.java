package ru.forwardmobile.tforwardpayment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.forwardmobile.tforwardpayment.TSettings;

/**
 * @author Vasiliy Vanin, Piskunov Igor'
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String    LOGGER_TAG              = "TFORWARD.DBH";

    public static final int        SQLITE_DATABASE_VERSION = 8;
    public static final String     SQLITE_DATABASE_NAME    = "forward";
    public static final String     SETTINGS_TABLE_NAME     = "settings2";
    public static final String     DEALER_TABLE_NAME        = "dealer";
    public static final String     PG_TABLE_NAME           = "groups";
    public static final String     P_TABLE_NAME            = "providers";
    public static final String     F_TABLE_NAME            = "provider_fields";
    public static final String     PAYMENT_QUEUE_TABLE     = "payments";
    public static final String     MESSAGES_TABLE_NAME     = "messages";


    public DatabaseHelper(Context context) {
        super(context, SQLITE_DATABASE_NAME, null, SQLITE_DATABASE_VERSION);
    }

    public DatabaseHelper() {
        super(null, SQLITE_DATABASE_NAME, null, SQLITE_DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqld) {
                
        // Settings table
        sqld.execSQL("CREATE TABLE " + SETTINGS_TABLE_NAME 
                + " (property text primary key, value blob not null)");
		
	    initDatabaseV6(sqld);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        if(i > 5) {
            sqld.execSQL("CREATE TABLE " + SETTINGS_TABLE_NAME 
                + " (property text primary key, value blob not null)");
            
        } else if (i > 6 && i1 < 6) {
            initDatabaseV6(sqld);
        }
    }
        
    /**
     * Вызывается при запуске, чтобы заполнить объект настройками из базы
     */
    public void readSettings() {

        Cursor c = getWritableDatabase().rawQuery("select `property`,`value` from " + SETTINGS_TABLE_NAME, new String[]{});

        if(c.moveToNext()) {

            do {
                TSettings.set(c.getString(0), c.getString(1));
                Log.v(LOGGER_TAG, "Read " + c.getString(0) + " with " + c.getString(1));
            } while(c.moveToNext());
        } else {
            Log.v(LOGGER_TAG, "Settings query returned an empty cursor.");
        }
    }
    
    public void saveSettings() {
        for( Object key: TSettings.getKeys() ) {
            saveSettings(key.toString(), TSettings.get(key.toString()));
        }
    }
    
    public void saveSettings(String key, String value) {

        Log.v(LOGGER_TAG,"saving " + key + " with value " + value);
        ContentValues cv = new ContentValues();
        cv.put("property", key);
        cv.put("value", value);

        getWritableDatabase().replace(SETTINGS_TABLE_NAME, null, cv);
    }

    
    private void initDatabaseV6(SQLiteDatabase sqld) {
        //Operators group table 
	    sqld.execSQL("CREATE TABLE " + PG_TABLE_NAME
				+ "(id integer primary key, name text, parent integer)");
        
	    //Operators list group
	    sqld.execSQL("CREATE TABLE " + P_TABLE_NAME
				+ "(id integer primary key, gid integer, name text, min text, max text)");

	    //Operators data
        sqld.execSQL("CREATE TABLE " + F_TABLE_NAME	
				+ "(provider integer, name text, prefix text, title text, mask text, required text, type text)");

        sqld.execSQL("CREATE TABLE  " + PAYMENT_QUEUE_TABLE +
                "(" +
                    " id integer primary key autoincrement, " +
                    " psid integer, " +
                    " transactid integer," +
                    " fields text, " +
                    " value integer, " +
                    " fullValue integer, " +
                    " errorCode integer, " +
                    " errorDescription text, " +
                    " startDate text, " +
                    " status integer, " +
                    " processDate text " +
                ")"
        );

        //Agent messages table
        sqld.execSQL("CREATE TABLE " + MESSAGES_TABLE_NAME
                + "(id integer primary key, type integer, messageText text, regDate text)");

        sqld.execSQL("insert into " + MESSAGES_TABLE_NAME + " (id, type, messageText, regDate) values('11', '1', 'Я первое сообщение', '1111');");
        sqld.execSQL("insert into " + MESSAGES_TABLE_NAME + " (id, type, messageText, regDate) values('21', '1', 'Я второе сообщение', '2222');");
        sqld.execSQL("insert into " + MESSAGES_TABLE_NAME + " (id, type, messageText, regDate) values('31', '1', 'Я третье сообщение', '3333');");

        sqld.execSQL("CREATE TABLE " + DEALER_TABLE_NAME + " (id integer primary key, name text, balance text, may_expend text, credit text, retention_amount text, fee text, summ_fuftutres text, fio text, phone text, email text)");

    }

    public Cursor getProvider(Integer id) {
        return getReadableDatabase().rawQuery("select name, min, max from " + P_TABLE_NAME + " where id = ? ",
                new String[]{ String.valueOf(id) });
    }
    
    public Cursor getProviderFields(Integer id) {
        return getReadableDatabase().rawQuery("select name, prefix, title, mask, type from " + F_TABLE_NAME + " where provider = ? ", new String[]{
            String.valueOf(id)
        });
    }

    public Cursor getMessage() {
        return getReadableDatabase().rawQuery("select type, messageText, regDate from " + MESSAGES_TABLE_NAME , null);
    }

    public void setPushMessage(){
        Cursor c = getWritableDatabase().rawQuery("select `id`,`type`,`messageText`,`regDate` from " + SETTINGS_TABLE_NAME, new String[]{});

        if(c.moveToNext()) {

            do {
                TSettings.set(c.getString(0), c.getString(1));
                Log.v(LOGGER_TAG, "Read " + c.getString(0) + " with " + c.getString(1));
            } while(c.moveToNext());
        } else {
            Log.v(LOGGER_TAG, "Settings query returned an empty cursor.");
        }
    }
}
