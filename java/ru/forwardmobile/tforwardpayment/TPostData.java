package ru.forwardmobile.tforwardpayment;

import android.app.ProgressDialog;
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

    ProgressDialog dialog;
    TParseOperators parse;
    MainActivity ctx;

    public TPostData(MainActivity ctx) {
        this.ctx = ctx;

        parse = new TParseOperators(ctx);
        dialog = new ProgressDialog(ctx);
        dialog.setTitle("Загрузка");
        dialog.setMessage("Пожалуйста ждите.");

    }

        @Override
        protected void onPreExecute() {

           // dialog.show();
            super.onPreExecute();
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
                parse.loadSettings(Converter.toUnicode(resp));

                return "OK";
            } catch (ClientProtocolException e) {
                Log.d("ClientProtocolException:", e.getMessage());
            } catch (IOException e) {
                Log.d("IOException:", e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //dialog.dismiss();
            ctx.onSignIn(result);
            super.onPostExecute(result);
        }

}
