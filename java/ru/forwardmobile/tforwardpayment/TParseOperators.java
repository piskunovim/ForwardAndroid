package ru.forwardmobile.tforwardpayment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

/**
 * Created by PiskunovI on 08.05.14.
 */
public class TParseOperators {

    final String LOG_TAG = "ParserLog";
    Context context;


    String tmp = "";

public TParseOperators(){

}


public void GetXMLSettings(String xmlstring, SQLiteOpenHelper dbHelper){

    // создаем объект для данных
    ContentValues cv = new ContentValues();
    ContentValues cv2 = new ContentValues();
    int gid = 0;
    int id = 0;

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


                    if (tag_name.equals("p-g")){
                        for (int i=0; i< xpp.getAttributeCount(); i++){
                           gid++;
                           cv.put("id",gid);
                           if (xpp.getAttributeName(i).equals("n"))
                           {
                            cv.put("name",xpp.getAttributeValue(i));
                           }
                           db.insert("pg", null, cv);
                        }
                    }

                    else if (tag_name.equals("p"))
                    {
                      for (int i = 0; i < xpp.getAttributeCount(); i++) {
                        cv.put("gid",gid);
                        if (xpp.getAttributeName(i).equals("i"))
                        {
                           id = Integer.parseInt(xpp.getAttributeValue(i)); //запомним идентификатор оператора

                           cv.put("id", xpp.getAttributeValue(i));

                            // вставляем запись и получаем ее ID
                           // operators.add(xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("n"))
                        {
                            cv.put("name", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("min"))
                        {
                           cv.put("min", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("max")){
                            cv.put("max", xpp.getAttributeValue(i));

                           long rowID = db.insert("p", null, cv);
                        }
                      }
                    }
                    else if (tag_name.equals("f")){
                        cv.clear();
                        cv.put("provider", id);
                        for (int i = 0; i < xpp.getAttributeCount(); i++){

                            if (xpp.getAttributeName(i).equals("n"))
                            {
                                cv.put("name", xpp.getAttributeValue(i));
                            }
                            else if (xpp.getAttributeName(i).equals("c"))
                            {
                                cv.put("title", xpp.getAttributeValue(i));
                            }
                            else if (xpp.getAttributeName(i).equals("p"))
                            {
                                cv.put("prefix", xpp.getAttributeValue(i));
                                //Log.d("TagFGot: ",xpp.getAttributeValue(i));
                            }
                            else if (xpp.getAttributeName(i).equals("m"))
                            {
                                cv.put("mask", xpp.getAttributeValue(i));
                                //Log.d("TagFGot: ",xpp.getAttributeValue(i));
                            }
                            else if (xpp.getAttributeName(i).equals("r"))
                            {
                                cv.put("required", xpp.getAttributeValue(i));
                            }
                            else if (xpp.getAttributeName(i).equals("t"))
                            {
                                cv.put("type", xpp.getAttributeValue(i));

                            }
                        }
                        db.insert("f", null, cv);
                    }



                    if (!TextUtils.isEmpty(tmp))
                        Log.d(LOG_TAG, "Attributes: " + tmp);
                    break;
                // конец тэга
                case XmlPullParser.END_TAG:

                    cv.clear();
                    break;

                case XmlPullParser.TEXT:

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
    } catch (IOException e) {
        e.printStackTrace();
    }

    // завершаем транзакцию, если до этого вызова не было вызова setTransactionSuccessful(),
    // все изменения, которые успели произойти, откатятся
    db.endTransaction();
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
