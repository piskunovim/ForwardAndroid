package ru.forwardmobile.tforwardpayment.dealer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ru.forwardmobile.tforwardpayment.MainAccessActivity;
import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.tforwardpayment.spp.impl.CommandRequestImpl;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Created by PiskunovI on 09.09.2014.
 */
public class DealerDataSource implements MainAccessActivity.onLoadListener{

    String LOG_TAG = "DealerDataSource";

    //public static ArrayList<String> dealerInfo = new ArrayList<String>();

    Context context;

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
    //public static String dealersPoint;

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

    public DealerDataSource(Context context) {
          this.context = context;
    }


    public void onTaskFinish(Object result) {

        Log.d(LOG_TAG, "onTaskFinish started");
        Log.d(LOG_TAG, result.toString());
        if(result != null && result instanceof IResponseSet) {

            IResponseSet responseSet = (IResponseSet) result;

            double balance  = 0;
            double limit    = 0;
            double total    = 0;

            try {
                // разбираем ответ
                ICommandResponse response = (ICommandResponse) responseSet.getResponses().get(0);


                balance = Double.parseDouble(response.getParam("balance"));
                limit = Double.parseDouble(response.getParam("limit"));
                total = balance + limit;


                String balanceToView = "";
                String creditToView = "";
                String totalToView = "";

                // Показываем результат
                if (balance != 0)
                    balanceToView = String.valueOf(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
                if (limit != 0)
                    creditToView = String.valueOf(new BigDecimal(limit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                if (total != 0)
                    if (total > 0)
                        totalToView = String.valueOf(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    else
                        totalToView =  "Прием платежей невозможен!";

                DealerAsyncTask dealerAsyncTask = new DealerAsyncTask();
                dealerAsyncTask.execute();
                String JSONString = new String();

                try {
                    JSONString = dealerAsyncTask.get();
                    Log.d("JSONObject Title", JSONString);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                JsonObject o = new JsonParser().parse(JSONString).getAsJsonObject();

                /*dealersName = o.getAsJsonPrimitive("title").toString().substring(1,o.getAsJsonPrimitive("title").toString().length()-1);
                dealersBalance = o.getAsJsonPrimitive("account").toString().substring(1,o.getAsJsonPrimitive("account").toString().length()-1);
                dealersCredit = o.getAsJsonPrimitive("credit").toString().substring(1,o.getAsJsonPrimitive("credit").toString().length()-1);
                dealersRealMoney = o.getAsJsonPrimitive("may_expend").toString().substring(1,o.getAsJsonPrimitive("may_expend").toString().length()-1);
                */

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


              //  DatabaseHelper databaseHelper = new DatabaseHelper();
              //  databaseHelper.getReadableDatabase;

            } catch (Exception ex) {
                // Ошибка разбора
                ex.printStackTrace();
            }
        } else {
            // Ошибка отправки запроса
        }
    }

    class DealerDataTask extends AbstractTask {

        public DealerDataTask(ITaskListener listener, Context ctx) {
            super(listener, ctx);
        }



        @Override
        protected Object doInBackground(Object... objects) {


            IResponseSet responseSet = null;

            try{
                ICommandRequest request = new CommandRequestImpl("command=JT_GET_BALANCE_INFO", true, true);
                HttpTransport transport = new HttpTransport();
                transport.setCryptEngine(new CryptEngineImpl(context));
                responseSet = transport.send(request);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return responseSet;

        }
    };


    @Override
    public void beforeApplicationStart(Context context) {

        Log.i(LOG_TAG,"started");

        try {
            DealerDataTask dt = new DealerDataTask(null, context);
            dt.execute();
            onTaskFinish(dt.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
