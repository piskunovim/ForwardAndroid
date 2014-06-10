package ru.forwardmobile.tforwardpayment.reports;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.R;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.tforwardpayment.spp.impl.CommandRequestImpl;

/**
 * Created by PiskunovI on 10.06.14.
 */
public class TBalanceReportScreenImpl extends ActionBarActivity {

    String LOG_TAG = "TForwardPayment.TBalanceReport";
    TextView balanceView, creditView, totalView;
    RequestTask rt;
    IResponseSet responseSet;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_balance);

            balanceView = (TextView)findViewById(R.id.balView); // View "Текущий баланс"
            creditView = (TextView)findViewById(R.id.creditView); //View "Кредитный лимит"
            totalView = (TextView)findViewById(R.id.totalView); //View "Можно израсходовать"
            try{
            //ICommandRequest request = new CommandRequestImpl("command=JT_GET_BALANCE_INFO", true, true);
            //HttpTransport transport = new HttpTransport();
            //transport.setCryptEngine(new CryptEngineImpl(this));
            rt = new RequestTask();
            rt.execute();//transport);

            }
            catch(Exception e){
                e.printStackTrace();
            }

        }


    public void processResponse(IResponseSet responseSet) throws Exception {
        double balance = 0;
        double limit = 0;
        double total = 0;

        // разбираем ответ
        try {
            if(responseSet == null || responseSet.getSize() == 0)
                throw new Exception("Пустой ответ сервера.");
            ICommandResponse response = (ICommandResponse)responseSet.getResponses().get(0);
            Log.d(LOG_TAG,response.toString());
            balance = Double.parseDouble(response.getParam("balance"));
            limit = Double.parseDouble(response. getParam("limit"));
            total = balance+limit;
        } catch(Exception e) {
            throw new Exception("Ошибка запроса баланса: " + e.getMessage());
        }


        // Показываем результат
        if(balance != 0)
            balanceView.setText("Текущий баланс: " + String.valueOf(balance) );
        if(limit != 0)
            creditView.setText("Кредитный лимит: " + String.valueOf(limit) );
        if(total != 0)
            if(total > 0)
                totalView.setText("Можно израсходовать: " + String.valueOf(total));
            else
                totalView.setText("Прием платежей невозможен!");

    }

    public interface OnTaskCompleteListener {
        // Notifies about task completion
        void onTaskComplete(IResponseSet result);
    }

    class RequestTask extends AsyncTask<Void, Void, IResponseSet> {

        @Override
        protected IResponseSet doInBackground(Void... param) {

            try{
                ICommandRequest request = new CommandRequestImpl("command=JT_GET_BALANCE_INFO", true, true);
                HttpTransport transport = new HttpTransport();
                transport.setCryptEngine(new CryptEngineImpl(TBalanceReportScreenImpl.this));
                responseSet = transport.send(request);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return responseSet;
        }

        @Override
        protected void onPostExecute(IResponseSet result) {
            super.onPostExecute(result);
            try{
            processResponse(result);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
