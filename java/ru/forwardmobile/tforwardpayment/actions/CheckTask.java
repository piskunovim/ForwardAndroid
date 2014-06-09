package ru.forwardmobile.tforwardpayment.actions;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.net.URLEncoder;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.network.ServerRequestFactory;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.util.http.IRequest;

/**
 * Проверка возможности платежа
 * @author Vasiliy Vanin
 */
public class CheckTask extends AsyncTask<Void, Integer, Integer> {
    
    private  final String    TAG = "TFORWARD.CheckTask";
    final IPayment payment;
    final Context  ctx;
    
    
    
    public CheckTask(Context ctx, IPayment payment) {
        this.ctx = ctx;
        this.payment = payment;
    }
    
    @Override
    protected Integer doInBackground(Void... params) {
                        
        StringBuilder command = new StringBuilder();
        StringBuilder fields  = new StringBuilder();
        command.append("command=JT_CHECK_TARGET");
        command.append("&psid=" + payment.getPsid());
        // DEBUG
        command.append("&value=10");
        
        for(IFieldInfo field: payment.getFields()) {
            if("target" . equals(field.getName())) {
                command.append("&target=" + field.getValue());
            } else {
                fields.append("<f n=\"" + field.getName() + "\">" + field.getValue() + "</f>");
            }
        }
        
        try {
            command.append("&data=" + URLEncoder.encode("<data>" + fields.toString() + "</data>","UTF-8") );
        }catch(Exception ex) {
            Log.e(TAG,"Encoding exception " + ex.getMessage());
            return -1;
        }
        
        IRequest request = ServerRequestFactory.getRequest(command.toString());
        HttpTransport transport = new HttpTransport();
        
        try {
            transport.setCryptEngine(new CryptEngineImpl(ctx));
        }catch(Exception ex) {
            Log.e(TAG,"Cryptengine exception " + ex.getMessage());
            return -1;
        }
        
        try {
            byte[] response = transport.send(request);
            Log.i(TAG, new String(response));

        } catch(Exception ex) {
            Log.e(TAG, "Transport exception " + ex.getMessage());
            return -1;
        }
        
        return 0;
    }
    
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values); 
    }
    
    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result); //To change body of generated methods, choose Tools | Templates.
    }
}
