package ru.forwardmobile.tforwardpayment.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Vasiliy Vanin
 * @deprecated use ru.forwardmobile.android.spp.db.DatabaseHelper
 */
public class KeyStorage extends SQLiteOpenHelper {

    private static final int    SQLITE_DATABASE_VERSION = 5;
    private static final String SQLITE_DATABASE_NAME    = "forward";
    private static final String SQLITE_TABLE_NAME       = "keys";
    
    private final SQLiteDatabase db;
    
    public KeyStorage(Context context) {
        super(context, SQLITE_DATABASE_NAME, null, SQLITE_DATABASE_VERSION);
        db = getWritableDatabase();
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqld) {
        Log.v("KEST", "Creating table");
        sqld.execSQL("CREATE TABLE " + SQLITE_TABLE_NAME
                + " (id int primary key, body blob not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        sqld.execSQL("CREATE TABLE " + SQLITE_TABLE_NAME
                + " (id int primary key, body blob not null)");        
    }
    
    public void saveKey(int type, byte[] data) {
        db.execSQL("REPLACE into " + SQLITE_TABLE_NAME + " values(?,?) ", new Object[]{
            type,
            data
        });
    }
    
    public byte[] getKey(int type) {
        Cursor cursor = db.rawQuery("select body from "
                + SQLITE_TABLE_NAME + " where id = ? ",
                new String[]{ String.valueOf(type) } );
        try {
            if(cursor.moveToFirst()) {
                return cursor.getBlob(0);
            }
        } finally {
            cursor.close();
        }        
        
        return null;
    }
            
}
