package ru.forwardmobile.tforwardpayment.dealer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.Settings;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.files.FileOperationsImpl;
import ru.forwardmobile.tforwardpayment.settings.TimeClass;

/**
 * Created by PiskunovI on 29.08.2014.
 *
 *  Класс отвечает за осуществление запроса данных дилера, их разбор,
 *  а также вывод в соответствующих блоках пользовательского интерфейса
 */

public class DealerInfo {

    protected ViewGroup viewGroup;
    protected Context context;
    FileOperationsImpl foi;

    public DealerInfo(ViewGroup _viewGroup, Context _context) {
        viewGroup = _viewGroup;
        context = _context;
    }

    // = Заполняем блок краткой информации дилера = //
    public void setDealerShotBlock(String dealerName, String dealerBalance, String dealerRealMoney, String dealerCredit) {
        setDealerName(dealerName);
        setDealerBalance(dealerBalance);
        setDealerRealMoney(dealerRealMoney);
        setDealerCredit(dealerCredit);
    }


    // = Заполняем информационный блок дилера = //
    public void setDealerInfoBlock(String dealerName, String dealerPoint) {
        setDealerName(dealerName);
        setDealerPoint(dealerPoint);
    }

    public void setDealerName(String name) {
        TextView dealerName = (TextView) viewGroup.findViewById(R.id.dealerName);
        dealerName.setText(name);
    }

    public void setDealerPoint(String pointNum) {
        TextView dealerPoint = (TextView) viewGroup.findViewById(R.id.pointNum);
        dealerPoint.setText(pointNum);
    }

    // = Заполняем блок дилера = //
    public void setDealerFinanceBlock(String balance, String credit, String moneyGo, String realMoney, String fee, String blockedMoney) {
        setDealerBalance(balance);
        setDealerCredit(credit);
        setDealerMoneyGo(moneyGo);
        setDealerRealMoney(realMoney);
        setDealerFee(fee);
        setDealerBlockedMoney(blockedMoney);
    }

    public void setDealerBalance(String text) {
        TextView dealerBalance = (TextView) viewGroup.findViewById(R.id.dealerBalance);
        dealerBalance.setText(text + " р.");
    }

    public void setDealerCredit(String text) {
        TextView dealerCredit = (TextView) viewGroup.findViewById(R.id.dealerKredit);
        dealerCredit.setText(text + " р.");
    }

    public void setDealerMoneyGo(String text) {
        TextView dealerMoneyGo = (TextView) viewGroup.findViewById(R.id.dealerMoneyGo);
        dealerMoneyGo.setText(text + " р.");
    }

    public void setDealerRealMoney(String text) {
        TextView dealerRealMoney = (TextView) viewGroup.findViewById(R.id.dealerRealMoney);
        dealerRealMoney.setText(text + " р.");
    }

    public void setDealerFee(String text) {
        TextView dealerFee = (TextView) viewGroup.findViewById(R.id.dealerFee);
        dealerFee.setText(text + " р.");
    }

    public void setDealerBlockedMoney(String text) {
        TextView dealerBlockedMoney = (TextView) viewGroup.findViewById(R.id.dealerBlockedMoney);
        dealerBlockedMoney.setText(text + " р.");
    }

    // = Заполняем блок менеджера = //
    public void setManagerBlock(String managerName, String managerNumber, String managerEmail) {
        setManagerName(managerName);
        setManagerNumber(managerNumber);
        setManagerEmail(managerEmail);
    }

    public void setManagerName(String name) {
        TextView managerName = (TextView) viewGroup.findViewById(R.id.managerFio);
        managerName.setText(name);
    }

    public void setManagerNumber(String number) {
        TextView managerNumber = (TextView) viewGroup.findViewById(R.id.managerCallNumber);
        managerNumber.setText(number);
    }

    public void setManagerEmail(String email) {
        TextView managerEmail = (TextView) viewGroup.findViewById(R.id.managerEmail);
        managerEmail.setText(email);
    }

    //----------------------------------
    // Методы для работы с базой данных
    //----------------------------------

    String  dealersName,          // = имя дилера
            dealersBalance,       // = баланс
            dealerMayExpend,      // = можно израсходовать
            dealersCredit,        // = кредит
            dealerSummFuftutres,  // = деньги в пути
            dealersPoint,         // = номер точки
            dealerFee,            // = невыплаченное вознаграждение
            dealerRetentionAmount,// = заблокированно средств
            managerName,          // = имя менеджера
            managerPhone,         // = телефон менеджера
            managerEmail;         // = почта менеджера

    public void getDealerInfo(){
        dealerInfoMissing();
        try{

            String JSONString = requestDealerInfo();
            Log.d("JSONObject Title", JSONString);

            JsonObject o = new JsonParser().parse(JSONString).getAsJsonObject();

            dealersName = o.getAsJsonPrimitive("title").toString().substring(1, o.getAsJsonPrimitive("title").toString().length() - 1);
            dealersBalance = o.getAsJsonPrimitive("account").toString().substring(1, o.getAsJsonPrimitive("account").toString().length() - 1);
            dealersCredit = o.getAsJsonPrimitive("credit").toString().substring(1, o.getAsJsonPrimitive("credit").toString().length() - 1);
            dealerRetentionAmount = o.getAsJsonPrimitive("retention_amount").toString().substring(1, o.getAsJsonPrimitive("retention_amount").toString().length() - 1);
            dealerMayExpend = o.getAsJsonPrimitive("may_expend").toString().substring(1, o.getAsJsonPrimitive("may_expend").toString().length() - 1);
            dealerFee = o.getAsJsonPrimitive("fee").toString().substring(1, o.getAsJsonPrimitive("fee").toString().length() - 1);
            dealerSummFuftutres = o.getAsJsonPrimitive("summ_fuftutres").toString().substring(1, o.getAsJsonPrimitive("summ_fuftutres").toString().length() - 1);

            managerName = o.getAsJsonPrimitive("fio").toString().substring(1, o.getAsJsonPrimitive("fio").toString().length() - 1);
            managerPhone = o.getAsJsonPrimitive("phone").toString().substring(1, o.getAsJsonPrimitive("phone").toString().length() - 1);
            managerEmail = o.getAsJsonPrimitive("email").toString().substring(1, o.getAsJsonPrimitive("email").toString().length() - 1);

            Settings.set(context, Settings.DEALERS_NAME, dealersName);

            ContentValues cv = new ContentValues();

            DatabaseHelper dbHelper = new DatabaseHelper(context);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM " + DatabaseHelper.DEALER_TABLE_NAME + " WHERE 1");

            cv.put("name", dealersName);
            cv.put("balance", dealersBalance);
            cv.put("may_expend", dealerMayExpend);
            cv.put("credit", dealersCredit);
            cv.put("retention_amount", dealerRetentionAmount);
            cv.put("fee", dealerFee);
            cv.put("summ_fuftutres", dealerSummFuftutres);
            cv.put("fio", managerName);
            cv.put("phone", managerPhone);
            cv.put("email", managerEmail);

            db.insert(DatabaseHelper.DEALER_TABLE_NAME, null, cv);
            cv.clear();
        }
        catch(Exception ex)
       {
           // Ошибка разбора
          ex.printStackTrace();
       }
    }

    // получаем информацию для блока
    public void getBlockInfo(){

        //"очищает" блок данных
        dealerInfoMissing();

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.DEALER_TABLE_NAME , null);

        if (c.moveToFirst()) {
            dealersName = c.getString(c.getColumnIndex("name"));
            dealersBalance = c.getString(c.getColumnIndex("balance"));
            dealerMayExpend = c.getString(c.getColumnIndex("may_expend"));
            dealersCredit = c.getString(c.getColumnIndex("credit"));
        }

        c.close();

        Log.d("DealerInfo: ", dealersName + "; Balance: " + dealersBalance + "; MayExpend: " +dealerMayExpend + "; credit:" + dealersCredit);

        // заполняем информационный блок информацией дилера
        setDealerShotBlock(dealersName, dealersBalance, dealerMayExpend, dealersCredit);
    }

    public void getFullInfo(){

        //"очищает" блок данных
        dealerInfoMissing();

        setToLog("Getting dealer's information from the base");

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.DEALER_TABLE_NAME , null);

        if (c.moveToFirst()) {
            dealersName            = c.getString(c.getColumnIndex("name"));
            dealersBalance         = c.getString(c.getColumnIndex("balance"));
            dealerMayExpend        = c.getString(c.getColumnIndex("may_expend"));
            dealersCredit          = c.getString(c.getColumnIndex("credit"));
            dealerSummFuftutres    = c.getString(c.getColumnIndex("summ_fuftutres"));
            dealersPoint           = Settings.get(context, "pointid").toString();
            dealerFee              = c.getString(c.getColumnIndex("fee"));
            dealerRetentionAmount  = c.getString(c.getColumnIndex("retention_amount"));
            managerName            = c.getString(c.getColumnIndex("fio"));
            managerPhone           = c.getString(c.getColumnIndex("phone"));
            managerEmail           = c.getString(c.getColumnIndex("email"));
        }

        c.close();

        setToLog("Set dealer's information to Block");

        setDealerInfoBlock(dealersName, dealersPoint);
        setDealerFinanceBlock(dealersBalance, dealersCredit, dealerSummFuftutres, dealerMayExpend, dealerFee, dealerRetentionAmount);
        setManagerBlock(managerName, managerPhone, managerEmail);
    }


    public void dealerInfoMissing(){
        dealersName            = "Отсутствует";  // = имя дилера
        dealersBalance         = "Отсутствует";  // = баланс
        dealerMayExpend        = "Остутствует";  // = можно израсходовать
        dealersCredit          = "Отсутствует";  // = кредит
        dealerSummFuftutres    = "Отсутствует";  // = деньги в пути
        dealersPoint           = "Отсутствует";  // = номер точки
        dealerFee              = "Отсутствует";  // = невыплаченное вознаграждение
        dealerRetentionAmount  = "Отсутствует";  // = заблокированно средств
        managerName            = "Отсутствует";  // = имя менеджера
        managerPhone           = "Отсутствует";  // = телефон менеджера
        managerEmail           = "Отсутствует";  // = почта менеджера;
    }

    public String requestDealerInfo() throws Exception {

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
        } finally {

            if(in != null) {
                try { in.close(); } catch (Exception ex){}
            }

            if(r != null) {
                try { r.close();} catch (Exception ex){}
            }
        }
    }

    void setToLog(String logMessage){
        foi = null;

        try {
            foi = new FileOperationsImpl(context);
            foi.writeToFile(new TimeClass().getFullCurrentDateString() + logMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
