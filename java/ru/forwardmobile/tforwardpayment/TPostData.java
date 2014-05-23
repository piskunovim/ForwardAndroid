package ru.forwardmobile.tforwardpayment;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.network.ServerRequestFactory;
import ru.forwardmobile.util.http.IRequest;
import ru.forwardmobile.util.http.IResponse;
import ru.forwardmobile.util.http.RequestFactory;

/**
 * Created by PiskunovI on 12.05.14.
 */
public class TPostData extends AsyncTask<String, String, String> {

        String pointID;
        String password;
        final String LOG_TAG = "TFORWARD.TPostData";

        public TPostData() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //
        }

        @Override
        protected String doInBackground(String... params) {
            // Создадим HttpClient и PostHandler
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://192.168.1.253:8170");
            HttpPost httppost = new HttpPost("http://www.forwardmobile.ru:8193");
            Log.d(LOG_TAG, "pointid: " + pointID + " password: " + password);


            try {
                // Добавим данные (пара - "название - значение")
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                /*nameValuePairs.add(new BasicNameValuePair("command", "JT_EXPORT_CONFIGURATION"));
                nameValuePairs.add(new BasicNameValuePair("pointid", pointID));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                //nameValuePairs.add(new BasicNameValuePair("pointid", "1197"));
                //nameValuePairs.add(new BasicNameValuePair("password", "123123"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));



                // Выполним запрос
                HttpResponse response = httpclient.execute(httppost);


                HttpEntity entity = response.getEntity();


                String responseStr = EntityUtils.toString(response.getEntity());
                int counto = responseStr.length();

                Log.d("ResponseContentLength", String.valueOf(response.getEntity().getContentLength()));
                Log.d("ResponseLength", String.valueOf(counto));
*/
                TSettings.set(TSettings.SERVER_HOST, "www.forwardmobile.ru");
                TSettings.set(TSettings.SERVER_PORT, "8193");
                StringBuilder builder = new StringBuilder();
                builder.append("command=JT_EXPORT_CONFIGURATION")
                        .append("&pointid=" + pointID)
                        .append("&password=" + password);

                IRequest request = ServerRequestFactory.getRequest(builder.toString());
                HttpTransport transport = new HttpTransport();
                byte[] resp = transport.send(request);
                return new String(resp);
                //return responseStr.toString();
            } catch (ClientProtocolException e) {
                // Ошибка :(
                Log.d("ClientProtocolException:", e.getMessage());
            } catch (IOException e) {
                // Ошибка :(
                Log.d("IOException:", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//
        }

}
