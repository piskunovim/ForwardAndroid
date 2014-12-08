package ru.forwardmobile.tforwardpayment.messages;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

/**
 * Created by PiskunovI on 03.12.2014.
 */
public class MessageParser extends AsyncTask<Void,Void,Void> {

    final Context context;

    public MessageParser(Context _context){
        this.context = _context;
    }

    String LOG_TAG = "MessageParser";
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //URL url = new URL("http://192.168.1.242:3000/get_notifications_queue/"+ TSettings.get("regid")); //в качестве параметра reg_id
            URL url = new URL("http://forwardmobile.ru:3000/get_notifications_queue/"+ TSettings.get("regid")); //в качестве параметра reg_id
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            Log.d(LOG_TAG, total.toString());

            if (!total.toString().equals("Нет новых записей"))
            {

            Gson gson = new Gson();

            NotifyMessage[] notifyType  = gson.fromJson(total.toString(), NotifyMessage[].class);

            ContentValues cv  = new ContentValues();

            DatabaseHelper dbHelper = new DatabaseHelper(context);

            // подключаемся к БД
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            for (int i=0; i<notifyType.length; i++) {
                Log.d(LOG_TAG, gson.toJson(notifyType[i].id));
                Log.d(LOG_TAG, gson.toJson(notifyType[i].date));
                Log.d(LOG_TAG, gson.toJson(notifyType[i].text));
                cv.put("id", Integer.parseInt(gson.toJson(notifyType[i].id).substring(1, gson.toJson(notifyType[i].id).length() - 1)));
                cv.put("type", 2);
                cv.put("regDate", UnixTimeToString(gson.toJson(notifyType[i].date).substring(1, gson.toJson(notifyType[i].date).length() - 1)));
                cv.put("messageText", gson.toJson(notifyType[i].text).substring(1, gson.toJson(notifyType[i].text).length() - 1));
                db.insert(DatabaseHelper.MESSAGES_TABLE_NAME, null, cv);
                cv.clear();
             }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class NotifyMessage{
        String id;
        String date;
        String text;
    }


    public String UnixTimeToString(String dateIntString)
    {
        Integer unixSeconds = Integer.parseInt(dateIntString);
        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        String formattedDate = sdf.format(date);
        Log.d(LOG_TAG, formattedDate);
        return formattedDate;
    }
    /*class CustomDeserializer implements JsonDeserializer {

        @Override
        public NotifyList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ArrayList<NotifyObject> result = new ArrayList<NotifyObject>();
            JsonObject object = json.getAsJsonObject();
            JsonArray array = object.getAsJsonArray("content");

            for (JsonElement element : array) {
                element.get
            }

            result.addAll(getElements(array));

            return new NotifyList(result);
        }
    }

    ArrayList<NotifyObject> getElements(JsonArray array){

        ArrayList<NotifyObject> result = new ArrayList<NotifyObject>();
        NotifyObject param;

        for (JsonElement element : array) {
            JsonObject object1 = element.getAsJsonObject();
            param = new NotifyObject();
            param.id = object1.get("id").getAsString();
            param.data = object1.get("data").getAsString();
            param.text = object1.get("text").getAsString();
            result.add(param);
        }

        return new ArrayList<NotifyObject>(result);
    }*/
}
