package ru.forwardmobile.tforwardpayment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Stack;

import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;

/**
 * Created by vaninv on 14.07.2014.
 */
public class OperatorsMenuActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static final String LOGGER_TAG = "TFORWARD.MENU";

    ListAdapter adapter = null;
    OperatorsDataSource dataSource = null;
    ListView listView = null;

    Stack<Integer> stack = new Stack<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operators_activity);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        dataSource = new OperatorsDataSource(this);
        showNode(getIntent().getIntExtra("nodep",0));
    }

    protected void showNode(Integer id) {
        stack.add(id);
        adapter = new ArrayAdapter<IProviderMenuItem>(this, android.R.layout.simple_list_item_1, dataSource.getMenuItems(id));

        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        IProviderMenuItem item = (IProviderMenuItem) adapter.getItem(i);
        if(item.isGroup())
            showNode(item.getId());
        //else
            // activity stub
    }
}
