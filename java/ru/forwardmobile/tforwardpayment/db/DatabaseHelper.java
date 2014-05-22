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

    private static final int        SQLITE_DATABASE_VERSION = 6;
    private static final String     SQLITE_DATABASE_NAME    = "forward";
    private static final String     SETTINGS_TABLE_NAME     = "settings";
    private static final String     PG_TABLE_NAME           = "pg";  
    private static final String     P_TABLE_NAME            = "p";  
    private static final String     F_TABLE_NAME            = "f"; 
	
    
    private final SQLiteDatabase db;
    private final SQLiteDatabase sdb;
    
    public DatabaseHelper(Context context) {
        super(context, SQLITE_DATABASE_NAME, null, SQLITE_DATABASE_VERSION);
        db  = getWritableDatabase();  
        sdb = getReadableDatabase();
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
        
        Cursor c = db.rawQuery("select `key`,`value` from " + SETTINGS_TABLE_NAME, new String[]{});
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
        db.rawQuery("REPLACE into " + SETTINGS_TABLE_NAME + " VALUES(?,?)",
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
				+ "(id integer primary key, fn text, fc text, fm text, fr text, ft text)");        
    }
    
    
    
}
