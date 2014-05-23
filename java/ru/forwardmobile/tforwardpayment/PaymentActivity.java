package ru.forwardmobile.tforwardpayment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.forwardmobile.tforwardpayment.spp.ProviderFactory;
import ru.forwardmobile.tforwardpayment.spp.impl.TextFieldImpl;


/**
 * @author Vasiliy Vanin
 */
public class PaymentActivity  extends Activity implements View.OnClickListener {
    
    Button      checkButton;
    Button      startButton;
    ViewGroup   layout; 
    ViewGroup   fields;
    
    EditText    valueField;
    EditText    fullValueField;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState); 
        
        // Получаем ПС с полями
        IProvider provider = ProviderFactory.getProvider(getIntent().getIntExtra("psid", 0), this);

        layout = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.default_data_enity_layout, null);
        fields = (ViewGroup) layout.findViewById(R.id.data_entry_fields_container);

        
        for(IField field: provider.getFields()) {
            fields.addView( field.getView() );
        }
        
        setContentView(layout);

        // Actions
        checkButton = (Button) findViewById(R.id.data_entry_button_check);
        checkButton.setOnClickListener(this);
        
        startButton = (Button) findViewById(R.id.data_entry_button_start);
        startButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        
        if( view.getId() == R.id.data_entry_button_check ) {
            // CHECK
            Log.i(PaymentActivity.class.getName(), "CHECK");
            
        } else 
        if( view.getId() == R.id.data_entry_button_start ) {
            // START 
            Log.i(PaymentActivity.class.getName(), "START");
            
        }
    }
}
