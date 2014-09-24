package ru.forwardmobile.tforwardpayment.reports;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;

import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.tforwardpayment.spp.impl.CommandRequestImpl;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Created by PiskunovI on 10.06.14.
 */
public class BalanceActivity extends ActionBarActivity implements ITaskListener {

    String LOG_TAG = "TForwardPayment.TBalanceReport";
    TextView balanceView, creditView, totalView;
    RequestTask rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        balanceView     = (TextView) findViewById(R.id.balView); // View "Текущий баланс"
        creditView      = (TextView) findViewById(R.id.creditView); //View "Кредитный лимит"
        totalView       = (TextView) findViewById(R.id.totalView); //View "Можно израсходовать"

        try{
            rt = new RequestTask(this,this);
            rt.execute();
        }
        catch(Exception e){
             e.printStackTrace();
        }
    }


    @Override
    public void onTaskFinish(Object result) {

        if(result != null && result instanceof IResponseSet) {

            IResponseSet responseSet = (IResponseSet) result;

            double balance  = 0;
            double limit    = 0;
            double total    = 0;

            try {
                // разбираем ответ
                ICommandResponse response = (ICommandResponse) responseSet.getResponses().get(0);
                Log.d(LOG_TAG, response.toString());
                balance = Double.parseDouble(response.getParam("balance"));
                limit = Double.parseDouble(response.getParam("limit"));
                total = balance + limit;

                // Показываем результат
                if (balance != 0)
                    balanceView.setText("Текущий баланс: \n" + String.valueOf(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + " руб.");
                if (limit != 0)
                    creditView.setText("Кредитный лимит: \n" + String.valueOf(new BigDecimal(limit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + " руб.");
                if (total != 0)
                    if (total > 0)
                        totalView.setText("Можно израсходовать: \n" + String.valueOf(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + " руб.");
                    else
                        totalView.setText("Прием платежей невозможен!");
            } catch (Exception ex) {
                // Ошибка разбора

            }
        } else {
            // Ошибка отправки запроса
        }
    }

    class RequestTask extends AbstractTask {

        public RequestTask(ITaskListener listener, Context ctx) {
            super(listener,ctx);
        }

        @Override
        protected IResponseSet doInBackground(Object... param) {

            IResponseSet responseSet = null;

            try{
                ICommandRequest request = new CommandRequestImpl("command=JT_GET_BALANCE_INFO", true, true);
                HttpTransport transport = new HttpTransport();
                transport.setCryptEngine(new CryptEngineImpl(BalanceActivity.this));
                responseSet = transport.send(request);
            } catch(Exception e)
            {
                e.printStackTrace();
            }

            return responseSet;
        }
    }

}
