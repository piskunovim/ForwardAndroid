package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
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

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

public class MainActivity extends ActionBarActivity {

    //Инициализация строковой переменной логирования
    final static String LOG_TAG = "TTestActivity.MainActivity";
    //Объект передачи сообщения
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";


    //инициализируем наши объекты формы
    //Button btnSingIn = (Button) findViewById(R.id.singin);
    EditText etName, etPass;

    boolean databaseExists;
    boolean datatablesFull;

    TPostData pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Для тестового сервера
    //    TSettings.set(TSettings.SERVER_HOST,"192.168.1.253");
    //    TSettings.set(TSettings.SERVER_PORT, "8170");

        //получаем идентификаторы точки доступа и пароль
        etName = (EditText) findViewById(R.id.epid);
        etPass = (EditText) findViewById(R.id.epass);

        databaseExists = checkDataBase();
        datatablesFull = checkForTables();


        if (databaseExists && datatablesFull)
        {
            // Чтение настроек
            DatabaseHelper helper = new DatabaseHelper(this);
            helper.readSettings();
            helper.close();

            Intent intent = new Intent(this, MainListActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "true");
            // запуск activity
            startActivity(intent);
            this.finish();
        }
        else
        {
            Log.d(LOG_TAG, "Does not exist database");
        }

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }


    public  void sendMessage(View view){
        SingIn(etName.getText().toString(), etPass.getText().toString());
    }


    public void SingIn(String pointid, String password){

        pd = new TPostData();
        pd.pointID = pointid;
        pd.password = password;

        try{

            TParseOperators parse = new TParseOperators(this);
            String responseStr = pd.execute().get();

            //parse.GetXMLSettings(responseStr, dbHelper);
            Log.d(LOG_TAG,responseStr);

            if (responseStr.length() > 0){
                // Создаем объект Intent для вызова новой Activity
                Intent intent = new Intent(this, MainListActivity.class);

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
        catch(Exception e)
        {
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
            checkDB = SQLiteDatabase.openDatabase("/data/data/ru.forwardmobile.tforwardpayment/databases/forward", null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null ? true : false;
    }

    public boolean checkForTables(){
        SQLiteOpenHelper dbHelper;
        boolean hasTables = false;

        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pg", null);

        if(cursor.getCount() > 0){
                hasTables=true;
         }

        cursor.close();
        dbHelper.close();

        return hasTables;
    }

    private void startPaymentService() {

    }
}
