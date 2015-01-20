package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.FieldFactory;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.forwardmobile.tforwardpayment.spp.IProvidersDataSource;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;
import ru.forwardmobile.tforwardpayment.spp.ProvidersDataSourceFactory;

/**
 * Created by Василий Ванин on 30.05.2014.
 */
public class PaymentDaoImpl implements IPaymentDao {

    private static final String LOGGER_TAG = "TFORWARD.DAO";
    private final SQLiteOpenHelper dbHelper;
    private Context context;


    public PaymentDaoImpl(Context ctx) {
        context = ctx;
        dbHelper = new DatabaseHelper(ctx);
    }

    public PaymentDaoImpl(SQLiteOpenHelper dbHelper)    {
        this.dbHelper = dbHelper;
    }


    public Collection<IPayment> getPayments(Date startDate, Date finishDate){

        Log.d(LOGGER_TAG, String.valueOf(startDate.getTime()));
        Log.d(LOGGER_TAG, String.valueOf(finishDate.getTime()));

        List<IPayment> collection = new ArrayList<IPayment>();

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(" select id, psid, transactid, fields, value, fullValue, errorCode, errorDescription, startDate, status, processDate, pstitle from "
                + DatabaseHelper.PAYMENT_QUEUE_TABLE  + " where processDate >= " + startDate.getTime() + " and processDate < " + finishDate.getTime()
                , new String[]{});
        try {
            while (cursor.moveToNext()) {

                Collection<IField> fields = parseFields(cursor.getString(3));

                IPayment payment = PaymentFactory.getPayment(context);
                payment.setPsid(cursor.getInt(1));
                payment.setValue((double) cursor.getInt(4) / 100 );
                payment.setFullValue((double) cursor.getInt(5) / 100);
                payment.setFields(fields);
                payment.setErrorCode(cursor.getInt(6));
                payment.setErrorDescription(cursor.getString(7));
                payment.setStartDate(new Date(cursor.getLong(8)));
                payment.setStatus(cursor.getInt(9));
                payment.setDateOfProcess(new Date(cursor.getLong(10)));
                payment.setPsTitle(cursor.getString(11));

                payment.setId(cursor.getInt(0));
                payment.setTransactionId(cursor.getInt(2));

                /*
                IProvidersDataSource providersDataSource = ProvidersDataSourceFactory.getDataSource(context);
                IProvider provider = providersDataSource.getById(payment.getPsid());

                payment.setPsTitle(provider.getName());
                */

                Log.v(LOGGER_TAG, "Fetching payment id "
                        + payment.getId()
                        + ", StartDate " + payment.getStartDate()
                        + ", psid " + payment.getPsid()
                        + ", status " + payment.getStatus()
                        + ", processDate " + payment.getDateOfProcess());

                collection.add(payment);
            }
        } finally {
            cursor.close();
        }
        return collection;
    }

    public Collection<IPayment> getPayments(){
        Calendar tomorrowDate = Calendar.getInstance();
        Calendar todayDate = Calendar.getInstance();

        tomorrowDate.add(Calendar.DATE, 1);
        tomorrowDate.set(Calendar.HOUR, 0);
        tomorrowDate.set(Calendar.MINUTE, 0);
        tomorrowDate.set(Calendar.SECOND, 0);

        todayDate.set(Calendar.HOUR, 0);
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);

        return getPayments(todayDate.getTime(), tomorrowDate.getTime());
    }

    @Override
    public void save(IPayment payment) {

        // Набор полей
        // @todo проверить
        StringBuilder payment_data = new StringBuilder();
        for( IField field: payment.getFields() ) {
            Log.d(LOGGER_TAG, "Save value: " + field.getValue().getValue());
            payment_data.append("<f n=\"" + field.getId() + "\" t=\"" +  field.getName() + "\">" + field.getValue().getValue() + "</f>");
            Log.i(LOGGER_TAG, "Saving field " + field.getId() + ". Name " + field.getName() + " value: " + field.getValue().getValue());
        }

        // для получения psTitle обращаемся к provider
        IProvidersDataSource providersDataSource = ProvidersDataSourceFactory.getDataSource(context);
        IProvider provider = providersDataSource.getById(payment.getPsid());

        ContentValues cv = new ContentValues();

        cv.put("transactid", payment.getTransactionId());
        cv.put("psid", payment.getPsid());
        cv.put("fields", "<data>" + payment_data.toString() + "</data>");
        cv.put("value", payment.getValue() * 100);
        cv.put("fullValue", payment.getFullValue() * 100);
        cv.put("errorCode", payment.getErrorCode());
        cv.put("errorDescription", payment.getErrorDescription());
        cv.put("startDate", safeTimestamp( payment.getStartDate()));
        cv.put("status", payment.getStatus());
        cv.put("processDate", safeTimestamp(payment.getDateOfProcess()));
        cv.put("pstitle", provider.getName());

        if(payment.getId() == null) {
            Long rowId = dbHelper.getWritableDatabase().insert(DatabaseHelper.PAYMENT_QUEUE_TABLE, null, cv);
            payment.setId(rowId.intValue());
        } else {
            dbHelper.getWritableDatabase().update("payments", cv, " id = ? ", new String[]{
                    String.valueOf(payment.getId())
            });
        }
    }

    private Long safeTimestamp(Date dt) {
        if(dt == null) return null;
        return dt.getTime();
    }

    @Override
    public IPayment find(Integer id) {

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select psid, fields, value, fullValue, errorCode, errorDescription, startDate, status, processDate, transactid, pstitle " +
                " from  payments where id = ?", new String[]{String.valueOf(id)});

        try {

            if(cursor.moveToNext()) {

                IPayment payment = PaymentFactory.getPayment(context);
                payment.setPsid(cursor.getInt(0));
                payment.setValue( (double) cursor.getInt(2)/100 );
                payment.setFullValue( (double) cursor.getInt(3)/100 );
                payment.setErrorCode( cursor.getInt(4) );
                payment.setErrorDescription( cursor.getString(5) );
                payment.setStartDate( new Date(cursor.getLong(6)) );
                payment.setStatus(cursor.getInt(7));
                payment.setDateOfProcess(new Date(cursor.getLong(8)));
                payment.setFields(parseFields(cursor.getString(1)));
                payment.setTransactionId(cursor.getInt(9));
                payment.setPsTitle(cursor.getString(11));
                payment.setId(id);

                return payment;
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    public void close() {
        dbHelper.close();
    }

    public synchronized Collection<IPayment> getUnprocessed() {

        List<IPayment> collection = new ArrayList<IPayment>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(" select id, psid, transactid, fields, value, fullValue, errorCode, errorDescription, startDate, status, processDate, pstitle from "
                + DatabaseHelper.PAYMENT_QUEUE_TABLE  + " where status not in(" + IPayment.FAILED + ","+ IPayment.DONE +") "
                , new String[]{});
        try {
            while (cursor.moveToNext()) {

                Collection<IField> fields = parseFields(cursor.getString(3));

                IPayment payment = PaymentFactory.getPayment(context);
                payment.setPsid(cursor.getInt(1));
                payment.setValue((double) cursor.getInt(4) / 100 );
                payment.setFullValue((double) cursor.getInt(5) / 100);
                payment.setFields(fields);
                payment.setErrorCode(cursor.getInt(6));
                payment.setErrorDescription(cursor.getString(7));
                payment.setStartDate(new Date(cursor.getLong(8)));
                payment.setStatus(cursor.getInt(9));
                payment.setDateOfProcess(new Date(cursor.getLong(10)));
                payment.setPsTitle(cursor.getString(11));

                payment.setId(cursor.getInt(0));
                payment.setTransactionId(cursor.getInt(2));

                Log.v(LOGGER_TAG, "Fetching payment id "
                        + payment.getId()
                        + ", StartDate " + payment.getStartDate()
                        + ", psid " + payment.getPsid()
                        + ", status " + payment.getStatus()
                        + ", processDate " + payment.getDateOfProcess());

                collection.add(payment);
            }
        } finally {
            cursor.close();
        }
        return collection;
    }

    public synchronized Collection<IPayment> getFailed() {

        List<IPayment> collection = new ArrayList<IPayment>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(" select id, psid, transactid, fields, value, fullValue, errorCode, errorDescription, startDate, status, processDate, pstitle from "
                + DatabaseHelper.PAYMENT_QUEUE_TABLE  + " where status =" + IPayment.FAILED
                , new String[]{});
        try {
            while (cursor.moveToNext()) {

                Collection<IField> fields = parseFields(cursor.getString(3));

                IPayment payment = PaymentFactory.getPayment(context);
                payment.setPsid(cursor.getInt(1));
                payment.setValue((double) cursor.getInt(4) / 100 );
                payment.setFullValue((double) cursor.getInt(5) / 100);
                payment.setFields(fields);
                payment.setErrorCode(cursor.getInt(6));
                payment.setErrorDescription(cursor.getString(7));
                payment.setStartDate(new Date(cursor.getLong(8)));
                payment.setStatus(cursor.getInt(9));
                payment.setDateOfProcess(new Date(cursor.getLong(10)));
                payment.setPsTitle(cursor.getString(11));

                payment.setId(cursor.getInt(0));
                payment.setTransactionId(cursor.getInt(2));

                Log.v(LOGGER_TAG, "Fetching payment id "
                        + payment.getId()
                        + ", StartDate " + payment.getStartDate()
                        + ", psid " + payment.getPsid()
                        + ", status " + payment.getStatus()
                        + ", processDate " + payment.getDateOfProcess());

                collection.add(payment);
            }
        } finally {
            cursor.close();
        }
        return collection;
    }

    public synchronized Collection<IPayment> getDone() {

        List<IPayment> collection = new ArrayList<IPayment>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(" select id, psid, transactid, fields, value, fullValue, errorCode, errorDescription, startDate, status, processDate, pstitle from "
                + DatabaseHelper.PAYMENT_QUEUE_TABLE  + " where status =" + IPayment.DONE
                , new String[]{});
        try {
            while (cursor.moveToNext()) {

                Collection<IField> fields = parseFields(cursor.getString(3));

                IPayment payment = PaymentFactory.getPayment(context);
                payment.setPsid(cursor.getInt(1));
                payment.setValue((double) cursor.getInt(4) / 100 );
                payment.setFullValue((double) cursor.getInt(5) / 100);
                payment.setFields(fields);
                payment.setErrorCode(cursor.getInt(6));
                payment.setErrorDescription(cursor.getString(7));
                payment.setStartDate(new Date(cursor.getLong(8)));
                payment.setStatus(cursor.getInt(9));
                payment.setDateOfProcess(new Date(cursor.getLong(10)));
                payment.setPsTitle(cursor.getString(11));

                payment.setId(cursor.getInt(0));
                payment.setTransactionId(cursor.getInt(2));

                Log.v(LOGGER_TAG, "Fetching payment id "
                        + payment.getId()
                        + ", StartDate " + payment.getStartDate()
                        + ", psid " + payment.getPsid()
                        + ", status " + payment.getStatus()
                        + ", processDate " + payment.getDateOfProcess());

                collection.add(payment);
            }
        } finally {
            cursor.close();
        }
        return collection;
    }

    @Override
    public IPayment findByTransaction(Integer transactid) {
        return null;
    }

    @Override
    public Collection<IPayment> getCollection() {
        return null;
    }

    @Override
    public void delete(IPayment payment) {

    }

    public static Collection<IField> parseFields(String data) {
        Log.i(LOGGER_TAG, data);
        Collection<IField> fields = new HashSet<IField>();

        try {
            Xml.parse(data, new FieldContentHandler(fields));
        } catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        return fields;
    }



    private static class FieldContentHandler implements ContentHandler {

        private Locator locator = null;
        private StringBuffer buffer = new StringBuffer();
        private final Collection<IField> collection;
        private IField field = null;

        public FieldContentHandler(Collection<IField> collection) {
            this.collection = collection;
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if("f" . equals(localName)) {

                field = FieldFactory.getField();

                field.setId(Integer.parseInt(atts.getValue(0)));
                field.setName(atts.getValue(1));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if("f" . equals(localName)) {
                Log.d(LOGGER_TAG+" buffer: ", buffer.toString());
                field.setValue(buffer.toString());
                collection.add(field);
            }

            buffer = new StringBuffer();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            buffer.append(ch, start, length);
        }

        @Override
        public void startDocument() throws SAXException {}
        @Override
        public void endDocument() throws SAXException {}
        @Override
        public void startPrefixMapping(String s, String s2) throws SAXException {}
        @Override
        public void endPrefixMapping(String s) throws SAXException {}
        @Override
        public void ignorableWhitespace(char[] chars, int i, int i2) throws SAXException {}
        @Override
        public void processingInstruction(String s, String s2) throws SAXException {}
        @Override
        public void skippedEntity(String s) throws SAXException {}
    }
}
