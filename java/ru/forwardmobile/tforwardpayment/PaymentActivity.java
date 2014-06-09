package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import ru.forwardmobile.tforwardpayment.actions.CheckTask;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;
import ru.forwardmobile.tforwardpayment.spp.ProviderFactory;


/**
 * @author Vasiliy Vanin
 */
public class PaymentActivity  extends Activity implements View.OnClickListener {

    static final String LOGGING_TAG = "TFORWARD.PAYMENTACTIVITY";

    Button      checkButton;
    Button      startButton;
    ViewGroup   layout; 
    ViewGroup   fields;
    
    EditText    valueField;
    EditText    fullValueField;
    
    IProvider   provider;
    Collection<IFieldInfo> fieldsInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState); 
        
        //  Debug
        TSettings.set(TSettings.CERTIFICATE_ACESS_ID, "1882");
        
        // Получаем ПС с полями
        provider = ProviderFactory.getProvider(getIntent().getIntExtra("psid", 453), this);
        //provider = ProviderFactory.mockProvider(this);
        Log.d("TForwardPayment.PaymentActivity", provider.getName());

        layout = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.default_data_enity_layout, null);
        fields = (ViewGroup) layout.findViewById(R.id.data_entry_fields_container);

        // Список полей для сервера
        fieldsInfo = new HashSet<IFieldInfo>();
        
        for(IField field: provider.getFields()) {
            fields.addView( field.getView() );
            fieldsInfo.add(field);
        }
        
        setContentView(layout);

        // Actions
        checkButton = (Button) findViewById(R.id.data_entry_button_check);
        checkButton.setOnClickListener(this);
        
        startButton = (Button) findViewById(R.id.data_entry_button_start);
        startButton.setOnClickListener(this);
    }

    public void onClick(View view) {

        Log.v(LOGGING_TAG, "Preparing payment.");

        valueField = (EditText) findViewById(R.id.data_entry_value_field);
        IPayment payment = null;

        try {
            payment = PaymentFactory.getPayment(provider.getId(),Double.valueOf(valueField.getText().toString()), fieldsInfo);
        }catch(NumberFormatException ex) {

            Toast.makeText(this, "Введите сумму нормально!", Toast.LENGTH_SHORT)
                    .show();

            Log.e(LOGGING_TAG, "Input error" + ex.toString());
            return;
        }

        if( view.getId() == R.id.data_entry_button_check ) {
            // CHECK
            Log.i(PaymentActivity.class.getName(), "CHECK");
            AsyncTask task = new CheckTask(this, payment);
            task.execute(new Void[]{});

            // @TODO Убрать impl-ы из фабрик и заменить их на прием из BaseField.fieldInfo();
        } else 
        if( view.getId() == R.id.data_entry_button_start ) {
            // START 
            Log.i(PaymentActivity.class.getName(), "START");
            payment.setStartDate(new Date());
            payment.setStatus(IPayment.NEW);



            try {

            }catch (Exception ex) {
                Log.e(LOGGING_TAG, "Payment err. " + ex.getMessage());
                Toast.makeText(this, "Ошибка создания платежа: " + ex.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}