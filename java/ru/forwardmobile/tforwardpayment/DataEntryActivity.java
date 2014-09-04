package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.forwardmobile.tforwardpayment.actions.CheckTask;
import ru.forwardmobile.tforwardpayment.spp.IFieldView;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.forwardmobile.tforwardpayment.spp.IProviderMenuItem;
import ru.forwardmobile.tforwardpayment.spp.PaymentDaoFactory;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;
import ru.forwardmobile.tforwardpayment.spp.PaymentQueueWrapper;
import ru.forwardmobile.tforwardpayment.spp.ProviderFactory;
import ru.forwardmobile.tforwardpayment.spp.impl.MenuItem;

/**
 * Created by Василий Ванин on 31.07.2014.
 */
public class DataEntryActivity extends AbstractBaseActivity implements View.OnClickListener {

    public static final String PAYMENT_PARAMETER    = "paydx17";
    public static final String PS_PARAMETER         = "psidx3";

    protected OperatorsDataSourceImpl dataSource  = null;
    protected IPaymentDao               paymentDao  = null;
    protected ArrayAdapter              adapter     = null;
    protected List<IProviderMenuItem>   providers   = null;
    protected IPayment                  payment     = null;
    protected List<IFieldView>              fields      = null;
    protected boolean                   loaded      = false;
    protected Spinner                   selector    = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        if(!loaded) {
            paymentDao = PaymentDaoFactory.getPaymentDao(this);
            dataSource = new OperatorsDataSourceImpl(this);
            providers = dataSource.getFullList();
            adapter = new ArrayAdapter<IProviderMenuItem>(this, android.R.layout.simple_list_item_1, providers);
        }

        // Now activity needs to determine provider for this data entry form
        // At first, it looking for id in PS_PARAMETER
        Integer providerId = getIntent().getIntExtra( PS_PARAMETER, 0);
        if(providerId == 0) {

            payment = paymentDao.find( getIntent().getIntExtra(PAYMENT_PARAMETER, 0) );
            if(payment == null) {
                throw new IllegalArgumentException("Payment or psid wasn't specified.");
            }

            providerId = payment.getPsid();
        }

        moveToFieldsView(providerId);
        onProviderSelected(providerId);
        loaded = true;


    }



    private void moveToFieldsView(Integer providerId) {

        setContentView(R.layout.data_entry_fields);

        ((Button) findViewById(R.id.mde_button_check)).setOnClickListener(this);
        ((Button) findViewById(R.id.mde_button_next)).setOnClickListener(this);

        selector = (Spinner) findViewById(R.id.mde_provider_selector);
        selector.setAdapter(adapter);

        // To find selected index in list we use dummy menu item
        // List uses hashCode method for search, so we don't need to specify the name of item
        Integer selectedIndex = providers.indexOf(MenuItem.getInstance(providerId, null));
        selector.setSelection(selectedIndex);
    }


    protected void onProviderSelected(Integer providerId) {

        IProvider provider = ProviderFactory.getProvider(providerId, this);

        if(payment == null) {
            payment = PaymentFactory.getPayment();
            payment.setPsid(providerId);
        }

        ViewGroup container = (ViewGroup) findViewById(R.id.mde_fields_container);
        fields = new ArrayList<IFieldView>();
        for(IFieldView field: provider.getFields())
        {
            IFieldView oldField = payment.getField(field.getName());
            if(oldField != null) {
                field.setValue(oldField.getValue());
            }

            container.addView(field.getView());
            fields.add(field);
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.mde_button_check) {

            payment.setFields(fields);
            payment.setValue(10d);

            try {
                (new CheckTask(this, payment))
                        .execute();
            }catch(Exception ex) {
                Toast.makeText(this,"Ошибка проверки " + ex.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        } else
        if( view.getId() == R.id.mde_button_next ) {

            // Packing fields to payment
            payment.setFields(fields);
            payment.setPsid( ((IProviderMenuItem) selector.getSelectedItem()).getId() );
            setContentView(R.layout.data_entry_payment);

            ((Button) findViewById(R.id.mde_button_back)).setOnClickListener(this);
            ((Button) findViewById(R.id.mde_button_start)).setOnClickListener(this);

            IProviderMenuItem item = (IProviderMenuItem) selector.getSelectedItem();

            ((TextView) findViewById(R.id.mde_provider_name))
                    .setText(item.getName());

            drawFieldsInfo((ViewGroup) findViewById(R.id.mde_fields_container), payment.getFields());
        } else
        if( view.getId() == R.id.mde_button_back ) {
            moveToFieldsView(payment.getPsid());
            onProviderSelected(payment.getPsid());
        } else
        if( view.getId() == R.id.mde_button_start ) {
            try {
                doPayment();
                Toast.makeText(this, "Платеж поставлен в очередь", Toast.LENGTH_SHORT)
                        .show();
                onBackPressed();

            } catch(Exception ex){
                Toast.makeText(this, "Ошибка. " + ex.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void doPayment() throws Exception {

        Double value;
        Double fullValue;

        try {
            TextView valueView = (TextView) findViewById(R.id.mde_value_value);
            value = Double.valueOf(valueView.getText().toString());
        } catch (Exception ex) {
            throw new Exception("Сумма к зачислению введена не верно!");
        }

        try {
            TextView fullValueView = (TextView) findViewById(R.id.mde_full_value_value);
            fullValue = Double.valueOf(fullValueView.getText().toString());
        } catch(Exception ex) {
            throw new Exception("Сумма с клиента введена не верно!");
        }

        if(value > fullValue) {
            throw new Exception("Сумма с клиента не может быть меньшу суммы к зачислению!");
        }

        payment.setValue(value);
        payment.setFullValue(value);

        PaymentQueueWrapper.getQueue().processPayment(payment);
    }

    private void drawFieldsInfo(ViewGroup container, Collection<IFieldView> fieldInfoCollection) {

        for(IFieldView field: fieldInfoCollection) {

            TextView label = new TextView(this);
            label.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            label.setText(field.getLabel());
            container.addView(label);

            TextView value = new TextView(this);
            value.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            value.setText(field.getValue());
            container.addView(value);
        }
    }


}
