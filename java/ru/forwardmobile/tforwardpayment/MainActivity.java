package ru.forwardmobile.tforwardpayment;

/*
 Главный экран приложения
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.IOException;

import ru.forwardmobile.tforwardpayment.files.FileOperationsImpl;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.network.ServerRequestFactory;
import ru.forwardmobile.tforwardpayment.operators.GetOperatorsXML;
import ru.forwardmobile.tforwardpayment.settings.TimeClass;
import ru.forwardmobile.util.DialogSignleton;
import ru.forwardmobile.util.android.ITaskListener;
import ru.forwardmobile.util.http.Converter;
import ru.forwardmobile.util.http.IRequest;

public class MainActivity extends ActionBarActivity implements EditText.OnEditorActionListener {

    static final String TEST_SERVER = "test";
    static final String REAL_SERVER = "online";

    //Инициализация строковой переменной логирования
    final static String LOG_TAG = "TFORWARD.MainActivity";

    ProgressDialog progressDialog = null;

    //ID проекта для GCM
    static final String SENDER_ID = "421740259735";

    AsyncTask<Void, Void, Void> mRegisterTask;

    //Определяем статическую переменную для точки
    public static String point;


    EditText etName, etPass;
    Button btnSignIn;
    AuthenticationTask at;

    FileOperationsImpl foi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foi = null;

        setToLog("Application started");

        // Если запросили выход
        if("true" . equals( getIntent().getStringExtra("EXIT"))) {
            super.onDestroy();
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());

        } else {
            initialize();
        }

        if (DialogSignleton.getTaskRunning()){
          showDialog();
        }
    }

    private void initialize() {
        setContentView(R.layout.activity_main);

        applyFonts(findViewById(R.id.activity_main_container), null);

        //задаем найстройки работы сервера
        getServerParams(REAL_SERVER);

        foi = null;

        setToLog( "Initialization | Server: " + TEST_SERVER);

        //Получаем идентификаторы точки доступа и пароль
        etName = (EditText) findViewById(R.id.epid);
        etPass = (EditText) findViewById(R.id.epass);

        btnSignIn = (Button) findViewById(R.id.singin);

        etName.setOnEditorActionListener(this);
        etPass.setOnEditorActionListener(this);

        if ( Settings.getInt(this, Settings.isAuthenticated, 0) > 0 ) {
            Intent intent = new Intent(this, MainAccessActivity.class);

            startActivity(intent);
            this.finish();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn(etName.getText().toString(), etPass.getText().toString());
            }
        });

    }


    public void SignIn(String pointid, String password){

        showDialog();
        DialogSignleton.setTaskRunning(true);
        at = new AuthenticationTask();
        at.pointID = pointid;
        at.password = password;
        at.execute();
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста подождите...");
        progressDialog.show();

        DialogSignleton.setDialog(progressDialog);
    }


    private void reportError(){


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Ошибка авторизации")
                .setMessage("Внимание! Произошла ошибка авторизации на сервере ForwardMobile. Пожалуйста. " +
                        "проверьте наличие на вашем устройстве доступа к сети интернет и правильность вводимых данных.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }


    /**
     * Check if the database exist
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(
                    getFilesDir().getParent() + "/databases/forward",
                    null, SQLiteDatabase.OPEN_READONLY);

            return checkDB != null;
        }catch(Exception ex) {
            return false;
        } finally {
            if(checkDB != null) checkDB.close();
        }
    }



    //Регистрация устройства GCM
    public void RegDevice(final String pointid) throws Exception {

       Log.d(LOG_TAG, "RegDevice started...");
       Log.d(LOG_TAG, "pointid: "+ pointid);

       point = pointid;

        //Убеждаемся, что устройство имеет соответствующие зависимости.
        GCMRegistrar.checkDevice(this);

        // Убеждаемся, что манифест был правильно настроен - закомментируйте эту строку
        // при разработке приложения. Раскомментируйте ее, когда оно будет готово.
        GCMRegistrar.checkManifest(this);

        // Получить GCM регистрационный id
        final String regId = GCMRegistrar.getRegistrationId(this);

        Log.d(LOG_TAG,"regId = " + regId);

        // Проверям был ли regid уже предоставлен ранее
        if (regId.equals("")) {
            // Регистрации ранее небыло, регистрируем сейчас на GCM
            Log.d(LOG_TAG, "regId - new registration");
            Log.d(LOG_TAG, SENDER_ID);


            GCMRegistrar.register(this, SENDER_ID);


        } else {
            Log.d(LOG_TAG, "Устройство уже зарегистрированно на GCM");
            // Устройство уже зарегистрированно на  GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Пропустить этап регистрации.
                 Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Попробуем зарегистрироваться снова, но не в потоке пользовательского интерфейса.
                // Это также необходимо для отмены потока OnDestroy (),
                // следовательно используем AsyncTask вместо исходного потока.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Регистрируемся на нашем сервере
                        // На сервере создается новый пользователь

                        Log.d(LOG_TAG, "point = " + point);
                        ServerUtilities.register(context, point, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    /**
     * Получаем push-сообщения
     * */

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " +
                    e.getMessage());
        }

        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        super.onDestroy();
    }

    protected  void applyFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "Magistral.TTF");

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFonts(child, fontToSet);
                }
            } else if (v instanceof TextView) {
                ((TextView)v).setTypeface(fontToSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

    protected void getServerParams(String params){
        if (params.equals("online")) {
          //Для доступа к серверу извне
          Settings.set(this, Settings.SERVER_HOST, "www.forwardmobile.ru");
          Settings.set(this, Settings.SERVER_PORT, "8193");

          Settings.set(this, Settings.NODE_HOST, "192.168.1.6");
          Settings.set(this, Settings.NODE_PORT, "3000");
        }
        else{
          // Для тестового сервера
          Settings.set(this, Settings.SERVER_HOST, "192.168.1.253");
          Settings.set(this, Settings.SERVER_PORT, "8170");

          Settings.set(this, Settings.NODE_HOST, "192.168.1.242");
          Settings.set(this, Settings.NODE_PORT, "3000");
        }
    }


    protected class AuthenticationTask extends AsyncTask<String, String, String> {

        String pointID;
        String password;
        final String LOG_TAG = "TFORWARD.TPostData";

        AuthenticationParser parse;

        public AuthenticationTask() {

            parse = new AuthenticationParser(MainActivity.this);
        }


        @Override
        protected String doInBackground(String... params) {

            String error = AuthenticatePoint();

            if(error != null)
                return error;

            Log.d(LOG_TAG, "Authentication successful ...");

            try {
                RegDevice(pointID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(LOG_TAG, "Loading operators ...");
            error = loadOperatorsXML();


            return error;
        }

        @Override
        protected void onPostExecute(String result) {
            DialogSignleton.setTaskRunning(false);

            if(DialogSignleton.getDialog() != null && DialogSignleton.getDialog().isShowing()) {
                DialogSignleton.getDialog().dismiss();
            }

            if(result == null) {
                Intent intent = new Intent(MainActivity.this, MainAccessActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

            } else  {
               reportError();
            }
        }

        protected String AuthenticatePoint() {

            try {

                StringBuilder builder = new StringBuilder();
                builder.append("command=JT_EXPORT_CONFIGURATION")
                        .append("&pointid=" + pointID)
                        .append("&password=" + password);

                IRequest request = ServerRequestFactory.getRequest(builder.toString());
                HttpTransport transport = new HttpTransport();

                byte[] resp = transport.send(request, MainActivity.this);
                parse.loadSettings(Converter.toUnicode(resp));

            }catch (XmlPullParserException ex) {
                Log.d(LOG_TAG, "XMLPULLERR " + ex.getMessage());
                return "Ошибка авторизации.";
            }catch (Exception ex) {
                return "Ошибка отправки запроса: " + ex.getMessage();
            }

            Settings.set(MainActivity.this, Settings.POINT_ID, pointID);
            return null;
        }

        protected String loadOperatorsXML (){
            String Tag = "HTTPConnectionTag";
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet("http://"+ Settings.get(MainActivity.this, Settings.NODE_HOST)+":"+ Settings.get(MainActivity.this, Settings.NODE_PORT)+"/get_operators");

            String filename = "operators.xml";


            try {
                Log.d(Tag, "Запрос отправлен!");

                HttpResponse response = httpclient.execute(httpget);

                HttpEntity httpEntity = response.getEntity();

                String line = EntityUtils.toString(httpEntity, "UTF-8");
                Log.d(Tag, line);

                FileOutputStream outputStream = null;
                try {
                    outputStream = MainActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(line.getBytes());
                    outputStream.close();
                } finally {
                    if(outputStream != null)
                        try { outputStream.close(); }catch (Exception ex){}
                }
                return null;
            }
            catch (Exception e) {
                Log.d(Tag, "Запрос не отправлен!");
                e.printStackTrace();
                return "Ошибка получения списка операторов.";
            }
        }

    }

    void setToLog(String logMessage){
        foi = null;

        try {
            foi = new FileOperationsImpl(this);
            foi.writeToFile(new TimeClass().getFullCurrentDateString() + logMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

