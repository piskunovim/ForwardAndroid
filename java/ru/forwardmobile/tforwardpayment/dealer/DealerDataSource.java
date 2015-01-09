package ru.forwardmobile.tforwardpayment.dealer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ru.forwardmobile.tforwardpayment.MainAccessActivity;
import ru.forwardmobile.tforwardpayment.Settings;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.tforwardpayment.spp.impl.CommandRequestImpl;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Created by PiskunovI on 09.09.2014.
 */
public class DealerDataSource implements MainAccessActivity.onLoadListener{

    /*
    Позволю сделать здесь парочку заметок по поводу того, что не понимаю смысла существования данного модуля
    1. Очевидно спроектирован данный класс был в далекие времена, которые благополучно забыты.
    2. Его функциональность уже перенесена в DealerInfo. Что есть приятно ^_^
    3. Следует еще раз задаться вопросом: все ли перенесли?
    */
    String LOG_TAG = "DealerDataSource";

    // = имя дилера
    public static String dealersName;

    // = баланс
    public static String dealersBalance;

    // = можно израсходовать
    public static String dealerMayExpend;

    // = кредит
    public static String dealersCredit;

    // = деньги в пути
    public static String dealerSummFuftutres;

    // = номер точки
    // public static String dealersPoint;

    // = невыплаченное вознаграждение
    public static String dealerFee;

    // = заблокированно средств
    public static String dealerRetentionAmount;

    // = имя менеджера
    public static String managerName;

    // = телефон менеджера
    public static String managerPhone;

    // = почта менеджера
    public static String managerEmail;


    public String loadDealerInfo(Context context) {

        Log.d(LOG_TAG, "loadDealerInfo started");

            SQLiteDatabase db = null;
            try {

                String JSONString  = getDealerInfo(context);
                Log.d("JSONObject Title", JSONString);

                JsonObject o = new JsonParser().parse(JSONString).getAsJsonObject();

                dealersName = o.getAsJsonPrimitive("title").toString().substring(1,o.getAsJsonPrimitive("title").toString().length()-1);
                dealersBalance = o.getAsJsonPrimitive("account").toString().substring(1,o.getAsJsonPrimitive("account").toString().length()-1);
                dealersCredit = o.getAsJsonPrimitive("credit").toString().substring(1,o.getAsJsonPrimitive("credit").toString().length()-1);
                dealerRetentionAmount = o.getAsJsonPrimitive("retention_amount").toString().substring(1,o.getAsJsonPrimitive("retention_amount").toString().length()-1);
                dealerMayExpend =  o.getAsJsonPrimitive("may_expend").toString().substring(1,o.getAsJsonPrimitive("may_expend").toString().length()-1);
                dealerFee = o.getAsJsonPrimitive("fee").toString().substring(1,o.getAsJsonPrimitive("fee").toString().length()-1);
                dealerSummFuftutres = o.getAsJsonPrimitive("summ_fuftutres").toString().substring(1,o.getAsJsonPrimitive("summ_fuftutres").toString().length()-1);

                managerName = o.getAsJsonPrimitive("fio").toString().substring(1,o.getAsJsonPrimitive("fio").toString().length()-1);
                managerPhone = o.getAsJsonPrimitive("phone").toString().substring(1,o.getAsJsonPrimitive("phone").toString().length()-1);
                managerEmail = o.getAsJsonPrimitive("email").toString().substring(1,o.getAsJsonPrimitive("email").toString().length()-1);

                ContentValues cv  = new ContentValues();

                DatabaseHelper dbHelper = new DatabaseHelper(context);

                // подключаемся к БД
                db = dbHelper.getWritableDatabase();
                db.rawQuery("DELETE FROM " + DatabaseHelper.DEALER_TABLE_NAME + " WHERE 1", null);

                cv.put("name", dealersName);
                cv.put("balance", dealersBalance);
                cv.put("may_expend", dealerMayExpend);
                cv.put("credit", dealersCredit);
                cv.put("retention_amount",dealerRetentionAmount);
                cv.put("fee", dealerFee);
                cv.put("summ_fuftutres",dealerSummFuftutres);
                cv.put("fio", managerName);
                cv.put("phone", managerPhone);
                cv.put("email", managerEmail);

                db.insert(DatabaseHelper.DEALER_TABLE_NAME, null, cv);
                cv.clear();


            } catch (Exception ex) {
                // Ошибка разбора
                ex.printStackTrace();
                return "Ошибка получения информации об Агенте. "
                            + ex.getMessage();
            }
            finally {
                if(db!=null)
                    db.close();
            }

        return null;
    }

    public String getDealerInfo(Context context) throws Exception{



        URL url = new URL("http://"+ Settings.get(context, Settings.NODE_HOST)+":"+ Settings.get(context, Settings.NODE_PORT)+"/dealers_info/"+ Settings.get(context, Settings.POINT_ID));
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        InputStream in = null;
        BufferedReader r = null;

        try {
            in = new BufferedInputStream(connection.getInputStream());
            r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;

            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        }finally {
            if(in != null) {
                try { in.close();} catch (Exception ex){}
            }

            if(r != null) {
                try { r.close();}catch (Exception ex){}
            }
        }
    }


    @Override
    public String beforeApplicationStart(Context context) {

        Log.i(LOG_TAG,"started");
        return loadDealerInfo(context);
    }

}
