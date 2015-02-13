package ru.forwardmobile.tforwardpayment.files;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.forwardmobile.tforwardpayment.Settings;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.settings.TimeClass;
import ru.forwardmobile.util.http.IRequest;
import ru.forwardmobile.util.http.IResponse;
import ru.forwardmobile.util.http.ITransport;
import ru.forwardmobile.util.http.RequestFactory;
import ru.forwardmobile.util.http.TransportFactory;

/**
 * Created by PiskunovI on 21.01.2015.
 */
public class FileOperationsImpl implements IFileOperations {

    /*
     В настоящий момент реализация данного класса отрабатывает случай ведения и отправки файлов логирования.
    */

    String LOG_TAG = "FileOperationsImpl";

    Context context;
    //File file;
    FileOutputStream fos;


    final static String currentDate = new TimeClass().getCurrentDateString();

    public FileOperationsImpl(Context context) throws IOException {
        this.context =  context;
        /*file = new File(currentDate);
        if(!file.exists()) {
            file.createNewFile();
        }*/
        Log.d(LOG_TAG, "FileOperationsImpl accessed");

        /*try {
            File dir = new File("logs");

            if (!dir.exists()) {
                dir.mkdirs();
            }
            Log.d(LOG_TAG, "FileLogPath: " + dir.getAbsolutePath());
            File file = new File(dir.getAbsolutePath(), currentDate);
            if(!file.exists()) {
                Log.d(LOG_TAG, "FileLogPath: " + file.getAbsolutePath());
                file.createNewFile();
            }



            fos = new FileOutputStream(file);


            //fos = this.context.openFileOutput(currentDate, Context.MODE_APPEND);

            fos.write(("").getBytes());

          //  fos.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        */

    }

    @Override
    public void serverConnection() {

    }


    public void writeToFile(String string) throws IOException {
        /*FileWriter fw = new FileWriter(currentDate, true);
        fw.write(string);
        fw.close();*/
        try {

            fos = this.context.openFileOutput(currentDate, Context.MODE_APPEND);

            fos.write(string.getBytes());

            fos.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Override
    public void sendFile(String fileName)
    {
        Log.d(LOG_TAG,"Filename(curTime): " + fileName);
        FileSendTask fst = new FileSendTask(this.context);
        fst.execute();
    }

    public class FileSendTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        public FileSendTask (Context context){
            mContext = context;
        }
        //other methods like onPreExecute etc.
        protected void onPostExecute() {
            Toast.makeText(mContext, "Log-файл отправлен успешно!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d(LOG_TAG,"Trying to send log-file");
                String url = "http://"+ Settings.get(context, Settings.NODE_HOST)+ ":" +
                                        Settings.get(context, Settings.NODE_PORT)+ "/get_logs";

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("pointid", Settings.get(context,Settings.POINT_ID)));
                nameValuePairs.add(new BasicNameValuePair("filename", currentDate));
                nameValuePairs.add(new BasicNameValuePair("log_text", getLogFromSql(context)));

                //использовалось когда лог хранился в текстовом файле
                //nameValuePairs.add(new BasicNameValuePair("log_text", readFile(currentDate)));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF8"));
                String response = client.execute(post, new BasicResponseHandler());
                Log.d(LOG_TAG, response);


                /* Этот запрос не получилось отправить и воспользовались стандартным Apache

                IRequest request = RequestFactory.getRequest_1_1();
                request.setMethod(IRequest.POST);
                request.addHeader("Content-Type: application/x-www-form-urlencoded");
                request.addHeader("User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:35.0) Gecko/20100101 Firefox/35.0");
                request.setHost(Settings.get(context, Settings.NODE_HOST));
                request.setPort(Settings.getInt(context, Settings.NODE_PORT));
                request.setTimeout(300);
                request.setPath("/get_logs");

                StringBuilder builder = new StringBuilder();
                builder.append('\n');
                builder.append('\n');
                builder.append("pointid=").append(Settings.get(context,Settings.POINT_ID));
                builder.append("&filename=").append(URLEncoder.encode(currentDate,"utf-8"));
                builder.append("&log_text=").append(URLEncoder.encode(readFile(currentDate),"utf-8"));

                request.setData(builder.toString().getBytes());

                Log.d(LOG_TAG, request.toString());

                ITransport transport = TransportFactory.getTransport();
                IResponse response = transport.send(request);

                String resp = new String(response.getData());
                */

                // Log.d(LOG_TAG,resp);
            }
            catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        protected String readFile(String filename){
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));

                String inputString;

                StringBuffer stringBuffer = new StringBuffer();

                while ((inputString = inputReader.readLine()) != null) {

                    stringBuffer.append(inputString + "\n");

                }

                return stringBuffer.toString();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return "";
        }

        protected String getLogFromSql(Context context){

            try{
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);

                DatabaseHelper dbHelper = new DatabaseHelper(context);

                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Log.d(LOG_TAG, Long.toString(today.getTime().getTime()));

                Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.LOGS_TABLE_NAME + " WHERE stamp >= " + today.getTime().getTime(), null);

                String log = "";

                c.moveToFirst();
                do{
                    Date date = new Date(c.getLong(c.getColumnIndex("stamp")));
                    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    log += formatter.format(date) + " : " + c.getString(c.getColumnIndex("message")) + "\n";
                }
                while(c.moveToNext());

                c.close();

                Log.d(LOG_TAG, log);

                return log;
                //select * FROM logs WHERE strftime('%Y-%m-%d', stamp / 1000, 'unixepoch');
            }
            catch(Exception e){

                e.printStackTrace();

            }

            return "";
        }
    }


}
