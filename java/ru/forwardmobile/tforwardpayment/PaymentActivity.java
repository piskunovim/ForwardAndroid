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
import java.util.HashSet;

import ru.forwardmobile.tforwardpayment.actions.CheckTask;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;
import ru.forwardmobile.tforwardpayment.spp.PaymentQueueWrapper;
import ru.forwardmobile.tforwardpayment.spp.ProviderFactory;


/**
 * @author Vasiliy Vanin
 */
public class PaymentActivity  extends Activity implements View.OnClickListener {

    private final static String LOGGER_TAG = "TFORWARD.PAYMENTACT";
    public  final static String PAYMENT_ID_PARAMETER = "payidx9";
    public  final static String PS_ID_PARAMETER      = "psidx1";

    Button      checkButton;
    Button      startButton;
    ViewGroup   layout; 
    ViewGroup   fields;
    
    EditText    valueField;
    EditText    fullValueField;
    
    IProvider   provider;
    Collection<IField> fieldsInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState); 

        // Получаем ПС с полями
        provider = ProviderFactory.getProvider(getIntent().getIntExtra(PS_ID_PARAMETER, 453), this);
        //provider = ProviderFactory.mockProvider(this);
        Log.d("TForwardPayment.PaymentActivity", provider.getName());

        layout = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.default_data_enity_layout, null);
        fields = (ViewGroup) layout.findViewById(R.id.data_entry_fields_container);

        // Список полей для сервера
        fieldsInfo = new HashSet<IField>();
        
        for( IField field: provider.getFields() ) {
            fields.addView( field.getView() );
            fieldsInfo.add(field);
        }
        
        setContentView(layout);

        // Actions
        checkButton = (Button) findViewById(R.id.data_entry_button_check);
        checkButton.setOnClickListener(this);

        startButton = (Button) findViewById(R.id.data_entry_button_start);
        startButton.setOnClickListener(this);

        valueField      = (EditText) findViewById(R.id.data_entry_value_field);
        fullValueField  = (EditText) findViewById(R.id.data_entry_full_value_field);
    }




    public void onClick(View view) {

        checkButton.setEnabled(false);
        startButton.setEnabled(false);

        if( view.getId() == R.id.data_entry_button_check ) {
            // CHECK
            Log.i(PaymentActivity.class.getName(), "CHECK");

            valueField = (EditText) findViewById(R.id.data_entry_value_field);
            try {
                IPayment payment = PaymentFactory.getPayment(provider.getId(), Double.valueOf(valueField.getText().toString()), null, fieldsInfo);
                AsyncTask task = new CheckTask(this, payment);
                task.execute((Object[]) new Void[]{});
            }catch(NumberFormatException ex) {
                Toast.makeText(this, "Сумма платежа введена неверно!", Toast.LENGTH_SHORT)
                        .show();
            }            
            
            // @TODO Убрать impl-ы из фабрик и заменить их на прием из BaseField.fieldInfo();
        } else 
        if( view.getId() == R.id.data_entry_button_start ) {
            // START 
            Log.i(PaymentActivity.class.getName(), "START");

            // Дело мы имеем в основном с неадекватами,
            // поэтому надо все проверять и все точно выводить
            String err = null;
            boolean hasError = false;
            Double value = null;
            Double fullValue = null;

            try {
               value =  Double.valueOf(valueField.getText().toString());
            } catch (Exception ex){
                Log.w(LOGGER_TAG, "Value err: " + ex.getMessage());
                err = "Сумма к зачислению указана неверно.";
                hasError = true;
            }

            try {
                fullValue = Double.valueOf(fullValueField.getText().toString());
            } catch(Exception ex) {
                Log.w(LOGGER_TAG, "Full value err: " + ex.getMessage());
                if(err != null) {
                    err += "\nСумма с клиента указана неверно.";
                } else {
                    err = "Сумма с клиента указана неверно.";
                }
                hasError = true;
            }

            if(fullValue < value) {
                if(err != null) {
                    err += "\nСумма с клиента не может быть меньше суммы к зачислению";
                } else {
                    err = "Сумма с клиента не может быть меньше суммы к зачислению";
                }

                hasError = true;
            }

            if(!hasError) {
                IPayment payment = PaymentFactory.getPayment(provider.getId(),value, fullValue, fieldsInfo);
                try {
                    Log.d(LOGGER_TAG, "Adding payment to queue");
                    PaymentQueueWrapper.getQueue().processPayment(payment);
                    Toast.makeText(this, "Платеж создан!", Toast.LENGTH_SHORT)
                            .show();

                    super.onBackPressed();
                }catch (Exception ex) {
                    Toast.makeText(this, "Ошибка создания платежа: " + ex.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Log.d(LOGGER_TAG, "Payment creation error...");
                Toast.makeText(this, err, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        checkButton.setEnabled(true);
        startButton.setEnabled(true);
    }

}
