package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.reports.PaymentListImpl;
import ru.forwardmobile.tforwardpayment.reports.TBalanceReportScreenImpl;

public class MainListActivity extends ActionBarActivity {

    final static String LOG_TAG = "TFORWARD.MainListActivity";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

    DatabaseHelper dbHelper;
    ArrayList<String> operatorgroup = new ArrayList<String>();

    ProgressDialog progress;
    //ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlist);



        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        ListView listContent = (ListView)findViewById(R.id.listView);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        //progressBar = (ProgressBar) findViewById(R.id.progressMainList);
        //progressBar.setVisibility(View.VISIBLE);
        Log.d(LOG_TAG, message);
        dbHelper = new DatabaseHelper(this);

        if (!message.equals("true"))
        {
            //dbHelper = new DatabaseHelper(this);
            TParseOperators parse = new TParseOperators(this);
            parse.GetXMLSettings(message, dbHelper);

        } else {
            dbHelper.readSettings();
        }

        GenerateListView("pg", "name", listContent);
        progress.dismiss();
       // progressBar.setVisibility(View.GONE);

        listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                progress.show();
                //progressBar.setVisibility(View.VISIBLE);
                String name = (String) parent.getItemAtPosition(position);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT id FROM pg WHERE TRIM(name) = '"+name.trim()+"'", null);
                c.moveToNext();
                Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id + ", name = " + name + ", gid = "+ c.getString(c.getColumnIndex("id")));

                OpenOperatorActivity(c.getString(c.getColumnIndex("id")));
                //OperatorsListView(c.getString(c.getColumnIndex("id")));//, oplistContent);
            }

        });


        // Debug
        for(Object key: TSettings.getKeys()) {
            Log.v(LOG_TAG, "Set " + key + ": " + TSettings.get((String) key));
        }

        // Payment queue start
        startPaymentQueue();
    }

    public void OpenOperatorActivity(String id){
        Log.d(LOG_TAG, "OpenOperatorActivity id: " + id);
        Intent intent = new Intent(this, OperatorsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        progress.dismiss();
        //progressBar.setVisibility(View.GONE);
        // запуск activity
        startActivity(intent);
    }

    public void GenerateListView(String table, String column, ListView listContent){
        Log.d(LOG_TAG, "--- table: "+ table + " ---");
        Log.d(LOG_TAG, "--- column: "+ column + " ---");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "--- Got rows in pg table: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        operatorgroup.clear();
        Cursor listpgc = db.query(table, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (listpgc.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int nameColIndex = listpgc.getColumnIndex(column);




            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.v(LOG_TAG,  ", "+column+" = " + listpgc.getString(nameColIndex));
                operatorgroup.add(listpgc.getString(nameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (listpgc.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, operatorgroup);
            listContent.setAdapter(adapter);
        } else
            Log.d(LOG_TAG, "Got 0 rows");

        listpgc.close();
        dbHelper.close();


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
            Intent intent = new Intent(MainListActivity.this, TBalanceReportScreenImpl.class);
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
                           intent = new Intent(MainListActivity.this, TBalanceReportScreenImpl.class);
                           MainListActivity.this.startActivity(intent);
                           break;
                    case 1:
                           intent = new Intent(MainListActivity.this, PaymentListImpl.class);
                           intent.putExtra(EXTRA_MESSAGE, "0");
                           MainListActivity.this.startActivity(intent);
                           break;
                    case 2:
                           intent = new Intent(MainListActivity.this, PaymentListImpl.class);
                           intent.putExtra(EXTRA_MESSAGE, "1");
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
        Log.i(LOG_TAG,"Starting payment queue...");
        startService(new Intent(this, TPaymentService.class));
    }

    private void stopPaymentQueue() {
        Log.i(LOG_TAG,"Deactivating payment queue...");
        stopService(new Intent(this,TPaymentService.class));
    }

    @Override
    protected void onDestroy() {

            stopPaymentQueue();

            DatabaseHelper helper = new DatabaseHelper(this);
            helper.saveSettings();
            helper.close();

        super.onDestroy();
    }




}
