package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gcm.GCMRegistrar;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

public class MainActivity extends ActionBarActivity implements EditText.OnEditorActionListener {

    //Инициализация строковой переменной логирования
    final static String LOG_TAG = "TFORWARD.MainActivity";
    static final String DISPLAY_MESSAGE_ACTION =
            "ru.forwardmobile.tforwardpayment.DISPLAY_MESSAGE";
    static final String SENDER_ID = "421740259735";
    //Объект передачи сообщения
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";
    AsyncTask<Void, Void, Void> mRegisterTask;
    AsyncTask<Void, Void, Void> mSendTask;
    TextView lblMessage;
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;

    //инициализируем наши объекты формы
    //Button btnSingIn = (Button) findViewById(R.id.singin);

    //определяем статическую переменную для точки
    public static String point;


    EditText etName, etPass;
    TPostData pd;

    //Для проверки соединения с сетью Интернет//СonnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Если запросили выход
        if("true" . equals( getIntent().getStringExtra("EXIT"))) {
            super.onDestroy();
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());

        } else {
            initialize();
        }
    }

    private void initialize() {
        setContentView(R.layout.activity_main);

        applyFonts( findViewById(R.id.activity_main_container) ,null);
       // applyBoldFonts( findViewById(R.id.activity_main_container_footer) ,null);

        // Для тестового сервера
        TSettings.set(TSettings.SERVER_HOST, "192.168.1.253");
        TSettings.set(TSettings.SERVER_PORT, "8170");

        //получаем идентификаторы точки доступа и пароль
        etName = (EditText) findViewById(R.id.epid);
        etPass = (EditText) findViewById(R.id.epass);

        etName.setOnEditorActionListener(this);
        etPass.setOnEditorActionListener(this);


        boolean databaseExists = checkDataBase();
        boolean datatablesFull = checkForTables();

        if (databaseExists && datatablesFull )
        {
            Intent intent = new Intent(this, MainAccessActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "true");
            // запуск activity
            startActivity(intent);
            this.finish();
        }
        else
        {
            Log.d(LOG_TAG, "Does not exist database");
        }
    }


    public void sendMessage(View view){
         //Intent intent = new Intent(this,MainPageActivity.class);
        Intent intent = new Intent(this,MainFooter.class);
        startActivity(intent);
        //SingIn(etName.getText().toString(), etPass.getText().toString());
    }


    public void SingIn(String pointid, String password){

        pd = new TPostData(this);
        pd.pointID = pointid;
        pd.password = password;
        pd.execute();
    }


    public void onSignIn(String responseStr) {

        Log.i(LOG_TAG, "Login result: " + responseStr);


        if (responseStr.length() > 0){
            try {
                // Создаем объект Intent для вызова новой Activity
                RegDevice(etName.getText().toString());
            }catch (Exception ex) {
                ex.printStackTrace();
            }           Intent intent = new Intent(this, MainAccessActivity.class);
            intent.putExtra(EXTRA_MESSAGE, responseStr);


            // запуск activity
            startActivity(intent);
            this.finish();

        } else {
            new AlertDialog.Builder(this)
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
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

    public boolean checkForTables(){

        SQLiteOpenHelper dbHelper = null;

        try {
            dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {

                db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.PG_TABLE_NAME + " LIMIT 1", null);
                return cursor.moveToNext();
            } finally {
                if(cursor != null) cursor.close();
                if(db != null) db.close();
            }

        } finally {
            if(dbHelper != null)
                dbHelper.close();
        }
    }

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

    protected  void applyBoldFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "MagistralBlackC.otf");

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

}