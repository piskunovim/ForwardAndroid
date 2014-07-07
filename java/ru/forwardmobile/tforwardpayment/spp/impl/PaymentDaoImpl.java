package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;

/**
 * Created by vaninv on 30.05.2014.
 */
public class PaymentDaoImpl implements IPaymentDao {

    private static final String LOGGER_TAG = "TFORWARD.DAO";
    private final SQLiteOpenHelper dbHelper;


    public PaymentDaoImpl(SQLiteOpenHelper dbHelper)    {
        this.dbHelper = dbHelper;
    }

    @Override
    public void save(IPayment payment) {

        // Набор полей
        StringBuilder payment_data = new StringBuilder();
        for( IFieldInfo field: payment.getFields() ) {
            payment_data.append("<f n=\"" + field.getName() + "\">" + field.getValue() + "</f>");
        }

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

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select psid, fields, value, fullValue, errorCode, errorDescription, startDate, status, processDate " +
                " from  payments where id = ?", new String[]{String.valueOf(id)});

        try {

            if(cursor.moveToNext()) {

                Collection<IFieldInfo> fields = parseFields(cursor.getString(1));
                IPayment payment = PaymentFactory.getPayment( cursor.getInt(0), (double) cursor.getInt(2)/100, (double) cursor.getInt(3)/100, fields );
                payment.setErrorCode(cursor.getInt(4));
                payment.setErrorDescription(cursor.getString(5));
                payment.setStartDate(new Date(cursor.getLong(6)));
                payment.setStatus(cursor.getInt(7));
                payment.setDateOfProcess(new Date(cursor.getLong(8)));

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

    /*@Override
    public IPayment getAll(){
        Cursor cursor = db.rawQuery("SELECT * FROM payments", null);
       // paymentgroup

    }*/

    public synchronized Collection<IPayment> getUnprocessed() {

        List<IPayment> collection = new ArrayList<IPayment>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(" select id, psid, transactid, fields, value, fullValue, errorCode, errorDescription, startDate, status, processDate from "
                + DatabaseHelper.PAYMENT_QUEUE_TABLE  + " where status not in(3,5) "
                , new String[]{});
        try {
            while (cursor.moveToNext()) {
                Collection<IFieldInfo> fields = parseFields(cursor.getString(3));
                IPayment payment = PaymentFactory.getPayment(cursor.getInt(2), (double) cursor.getInt(4) / 100, (double) cursor.getInt(5) / 100, fields);
                payment.setErrorCode(cursor.getInt(6));
                payment.setErrorDescription(cursor.getString(7));
                payment.setStartDate(new Date(cursor.getLong(8)));
                payment.setStatus(cursor.getInt(9));
                payment.setDateOfProcess(new Date(cursor.getLong(10)));

                payment.setId(cursor.getInt(0));
                payment.setTransactionId(cursor.getInt(1));

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

    public static Collection<IFieldInfo> parseFields(String data) {

        Collection<IFieldInfo> fields = new HashSet<IFieldInfo>();

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
        private String currentField = null;
        private final Collection<IFieldInfo> collection;

        public FieldContentHandler(Collection<IFieldInfo> collection) {
            this.collection = collection;
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if("f" . equals(localName)) {
                currentField = atts.getValue(0);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if("f" . equals(localName)) {
                collection.add(BaseField.fieldInfo(currentField, buffer.toString()));
                currentField = null;
                buffer = new StringBuffer();
            }
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
