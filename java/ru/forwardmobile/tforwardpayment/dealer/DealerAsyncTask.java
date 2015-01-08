package ru.forwardmobile.tforwardpayment.dealer;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.forwardmobile.tforwardpayment.Settings;

/**
 * Created by PiskunovI on 20.11.2014.
 */
public class DealerAsyncTask extends AsyncTask<Object,Void,String> {

    private final Context context;
    public DealerAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            URL url = new URL("http://"+ Settings.get(context, Settings.NODE_HOST)+":"+ Settings.get(context, Settings.NODE_PORT)+"/dealers_info/"+ Settings.get(context, Settings.POINT_ID));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}