package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.reports.BalanceActivity;
import ru.forwardmobile.tforwardpayment.reports.PaymentListActivity;

public class OperatorsActivity extends ActionBarActivity {

    final static String LOG_TAG = "TTestActivity.OperatorsActivity";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

    SQLiteOpenHelper dbHelper;
    ArrayList<String> operatorgroup = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operators_activity);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Log.d(LOG_TAG, message);
        dbHelper = new DatabaseHelper(this);
        OperatorsListView(message);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.operators, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Intent intent = new Intent(OperatorsActivity.this, BalanceActivity.class);
            OperatorsActivity.this.startActivity(intent);
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
                            intent = new Intent(OperatorsActivity.this, BalanceActivity.class);
                            OperatorsActivity.this.startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(OperatorsActivity.this, PaymentListActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, "0");
                            OperatorsActivity.this.startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(OperatorsActivity.this, PaymentListActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, "1");
                            OperatorsActivity.this.startActivity(intent);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.operators_fragment, container, false);
            return rootView;
        }
    }

    public void OperatorsListView(String gid){//, ListView listContent){
        Log.d(LOG_TAG, "Start OperatorsListView");
        operatorgroup.clear();
        ListView listContent = (ListView)findViewById(R.id.listView);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor listpc = db.query("p", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (listpc.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            do {
                int gidColIndex = listpc.getColumnIndex("gid");
                int nameColIndex = listpc.getColumnIndex("name");
                Log.d(LOG_TAG, gid);
                Log.d(LOG_TAG, listpc.getString(listpc.getColumnIndex("gid")));
                Log.d(LOG_TAG, listpc.getString(listpc.getColumnIndex("name")));
                // получаем значения по номерам столбцов и пишем все в лог
                if (gid.equals(listpc.getString(listpc.getColumnIndex("gid"))))
                {
                    Log.d(LOG_TAG, listpc.getString(nameColIndex));
                    operatorgroup.add(listpc.getString(nameColIndex));
                }
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (listpc.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, operatorgroup);
            listContent.setAdapter(adapter);
            listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String name = (String) parent.getItemAtPosition(position);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cr = db.rawQuery("SELECT id FROM p WHERE TRIM(name) = '"+name.trim()+"'", null);
                    cr.moveToNext();
                    Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id + ", name = " + name + ", gid = "+ cr.getString(cr.getColumnIndex("id")));

                    PaymentActivity pa= new PaymentActivity();

                    //pa.SetOperatorId(Integer.parseInt(cr.getString(cr.getColumnIndex("id"))));
                    //pa.operator_id = cr.getColumnIndex("id");
                    //pa.ShowOperatorId();
                    //
                    //! Здесь будем передавать id
                    //
                    cr.getString(cr.getColumnIndex("id"));
                    Intent intent = new Intent(OperatorsActivity.this, PaymentActivity.class);
                    intent.putExtra("psid",Integer.parseInt(cr.getString(cr.getColumnIndex("id"))));
                    startActivity(intent);
                }
            });

        } else
            Log.d(LOG_TAG, "Got 0 rows");
        listpc.close();
        dbHelper.close();

    }

}
