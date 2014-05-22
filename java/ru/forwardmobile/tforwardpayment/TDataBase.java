package ru.forwardmobile.tforwardpayment;

/**
 * Created by PiskunovI on 08.05.14.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TDataBase extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pack3db";

    public TDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_PG_TABLE = "CREATE TABLE pg ( " +
                "id INTEGET PRIMARY KEY, " +
                "name TEXT)";
        String CREATE_P_TABLE = "CREATE TABLE p ( " +
                "id INTEGER PRIMARY KEY, " +
                "gid INTEGER," +
                "name TEXT, "+
                "min TEXT, "+
                "max TEXT)";

        String CREATE_F_TABLE = "CREATE TABLE f ( " +
                "id INTEGER PRIMARY KEY, " +
                "fn TEXT, "+
                "fc TEXT, "+
                //"fp TEXT, "+
                //"fm TEXT, "+
                "fr TEXT, "+
                "ft TEXT)";

        // create p table
        db.execSQL(CREATE_PG_TABLE);
        db.execSQL(CREATE_P_TABLE);
        db.execSQL(CREATE_F_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older p table if existed
        db.execSQL("DROP TABLE IF EXISTS pg");
        db.execSQL("DROP TABLE IF EXISTS p");
        db.execSQL("DROP TABLE IF EXISTS f");


        // create fresh books table
        this.onCreate(db);
    }

}


