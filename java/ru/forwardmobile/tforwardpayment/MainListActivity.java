package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.reports.BalanceActivity;
import ru.forwardmobile.tforwardpayment.reports.PaymentListActivity;

/**
 * @deprecated
 */
public class MainListActivity extends ActionBarActivity {

    final static String LOG_TAG = "TFORWARD.MainListActivity";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";


    ArrayList<String> operatorgroup = new ArrayList<String>();

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "Main list resumed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlist);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if(message != null) {
            initialize(message);
        }
    }

    private void initialize(String message) {

        Log.d(LOG_TAG, message);
        ListView listContent = (ListView)findViewById(R.id.listView);

        Log.v(LOG_TAG, "Loading settings");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.readSettings();
        dbHelper.close();

        GenerateListView(DatabaseHelper.PG_TABLE_NAME, "name", listContent);

        listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                SQLiteOpenHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT id FROM " + DatabaseHelper.PG_TABLE_NAME + " WHERE TRIM(name) = '"+name.trim()+"'", null);
                try {
                    c.moveToNext();
                    Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id + ", name = " + name + ", gid = " + c.getString(c.getColumnIndex("id")));
                    OpenOperatorActivity(c.getString(c.getColumnIndex("id")));
                } finally {
                    c.close();
                    db.close();
                    dbHelper.close();
                }
            }
        });

        // Payment queue start
        startPaymentQueue();
    }


    public void OpenOperatorActivity(String id){

        Log.d(LOG_TAG, "OpenOperatorActivity id: " + id);
        //Intent intent = new Intent(this, OperatorsActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, id);

        Intent intent = new Intent(this, OperatorsMenuActivity.class);
        intent.putExtra("node", Integer.valueOf(id));

        // запуск activity
        startActivity(intent);
    }

    public void GenerateListView(String table, String column, ListView listContent){

        SQLiteOpenHelper dbHelper = new DatabaseHelper(this);
        Log.v(LOG_TAG, "--- table: "+ table + " ---");
        Log.v(LOG_TAG, "--- column: "+ column + " ---");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.v(LOG_TAG, "--- Got rows in pg table: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        operatorgroup.clear();
        Cursor listpgc = null ;

        try {

            listpgc =  db.query(table, null, null, null, null, null, null);

            // ставим позицию курсора на первую строку выборки
            // если в выборке нет строк, вернется false
            if (listpgc.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int nameColIndex = listpgc.getColumnIndex(column);
                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.v(LOG_TAG, ", " + column + " = " + listpgc.getString(nameColIndex));
                    operatorgroup.add(listpgc.getString(nameColIndex));
                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (listpgc.moveToNext());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, operatorgroup);
                listContent.setAdapter(adapter);

            } else {
                Log.d(LOG_TAG, "Got 0 rows");
            }
        } finally {
            listpgc.close();
            db.close();
            dbHelper.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Intent intent = new Intent(MainListActivity.this, BalanceActivity.class);
            MainListActivity.this.startActivity(intent);
            return true;
        }
        if (id == R.id.action_report) {
            CharSequence reports[] = new CharSequence[] {"Запрос остатка средств", "Текущие платежи", "Принятые платежи"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Выберите отчет:");
            builder.setItems(reports, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on reports[which]
                    Intent intent;
                    switch (which){
                    case 0:
                           intent = new Intent(MainListActivity.this, BalanceActivity.class);
                           MainListActivity.this.startActivity(intent);
                           break;
                    case 1:
                           intent = new Intent(MainListActivity.this, PaymentListActivity.class);
                           intent.putExtra(PaymentListActivity.TYPE_EXTRA_PARAMETER, PaymentListActivity.LIST_UNPROCESSED);
                           MainListActivity.this.startActivity(intent);
                           break;
                    case 2:
                           intent = new Intent(MainListActivity.this, PaymentListActivity.class);
                           intent.putExtra(PaymentListActivity.TYPE_EXTRA_PARAMETER, PaymentListActivity.LIST_ALL);
                           MainListActivity.this.startActivity(intent);
                           break;
                    default:
                            break;
                   }
                }
            });
            builder.show();
            return true;
        }

        if (id == R.id.settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startPaymentQueue() {
        Log.i(LOG_TAG, "Starting payment queue...");
        startService(new Intent(this, TPaymentService.class));
    }

    private void stopPaymentQueue() {
        Log.i(LOG_TAG,"Deactivating payment queue...");
        stopService(new Intent(this,TPaymentService.class));
    }

    private void onExit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT", "true");
        startActivity(intent);

        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopPaymentQueue();
        DatabaseHelper helper = new DatabaseHelper(this);
        helper.saveSettings();
        helper.close();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Выйти из приложения?")
            .setMessage("Вы действительно хотите выйти?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    onExit();
                }
            }).create().show();
    }

}

