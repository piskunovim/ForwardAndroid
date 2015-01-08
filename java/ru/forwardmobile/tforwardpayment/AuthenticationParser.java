package ru.forwardmobile.tforwardpayment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Stack;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.security.IKeyStorage;
import ru.forwardmobile.tforwardpayment.security.KeyStorageFactory;

/**
 * Created by PiskunovI on 08.05.14.
 */
public class AuthenticationParser {

    final String LOG_TAG = "ParserLog";
    Context context;
    IKeyStorage keyStorage = null;

    String tmp = "";

public AuthenticationParser(Context context) {
    this.context = context;
    keyStorage   = KeyStorageFactory.getKeyStorage(context);
}

/**
 * @deprecated Use constructor with context
 */
public AuthenticationParser(){}

public void loadSettings(String xmlString) throws XmlPullParserException, IOException {

    DatabaseHelper dbHelper = new DatabaseHelper(context);
    SQLiteDatabase db       = dbHelper.getWritableDatabase();

    db.beginTransaction();

    try {

        db.execSQL("delete from " + DatabaseHelper.P_TABLE_NAME);
        db.execSQL("delete from " + DatabaseHelper.PG_TABLE_NAME);
        db.execSQL("delete from " + DatabaseHelper.F_TABLE_NAME);

        //Log.i("PARSER", "Loading settings from " + xmlString.substring(0,155));
        XmlPullParser xpp = prepareXpp(xmlString);
        Log.i("PARSER", "Successfull init..");

        Integer currentGroup = 0;
        Integer currentPs    = 0;
        Stack<Integer> hierarchy = new Stack<Integer>();
        String buffer = null;
        boolean insideSettings = false;
        String currentProperty = null;

        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {

            switch (xpp.getEventType()) {
                case XmlPullParser.START_DOCUMENT:
                    break;

                case XmlPullParser.START_TAG:
                    String startNodeName = xpp.getName();

                    if ("s".equals(startNodeName)) {
                        insideSettings = true;
                    } else
                    // Settings properties
                    if ("v".equals(startNodeName)) {
                        currentProperty = xpp.getAttributeValue(0);
                        Log.i("PARSER", "Property " + currentProperty);
                    }

                    break;
                case XmlPullParser.END_TAG:
                    String endNodeName = xpp.getName();

                    if("p-g".equals(endNodeName)) {
                        currentGroup = hierarchy.empty() ? 0 : hierarchy.pop();
                    }

                    if (buffer.length() > 0) {
                        // Shared key
                        if ("s-c".equals(xpp.getName())) {
                            Log.d(LOG_TAG, "Adding shared key...");
                            keyStorage.setKey(IKeyStorage.PUBLIC_KEY_TYPE, buffer.getBytes());
                        } else {
                            // Private key
                            if ("p-k".equals(xpp.getName())) {
                                Log.d(LOG_TAG, "Adding private key...");
                                keyStorage.setKey(IKeyStorage.SECRET_KEY_TYPE, buffer.getBytes());
                            } else
                            // Access ID
                            if ("access-id".equals(xpp.getName())) {
                                Log.d(LOG_TAG, "Saving acces id. " + buffer);
                                Settings.set(context, Settings.CERTIFICATE_ACESS_ID, buffer);
                            } else if (insideSettings && "v".equals(xpp.getName())) {
                                Settings.set(context, currentProperty, buffer);
                            }
                        }
                    }

                    break;
                case XmlPullParser.TEXT:
                    buffer = xpp.getText();
                    break;

                default:
                    // nothing to do
                    break;
            }

            xpp.next();
        }

        // Подтверждаем изменения
        db.setTransactionSuccessful();

        Log.i("PARESER", "Transaction finished");
    } finally {
        // завершаем транзакцию, если до этого вызова не было вызова setTransactionSuccessful(),
        // все изменения, которые успели произойти, откатятся
        db.endTransaction();
        db.close();
        dbHelper.close();

        Log.i("PARSER", "Parsing finished.");
    }

}


public void GetXMLSettings(String xmlstring ) throws Exception {

    // создаем объект для данных
    ContentValues cv  = new ContentValues();
    ContentValues cv2 = new ContentValues();
    int gid = 0;
    int id = 0;

    DatabaseHelper dbHelper = new DatabaseHelper(context);

    // подключаемся к БД
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    // Для того чтобы правильно загрузить настройки,
    // нужно сначала очистить старые. Но, если что-то пойдет не так,
    // то у нас не будет ни старых ни новых настроек
    // Поэтому перед загрузкой создается транзакция БД, в которую войдут все запросы
    // по очистке и загрузке. Она либо срабатывает вся целиком, либо откатывается, в случае ошибки
    db.beginTransaction();


    try {
        // Чистим таблицы
        db.execSQL("delete from " + DatabaseHelper.P_TABLE_NAME);
        db.execSQL("delete from " + DatabaseHelper.PG_TABLE_NAME);
        db.execSQL("delete from " + DatabaseHelper.F_TABLE_NAME);

        boolean insideSettings = false;
        String currentProperty = new String();
        String buffer = new String();

        XmlPullParser xpp = prepareXpp(xmlstring);

        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                // начало документа
                case XmlPullParser.START_DOCUMENT:

                    break;
                // начало тэга
                case XmlPullParser.START_TAG:
                    tmp = "";
                    String tag_name = xpp.getName();


                    if (tag_name.equals("p-g")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            gid++;
                            cv.put("id", gid);
                            cv.put("parent", 0);
                            if (xpp.getAttributeName(i).equals("n")) {
                                cv.put("name", xpp.getAttributeValue(i));
                            }
                            db.insert(DatabaseHelper.PG_TABLE_NAME, null, cv);
                            cv.clear();
                        }
                    } else if (tag_name.equals("p")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            cv.put("gid", gid);
                            if (xpp.getAttributeName(i).equals("i")) {
                                id = Integer.parseInt(xpp.getAttributeValue(i)); //запомним идентификатор оператора

                                cv.put("id", xpp.getAttributeValue(i));

                                // вставляем запись и получаем ее ID
                                // operators.add(xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("n")) {
                                cv.put("name", xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("min")) {
                                cv.put("min", xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("max")) {
                                cv.put("max", xpp.getAttributeValue(i));

                                long rowID = db.insert(DatabaseHelper.P_TABLE_NAME, null, cv);
                            }
                        }
                    } else if (tag_name.equals("f")) {
                        cv.clear();
                        cv.put("provider", id);
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {

                            if (xpp.getAttributeName(i).equals("n")) {
                                cv.put("name", xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("c")) {
                                cv.put("title", xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("p")) {
                                cv.put("prefix", xpp.getAttributeValue(i));
                                //Log.d("TagFGot: ",xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("m")) {
                                cv.put("mask", xpp.getAttributeValue(i));
                                //Log.d("TagFGot: ",xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("r")) {
                                cv.put("required", xpp.getAttributeValue(i));
                            } else if (xpp.getAttributeName(i).equals("t")) {
                                cv.put("type", xpp.getAttributeValue(i));
                            }
                        }
                        db.insert(DatabaseHelper.F_TABLE_NAME, null, cv);
                    } else
                        // Start settings part
                        if ("s".equals(tag_name)) {
                            insideSettings = true;
                        } else
                            // Settings properties
                            if ("v".equals(tag_name)) {
                                currentProperty = xpp.getAttributeValue(0);
                            }

                    if (!TextUtils.isEmpty(tmp))
                        Log.d(LOG_TAG, "Attributes: " + tmp);
                    break;
                // конец тэга
                case XmlPullParser.END_TAG:

                    cv.clear();
                    if (buffer.length() > 0) {
                        // Shared key
                        if ("s-c".equals(xpp.getName())) {
                            Log.d(LOG_TAG, "Adding shared key...");
                            keyStorage.setKey(IKeyStorage.PUBLIC_KEY_TYPE, buffer.getBytes());
                        } else
                        // Private key
                        if ("p-k".equals(xpp.getName())) {
                            Log.d(LOG_TAG, "Adding private key...");
                            keyStorage.setKey(IKeyStorage.SECRET_KEY_TYPE, buffer.getBytes());
                        } else
                        // Access ID
                        if ("access-id".equals(xpp.getName())) {
                            Log.d(LOG_TAG, "Saving acces id. " + buffer);
                            Settings.set(context, Settings.CERTIFICATE_ACESS_ID, buffer);
                        } else if (insideSettings && "v".equals(xpp.getName())) {
                            Settings.set(context, currentProperty, buffer);
                        }
                    }

                    break;

                case XmlPullParser.TEXT:
                    buffer = xpp.getText();
                    break;

                default:
                    break;
            }
            // следующий элемент
            xpp.next();
        }

        // Подтверждаем изменения
        db.setTransactionSuccessful();

    } catch (XmlPullParserException e) {
        e.printStackTrace();
        throw new Exception(e);
    } catch (IOException e) {
        e.printStackTrace();
        throw new Exception(e);
    } finally {
        // завершаем транзакцию, если до этого вызова не было вызова setTransactionSuccessful(),
        // все изменения, которые успели произойти, откатятся
        db.endTransaction();
        db.close();
        dbHelper.close();
    }
   }

    XmlPullParser prepareXpp(String xmlstring) throws XmlPullParserException {
        // получаем фабрику
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // включаем поддержку namespace (по умолчанию выключена)
        factory.setNamespaceAware(true);
        // создаем парсер
        XmlPullParser xpp = factory.newPullParser();
        // даем парсеру на вход Reader
        xpp.setInput(new StringReader(xmlstring));
        return xpp;
    }
}
