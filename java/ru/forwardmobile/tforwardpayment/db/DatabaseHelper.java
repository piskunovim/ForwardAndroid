package ru.forwardmobile.tforwardpayment.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ru.forwardmobile.tforwardpayment.TSettings;

/**
 * @author Vasiliy Vanin
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int        SQLITE_DATABASE_VERSION = 6;
    public static final String     SQLITE_DATABASE_NAME    = "forward";
    public static final String     SETTINGS_TABLE_NAME     = "settings2";
    public static final String     PG_TABLE_NAME           = "pg";
    public static final String     P_TABLE_NAME            = "p";
    public static final String     F_TABLE_NAME            = "f";
	

    public DatabaseHelper(Context context) {
        super(context, SQLITE_DATABASE_NAME, null, SQLITE_DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqld) {
                
        // Settings table
        sqld.execSQL("CREATE TABLE " + SETTINGS_TABLE_NAME 
                + " (key text primary key, value blob not null)");
		
	    initDatabaseV6(sqld);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        if(i > 5) {
            sqld.execSQL("CREATE TABLE " + SETTINGS_TABLE_NAME 
                + " (key text primary key, value blob not null)");
            
        } else if (i > 6 && i1 < 6) {
            initDatabaseV6(sqld);
        }
    }
        
    /**
     * Вызывается при запуске, чтобы заполнить объект настройками из базы
     */
    public void readSettings() {
        
        TSettings.set(TSettings.SERVER_HOST, "www.forwardmobile.ru");
        TSettings.set(TSettings.SERVER_PORT, "8193");
        
        Cursor c = getReadableDatabase().rawQuery("select `key`,`value` from " + SETTINGS_TABLE_NAME, new String[]{});
        while(c.moveToNext()) {
            TSettings.set(c.getString(0), c.getString(1));
        }
    }
    
    public void saveSettings() {
        for( Object key: TSettings.getKeys() ) {
            saveSettings(key.toString(), TSettings.get(key.toString()));
        }        
    }
    
    public void saveSettings(String key, String value) {
        getWritableDatabase().rawQuery("REPLACE into " + SETTINGS_TABLE_NAME + " VALUES(?,?)",
                new String[]{ key, value });
    }

    
    private void initDatabaseV6(SQLiteDatabase sqld) {
        //Operators group table 
	    sqld.execSQL("CREATE TABLE " + PG_TABLE_NAME
				+ "(id integer primary key, name text)");
        
	    //Operators list group
	    sqld.execSQL("CREATE TABLE " + P_TABLE_NAME
				+ "(id integer primary key, gid integer, name text, min text, max text)");

	    //Operators data
        sqld.execSQL("CREATE TABLE " + F_TABLE_NAME	
				+ "(provider integer, name text, prefix text, title text, mask text, required text, type text)");        
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
}
