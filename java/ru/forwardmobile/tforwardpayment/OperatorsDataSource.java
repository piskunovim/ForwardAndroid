package ru.forwardmobile.tforwardpayment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;
import ru.forwardmobile.tforwardpayment.spp.impl.GroupMenuItem;
import ru.forwardmobile.tforwardpayment.spp.impl.MenuItem;

/**
 * Created by PiskunovI on 07.07.14.
 */
public class OperatorsDataSource {

    public static final String PK_FIELD             = "id";
    public static final String GROUP_ID_FIELD       = "id";
    public static final String P_NAME_FIELD         = "name";
    public static final String GROUP_NAME_FIELD     = "name";
    public static final String ITEM_TYPE_FIELD      = "type";
    public static final String GROUP_PARENT_FIELD   = "parent";
    public static final String MIN_FIELD            = "min";
    public static final String MAX_FIELD            = "max";
    public static final String P_GROUP_FIELD        = "gid";

    public static final String FLD_PROVIDER_FIELD   = "provider";
    public static final String FLD_NAME_FIELD       = "name";
    public static final String FLD_PREFIX_FIELD     = "prefix";
    public static final String FLD_TITLE_FIELD      = "title";
    public static final String FLD_MASK_FIELD       = "mask";
    public static final String FLD_REQUIRED_FIELD   = "required";
    public static final String FLD_TYPE_FIELD       = "type";

    private final SQLiteOpenHelper helper;
    private final SQLiteDatabase database;

    public OperatorsDataSource(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getReadableDatabase();
    }


    public List<IProviderMenuItem> getMainMenu() {
        throw new UnsupportedOperationException();
    }

    public List<IProviderMenuItem> getItemsByName(String name) {

        Cursor cursor = null;
        List<IProviderMenuItem> items = new ArrayList<IProviderMenuItem>();
        try {

            cursor = database.rawQuery(
                " select id, name from " + DatabaseHelper.P_TABLE_NAME + " where name like '%" + name + "%'",
                   null
            );

            while( cursor.moveToNext() ) {
                items.add(MenuItem.getInstance(
                    cursor.getInt(cursor.getColumnIndex(PK_FIELD)),
                    cursor.getString(cursor.getColumnIndex(P_NAME_FIELD))
                ));
            }

        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return items;
    }

    public List<IProviderMenuItem> getMenuItems(int parent) {

        Cursor cursor = null;
        List<IProviderMenuItem> items = new ArrayList<IProviderMenuItem>();

        try {

            cursor = database.rawQuery(
                " select g.id, g.name, 'group' as type, sum(c.cnt) as items_count from " + DatabaseHelper.PG_TABLE_NAME
                           + " as g left join ( "
                           + "    select gid , count(*) cnt from " + DatabaseHelper.P_TABLE_NAME + " group by gid "
                           + " union all "
                           + " select parent,  count(*) cnt from " + DatabaseHelper.PG_TABLE_NAME + " group by parent "
                + " ) as c on c.gid = g.id "
                + " where g.parent = ? group by g.id "
                + " union all "
                + " select id, name, 'provider', 0 from providers where gid = ?",
                    new String[]{
                        String.valueOf(parent),
                        String.valueOf(parent),
                    });

            while( cursor.moveToNext() ) {

                IProviderMenuItem item = null;

                String type = cursor.getString(cursor.getColumnIndex(ITEM_TYPE_FIELD));
                if( "group" . equals(type) ) {

                    int itemsCount = cursor.getInt(cursor.getColumnIndex("items_count"));

                    if(itemsCount > 0) {
                        item = new GroupMenuItem(
                                cursor.getInt(cursor.getColumnIndex(GROUP_ID_FIELD)),
                                cursor.getString(cursor.getColumnIndex(GROUP_NAME_FIELD))
                                        + " (" + itemsCount + ")"
                        );
                    } else {
                        continue;
                    }

                } else {

                    item = MenuItem.getInstance(
                            cursor.getInt(cursor.getColumnIndex(GROUP_ID_FIELD) ),
                            cursor.getString(cursor.getColumnIndex(P_NAME_FIELD))
                           );
                }

                items.add(item);
            }

        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return items;
    }

    public List<IProviderMenuItem> getFullList() {
        Cursor cursor = null;
        List<IProviderMenuItem> items = new ArrayList<IProviderMenuItem>();

        try {

            cursor = database.rawQuery(
                    " select id, name from " + DatabaseHelper.P_TABLE_NAME + " order by " + P_NAME_FIELD + " asc ",
                    null
            );

            while( cursor.moveToNext() ) {
                items.add(MenuItem.getInstance(
                        cursor.getInt(cursor.getColumnIndex(PK_FIELD)),
                        cursor.getString(cursor.getColumnIndex(P_NAME_FIELD))
                ));
            }

        } finally {
            if(cursor != null)
                cursor.close();
        }


        return items;
    }

    public void close() {
        database.close();
        helper.close();
    }
}