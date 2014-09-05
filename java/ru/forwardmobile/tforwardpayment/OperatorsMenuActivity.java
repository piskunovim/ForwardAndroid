package ru.forwardmobile.tforwardpayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.util.Stack;

import ru.forwardmobile.tforwardpayment.operators.IOperatorsDataSource;
import ru.forwardmobile.tforwardpayment.operators.OperatorsXmlReader;
import ru.forwardmobile.tforwardpayment.operators.impl.OperatorsEntityManagerImpl;
import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;

/**
 * Created by vaninv on 14.07.2014.
 */
public class OperatorsMenuActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    protected static final String LOGGER_TAG = "TFORWARD.MENU";

    Integer currentNode;
    ListAdapter adapter = null;
    IOperatorsDataSource dataSource = null;
    ListView listView = null;

    Stack<Integer> stack = new Stack<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operators_activity);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        //@Todo убрать этот мусор отсюда
        try {
            InputStream is = getAssets().open("operators.xml");
            dataSource = new OperatorsXmlReader().readOperators(is);
        }catch(Exception ex) {}

        Log.i("MENUA", "Show: " + getIntent().getIntExtra("gid",0));
        showNode(getIntent().getIntExtra("gid",0));
    }

    protected void showNode(Integer id) {
        currentNode = id;
        adapter     = new ArrayAdapter<IProviderMenuItem>(this, android.R.layout.simple_list_item_1, dataSource.getMenuItems(id));
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        IProviderMenuItem item = (IProviderMenuItem) adapter.getItem(i);
        if(item.isGroup()) {
            stack.push(currentNode);
            showNode(item.getId());
        }
        else {
            startPayment(item);
        }
    }

    protected void startPayment(IProviderMenuItem item) {

        Log.i(LOGGER_TAG, "Starting payment to " + item.getName());
        startPayment(item.getId());

    }

    protected void startPayment(Integer item) {
        Intent intent = new Intent(this, DataEntryActivity.class);
        intent.putExtra(DataEntryActivity.PS_PARAMETER, item);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(stack.empty()) {
            super.onBackPressed();
        } else {
            Integer node = stack.pop();
            showNode(node);
        }
    }
}
