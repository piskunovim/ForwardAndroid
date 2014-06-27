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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.network.ServerRequestFactory;
import ru.forwardmobile.util.http.Converter;
import ru.forwardmobile.util.http.IRequest;

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
            // ???????? HttpClient ? PostHandler
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://"
                    + TSettings.get(TSettings.SERVER_HOST,"www.forwardmobile.ru"
                    + ":"
                    + TSettings.get(TSettings.SERVER_PORT,"8193")));

            Log.d(LOG_TAG, "pointid: " + pointID + " password: " + password);


            try {
                // ??????? ?????? (???? - "???????? - ????????")
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("command", "JT_EXPORT_CONFIGURATION"));
                nameValuePairs.add(new BasicNameValuePair("pointid", pointID));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                //nameValuePairs.add(new BasicNameValuePair("pointid", "1197"));
                //nameValuePairs.add(new BasicNameValuePair("password", "123123"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));



                // ???????? ??????
                HttpResponse response = httpclient.execute(httppost);


                HttpEntity entity = response.getEntity();


                String responseStr = EntityUtils.toString(response.getEntity());
                int counto = responseStr.length();

                Log.d("ResponseContentLength", String.valueOf(response.getEntity().getContentLength()));
                Log.d("ResponseLength", String.valueOf(counto));

                StringBuilder builder = new StringBuilder();
                builder.append("command=JT_EXPORT_CONFIGURATION")
                        .append("&pointid=" + pointID)
                        .append("&password=" + password);

                IRequest request = ServerRequestFactory.getRequest(builder.toString());
                HttpTransport transport = new HttpTransport();
                byte[] resp = transport.send(request);
                return Converter.toUnicode(resp);
                //return responseStr.toString();
            } catch (ClientProtocolException e) {
                // ?????? :(
                Log.d("ClientProtocolException:", e.getMessage());
            } catch (IOException e) {
                // ?????? :(
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
