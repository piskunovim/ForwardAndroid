/*package ru.forwardmobile.tforwardpayment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.reports.PaymentInfo;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.impl.PaymentDaoImpl;

/**
 * Created by PiskunovI on 07.07.14.
 */
/*public class OperatorsDataSource {
    public static final String PK_FIELD             = "id";
    public static final String GROUP_ID_FIELD       = "gid";
    public static final String P_NAME_FIELD        = "name";
    public static final String MIN_FIELD         = "min";
    public static final String MAX_FIELD         = "max";

    private final SQLiteOpenHelper helper;
    private final SQLiteDatabase database;

    public OperatorsDataSource(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getReadableDatabase();
    }

    /**
     * Список платежей, которые находятся в состоянии проведения
     * @return List<PaymentInfo> - Список платежей
     */
  /*  public List<Operator> getUnprocessed() {

        List<Operator> items = new ArrayList<Operator>();
        Cursor cursor = null;

        try {

            cursor = database.rawQuery(
                    "select " +
                            "op.id, " +
                            "op.gid, " +
                            "op.name, " +
                            "op.min, " +
                            "op.max, " +
                            "p.name as psName" +
                            "from payments pay left join p on p.id = pay.psid " +
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


    private Operator getItem(Cursor cursor) {

        Operator item = new Operator(
                cursor.getInt( cursor.getColumnIndex(GROUP_ID_FIELD) ));

        // Платежная система (название)
        item.setPsName( cursor.getString( cursor.getColumnIndex(P_NAME_FIELD)) );

        // Поля
        Collection<IFieldInfo> fields = PaymentDaoImpl.parseFields(
                cursor.getString(cursor.getColumnIndex())
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
}*/