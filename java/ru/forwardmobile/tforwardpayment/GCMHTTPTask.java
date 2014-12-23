package ru.forwardmobile.tforwardpayment;

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
import java.net.URLEncoder;

/**
 * Created by PiskunovI on 01.12.2014.
 */
public class GCMHTTPTask extends AsyncTask<Void,Void,Void> {

    String LOG_TAG = "GCMHTTPTask";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Log.d(LOG_TAG +" RegID", TSettings.get(TSettings.REG_ID));
            String encodedRegID = URLEncoder.encode(TSettings.get(TSettings.REG_ID), "UTF-8");
            String phonenumber = "111111";
            //URL url = new URL("http://192.168.1.242:3000/send_reg_id/"+ phonenumber + "/"+ encodedRegID);
            URL url = new URL("http://"+TSettings.get(TSettings.NODE_HOST)+":"+TSettings.get(TSettings.NODE_PORT)+"/send_reg_id/"+ phonenumber + "/"+ encodedRegID);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            Log.d(LOG_TAG, "Ended");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
