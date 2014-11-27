package ru.forwardmobile.tforwardpayment.dealer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.forwardmobile.tforwardpayment.TSettings;

/**
 * Created by PiskunovI on 20.11.2014.
 */
public class DealerAsyncTask extends AsyncTask<Object,Void,String> {

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            URL url = new URL("http://192.168.1.242:3000/dealers_info/"+ TSettings.get("pointid"));
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