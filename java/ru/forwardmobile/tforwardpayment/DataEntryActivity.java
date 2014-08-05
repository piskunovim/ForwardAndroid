package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;
import ru.forwardmobile.tforwardpayment.spp.impl.MenuItem;

/**
 * Created by Василий Ванин on 31.07.2014.
 */
public class DataEntryActivity extends Activity {

    public static final String PAYMENT_PARAMETER    = "paydx17";
    public static final String PS_PARAMETER         = "psidx3";

    protected OperatorsDataSource       dataSource  = null;
    protected ArrayAdapter              adapter     = null;
    protected List<IProviderMenuItem>   providers   = null;
    protected IPayment                  payment     = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managable_data_entry_layout);

        dataSource = new OperatorsDataSource(this);
        providers  = dataSource.getFullList();
        adapter    = new ArrayAdapter<IProviderMenuItem>(this, android.R.layout.simple_list_item_1, providers);

        Spinner selector = (Spinner) findViewById(R.id.mde_provider_selector);
                selector.setAdapter(adapter);

        // Now activity needs to determine provider for this data entry form
        // At first, it looking for id in PS_PARAMETER
        Integer providerId = getIntent().getIntExtra( PS_PARAMETER, 0);
        if(providerId == 0) {
            // Ar second, in PAYMENT_PARAMETER

        }
        // To find selected index in list we use dummy menu item
        // List uses hashCode method for search, so we don't need to specify the name of item
        Integer selectedIndex = providers.indexOf(MenuItem.getInstance(getIntent().getIntExtra( PS_PARAMETER,0), null));
        selector.setSelection(selectedIndex);

        onProviderSelected();
    }

    protected void onProviderSelected() {


    }
}
