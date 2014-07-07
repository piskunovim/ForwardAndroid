package ru.forwardmobile.tforwardpayment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.network.ServerRequestFactory;
import ru.forwardmobile.util.http.Converter;
import ru.forwardmobile.util.http.IRequest;

/**
 * Created by PiskunovI on 12.05.14.
 */
public class TPostData extends AsyncTask<String, String, String> {
        //private Context context;
        String pointID;
        String password;
        final String LOG_TAG = "TFORWARD.TPostData";
        ProgressDialog progress;

        public TPostData(Context ctx) {
            //context = ctx;
            progress = new ProgressDialog(ctx);
            progress.setMessage("Получение настроек ...");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
            //
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                StringBuilder builder = new StringBuilder();
                builder.append("command=JT_EXPORT_CONFIGURATION")
                        .append("&pointid=" + pointID)
                        .append("&password=" + password);

                IRequest request = ServerRequestFactory.getRequest(builder.toString());
                HttpTransport transport = new HttpTransport();

                byte[] resp = transport.send(request);
                return Converter.toUnicode(resp);
            } catch (ClientProtocolException e) {
                Log.d("ClientProtocolException:", e.getMessage());
            } catch (IOException e) {
                Log.d("IOException:", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss();
//
        }

}
