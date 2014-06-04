package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;

/**
 * Created by vaninv on 30.05.2014.
 */
public class PaymentDaoImpl implements IPaymentDao {

    private final SQLiteDatabase db;

    public PaymentDaoImpl(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void save(IPayment payment) {



        // Набор полей
        StringBuilder payment_data = new StringBuilder();
        for( IFieldInfo field: payment.getFields() ) {
            payment_data.append("<f n=\"" + field.getName() + "\">" + field.getValue() + "</f>");
        }

        if(payment.getId() == null) {
            ContentValues cv = new ContentValues();

            cv.put("transactid", payment.getTransactionId());
            cv.put("status", payment.getStatus());
            cv.put("target", payment.getTarget().getValue());
            cv.put("payment_data", "<data>" + payment_data.toString() + "</data>");
            cv.put("started", payment.getStartDate().getTime());
            cv.put("finished", payment.getFinishDate() != null ? payment.getFinishDate().getTime() : null );
            cv.put("psid", payment.getPsid());
            cv.put("value", payment.getValue() * 100);
            cv.put("full_value", payment.getFullValue() * 100);
            cv.put("error_code", payment.getErrorCode());

            Long rowId = db.insert(DatabaseHelper.PAYMENT_QUEUE_TABLE, null, cv);
            payment.setId(rowId.intValue());

        } else {

            db.execSQL("REPLACE into " + DatabaseHelper.PAYMENT_QUEUE_TABLE + " VALUES(?,?,?,?,?,?,?,?,?,?,?)", new String[]{
                    String.valueOf(payment.getId()),
                    String.valueOf(payment.getTransactionId()),
                    String.valueOf(payment.getStatus()),
                    payment.getTarget().getValue(),
                    "<data>" + payment_data.toString() + "</data>",
                    String.valueOf(payment.getStartDate().getTime()),
                    String.valueOf(payment.getFinishDate() != null ? payment.getFinishDate().getTime() : null),
                    String.valueOf(payment.getPsid()),
                    String.valueOf(payment.getValue() * 100),
                    String.valueOf(payment.getFullValue() * 100),
                    String.valueOf(payment.getErrorCode())
            });
        }

    }

    @Override
    public IPayment find(Integer id) {

        Cursor cursor = db.rawQuery("select transactid, status, payment_data, started, finished, psid, value, error_code  from "
                + DatabaseHelper.PAYMENT_QUEUE_TABLE + " where id = ?", new String[]{
                    String.valueOf(id)
        });

        try {
            if (cursor.moveToNext()) {

                Collection<IFieldInfo> fields = parseFields(cursor.getString(2));
                IPayment payment = PaymentFactory.getPayment(cursor.getInt(5), Double.valueOf(cursor.getInt(6) / 100), fields);
                payment.setStartDate(new Date(Long.valueOf(cursor.getLong(3))));
                payment.setFinishDate(cursor.getLong(4) > 0 ? new Date(cursor.getLong(4)) : null);
                payment.setStatus(cursor.getInt(1));
                payment.setErrorCode(cursor.getInt(7));
                payment.setTransactionId(cursor.getInt(0));

                return payment;
            }
        }finally {
             cursor.close();
        }

        return null;
    }

    @Override
    public IPayment findByTransaction(Integer transactid) {
        return null;
    }

    @Override
    public Collection<IPayment> getCollection() {
        return null;
    }

    private Collection<IFieldInfo> parseFields(String data) {

        Collection<IFieldInfo> fields = new HashSet<IFieldInfo>();

        try {
            Xml.parse(data, new FieldContentHandler(fields));
        } catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        return fields;
    }



    private class FieldContentHandler implements ContentHandler {

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
