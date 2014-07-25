package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Created by vaninv on 04.07.2014.
 */
public class PaymentListDataSource {

    public static final String PK_FIELD             = "id";
    public static final String PS_ID_FIELD          = "psid";
    public static final String PS_NAME_FIELD        = "psName";
    public static final String FIELDS_FIELD         = "fields";
    public static final String VALUE_FIELD          = "value";
    public static final String FULL_VALUE_FIELD     = "fullValue";
    public static final String ERROR_CODE_FIELD     = "errorCode";
    public static final String ERROR_DESCR_FIELD    = "errorDescription";
    public static final String START_DATE_FIELD     = "startDate";
    public static final String STATUS_FIELD         = "status";
    public static final String PROCESS_DATE_FIELD   = "processDate";
    public static final String TRANSACTION_FIELD    = "transactid";

    private final SQLiteOpenHelper helper;
    private final SQLiteDatabase database;

    public PaymentListDataSource(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getReadableDatabase();
    }

    public List<PaymentInfo> getAll() {
        List<PaymentInfo> items = new ArrayList<PaymentInfo>();
        Cursor cursor = null;

        try {

            cursor = database.rawQuery(
                    "select " +
                            "pay.id, " +
                            "pay.psid, " +
                            "pay.fields, " +
                            "pay.value, " +
                            "pay.fullValue, " +
                            "pay.errorCode, " +
                            "pay.errorDescription, " +
                            "pay.startDate, " +
                            "pay.status, " +
                            "pay.processDate, " +
                            "p.name as psName " +
                            " from payments pay left join " + DatabaseHelper.P_TABLE_NAME + " p on p.id = pay.psid ",
                    new String[]{});

            while( cursor.moveToNext() )
                items.add(getItem(cursor));

        } finally {
            if(cursor != null)
                cursor.close();
        }

        return items;
    }

    /**
     * Список платежей, которые находятся в состоянии проведения
     * @return List<PaymentInfo> - Список платежей
     */
    public List<PaymentInfo> getUnprocessed() {

        List<PaymentInfo> items = new ArrayList<PaymentInfo>();
        Cursor cursor = null;

        try {

            cursor = database.rawQuery(
                "select " +
                    "pay.id, " +
                    "pay.psid, " +
                    "pay.fields, " +
                    "pay.value, " +
                    "pay.fullValue, " +
                    "pay.errorCode, " +
                    "pay.errorDescription, " +
                    "pay.startDate, " +
                    "pay.status, " +
                    "pay.processDate, " +
                    "p.name as psName " +
                " from payments pay left join " + DatabaseHelper.P_TABLE_NAME + " p on p.id = pay.psid " +
                " where pay.status not in(3,5) ",
            new String[]{});

            while( cursor.moveToNext() )
                items.add(getItem(cursor));

        } finally {
            if(cursor != null)
                cursor.close();
        }

        return items;
    }


    private PaymentInfo getItem(Cursor cursor) {

        Log.i("GETITEM", "PSID " + cursor.getInt( cursor.getColumnIndex(PS_ID_FIELD) ));
        Log.i("GETITEM", "PSNAME " +  cursor.getString( cursor.getColumnIndex(PS_NAME_FIELD)));

        PaymentInfo item = new PaymentInfo(
            cursor.getInt( cursor.getColumnIndex(PS_ID_FIELD) ),
            ((double) cursor.getInt( cursor.getColumnIndex(VALUE_FIELD) ) / 100),
            ((double) cursor.getInt( cursor.getColumnIndex(FULL_VALUE_FIELD) ) / 100)
        );

        // Платежная система (название)
        item.setPsName( cursor.getString( cursor.getColumnIndex(PS_NAME_FIELD)) );

        // Поля
        Collection<IFieldInfo> fields = PaymentDaoImpl.parseFields(
                        cursor.getString(cursor.getColumnIndex(FIELDS_FIELD))
                    );
        for(IFieldInfo field: fields) item.addField(field);

        // Дата создания платежа
        item.setStartDate(new Date( cursor.getLong( cursor.getColumnIndex(START_DATE_FIELD) ) ));
        // Дата завершения платежа
        item.setDateOfProcess(new Date( cursor.getLong(cursor.getColumnIndex(PROCESS_DATE_FIELD)) ));
        // Код ошибки
        item.setErrorCode( cursor.getInt( cursor.getColumnIndex(ERROR_CODE_FIELD) ) );
        // Описание ошибки
        item.setErrorDescription( cursor.getString(cursor.getColumnIndex(ERROR_DESCR_FIELD)) );

        // Статус платежа
        item.setStatus( cursor.getInt( cursor.getColumnIndex(STATUS_FIELD) ) );

        // Идентификатор платежа
        item.setId( cursor.getInt( cursor.getColumnIndex(PK_FIELD) ) );

        // Идентификатор тразакции
        if(cursor.getColumnIndex(TRANSACTION_FIELD) != -1) {
            item.setTransactionId( cursor.getInt( cursor.getColumnIndex(TRANSACTION_FIELD) ) );
        }

        return item;
    }


    public void close() {
        database.close();
        helper.close();
    }
}
