package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

public class MainActivity extends ActionBarActivity {

    //Инициализация строковой переменной логирования
    final static String LOG_TAG = "TFORWARD.MainActivity";
    //Объект передачи сообщения
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";


    //инициализируем наши объекты формы
    //Button btnSingIn = (Button) findViewById(R.id.singin);
    EditText etName, etPass;

    TPostData pd;
    ProgressDialog progress;
    //ProgressBar progressBar;

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

        // Для тестового сервера
        TSettings.set(TSettings.SERVER_HOST, "192.168.1.253");
        TSettings.set(TSettings.SERVER_PORT, "8170");

        //получаем идентификаторы точки доступа и пароль
        etName = (EditText) findViewById(R.id.epid);
        etPass = (EditText) findViewById(R.id.epass);

        //для прогрессбара
        //progressBar = (ProgressBar) findViewById(R.id.progressMain);
      /*  progress = new ProgressDialog(this);
        progress.setMessage("Получение настроек ...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/


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


    public  void sendMessage(View view){

        SingIn(etName.getText().toString(), etPass.getText().toString());



        //progressBar.setVisibility(View.VISIBLE);
       /* progress.show();

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                SingIn(etName.getText().toString(), etPass.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        progress.dismiss();
                    }
                });
            }
        }).start();*/
    }


    public void SingIn(String pointid, String password){

        Log.d(LOG_TAG,"SingIn started ...");

        pd = new TPostData(this);
        pd.pointID = pointid;
        pd.password = password;

        try{

            TParseOperators parse = new TParseOperators(this);
            String responseStr = pd.execute().get();

            parse.GetXMLSettings(responseStr);
            Log.d(LOG_TAG, responseStr);


            if (responseStr.length() > 0){
                // Создаем объект Intent для вызова новой Activity
                Intent intent = new Intent(this, MainAccessActivity.class);

                intent.putExtra(EXTRA_MESSAGE, responseStr);
                //progressBar.setVisibility(View.GONE);
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
                //progressBar.setVisibility(View.GONE);
            }
        }
        catch(Exception e)
        {
            //progressBar.setVisibility(View.GONE);
            Log.d(LOG_TAG,e.getMessage());
            e.printStackTrace();
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

        }
        catch(Exception e)
        {
            return false;
        }
        finally {
            if(checkDB != null) checkDB.close();
        }
    }

    public boolean checkForTables(){

        SQLiteOpenHelper dbHelper = null;

        try {
            dbHelper = new DatabaseHelper(this);
            SQLiteDatabase  db = null;
            Cursor      cursor = null;

            try {

                db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM pg LIMIT 1", null);
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

    class SingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected Void doInBackground(String... et) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
        }
    }

}
