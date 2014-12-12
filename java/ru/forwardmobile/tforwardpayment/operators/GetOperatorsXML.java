package ru.forwardmobile.tforwardpayment.operators;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;

import ru.forwardmobile.tforwardpayment.MainActivity;
import ru.forwardmobile.tforwardpayment.TParseOperators;
import ru.forwardmobile.tforwardpayment.TSettings;

/**
 * Created by PiskunovI on 10.11.2014.
 */
public class GetOperatorsXML extends AsyncTask<String,Void,Boolean> {
    private Context mContext;
    ProgressDialog dialog;
    public GetOperatorsXML (Context context){
        mContext = context;
        dialog = new ProgressDialog(mContext);
        dialog.setTitle("Загрузка");
        dialog.setMessage("Пожалуйста ждите.");
    }







  /*  @Override
    protected void onPreExecute() {

        dialog.show();
        super.onPreExecute();
    }
*/

    @Override
    protected Boolean doInBackground(String... strings) {
        String Tag = "HTTPConnectionTag";
        HttpClient httpclient = new DefaultHttpClient();
        //HttpGet httpget = new HttpGet("http://192.168.1.242:3000/get_operators");
        //HttpGet httpget = new HttpGet("http://192.168.1.6:3000/get_operators");
        HttpGet httpget = new HttpGet("http://"+TSettings.get(TSettings.NODE_HOST)+":"+TSettings.get(TSettings.NODE_PORT)+"/get_operators");

        String filename = "operators.xml";
        FileOutputStream outputStream;

        try {
            Log.d(Tag, "Запрос отправлен!");

            HttpResponse response = httpclient.execute(httpget);

            HttpEntity httpEntity = response.getEntity();

            String line = EntityUtils.toString(httpEntity, "UTF-8");
            Log.d(Tag, line);

            try {
                outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(line.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        catch (ClientProtocolException e) {
            Log.d(Tag, "Ошибка!");
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
// TODO Auto-generated catch block
            Log.d(Tag, "Запрос не отправлен!");
            e.printStackTrace();
            return false;
        }

    }

/*    @Override
    protected void onPostExecute(Boolean result) {
         try {
             if ((this.dialog != null) && this.dialog.isShowing()) {
                 this.dialog.dismiss();
             }
             super.onPostExecute(result);
         }
         catch (final IllegalArgumentException e){
             // Handle or log or ignore
         }catch (final Exception e) {
             // Handle or log or ignore
         } finally {
             this.dialog = null;
         }

    }
    */
}