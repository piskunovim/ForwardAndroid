package ru.forwardmobile.tforwardpayment.dealer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import ru.forwardmobile.tforwardpayment.MainAccessActivity;
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
public class DealerDataSource implements MainAccessActivity.onLoadListener,ITaskListener{

    String LOG_TAG = "DealerDataSource";

    //public static ArrayList<String> dealerInfo = new ArrayList<String>();

    Context context;

    // = имя дилера
    public static String dealersName;

    // = баланс
    public static String dealersBalance;

    // = можно израсходовать
    public static String dealersRealMoney;

    // = кредит
    public static String dealersCredit;

    // = деньги в пути
    public static String dealersMoneyGo;

    // = номер точки
    public static String dealersPoint;


    public DealerDataSource(Context context) {
          this.context = context;
    }

    @Override
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


                dealersName = "Иванов Иван Иванович";
                dealersBalance = balanceToView;
                dealersCredit = creditToView;
                dealersRealMoney = totalToView;

                //String[] items = new String[]{ "Иванов Иван Иванович", balanceToView, creditToView, totalToView};
                //dealerInfo.addAll(Arrays.asList(items));

                Log.d(LOG_TAG, dealersBalance);

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
            DealerDataTask dt = new DealerDataTask(this, context);
            dt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public ArrayList<String> getDealerInfo(){
        return dealerInfo;
    }
*/

}
