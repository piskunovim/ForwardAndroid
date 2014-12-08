package ru.forwardmobile.tforwardpayment.operators;

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

/**
 * Created by PiskunovI on 10.11.2014.
 */
public class GetOperatorsXML extends AsyncTask<String,Void,Boolean> {
    private Context mContext;
    public GetOperatorsXML (Context context){
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String Tag = "HTTPConnectionTag";
        HttpClient httpclient = new DefaultHttpClient();
        //HttpGet httpget = new HttpGet("http://192.168.1.242:3000/get_operators");
        HttpGet httpget = new HttpGet("http://forwardmobile.ru:3000/get_operators");

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
}