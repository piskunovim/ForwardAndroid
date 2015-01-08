package ru.forwardmobile.tforwardpayment.operators;

import android.content.Context;
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

import ru.forwardmobile.tforwardpayment.Settings;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Created by PiskunovI on 10.11.2014.
 */
public class GetOperatorsXML extends AbstractTask {


    public GetOperatorsXML (Context context, ITaskListener listener){
        super(listener, context);
    }


    @Override
    protected Object doInBackground(Object... strings) {
        String Tag = "HTTPConnectionTag";
        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet("http://"+ Settings.get(getContext(), Settings.NODE_HOST)+":"+ Settings.get(getContext(), Settings.NODE_PORT)+"/get_operators");

        String filename = "operators.xml";
        FileOutputStream outputStream;

        try {
            Log.d(Tag, "Запрос отправлен!");

            HttpResponse response = httpclient.execute(httpget);

            HttpEntity httpEntity = response.getEntity();

            String line = EntityUtils.toString(httpEntity, "UTF-8");
            Log.d(Tag, line);

            try {
                outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(line.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
        catch (ClientProtocolException e) {
            Log.d(Tag, "Ошибка!");
            e.printStackTrace();
            return 0;
        }
        catch (IOException e) {
        // TODO Auto-generated catch block
            Log.d(Tag, "Запрос не отправлен!");
            e.printStackTrace();
            return 0;
        }

    }

}