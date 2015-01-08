package ru.forwardmobile.tforwardpayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.operators.IProcessingAction;
import ru.forwardmobile.tforwardpayment.operators.IProcessor;
import ru.forwardmobile.tforwardpayment.operators.RequestBuilder;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentQueue;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.forwardmobile.tforwardpayment.spp.IProvidersDataSource;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.tforwardpayment.spp.PaymentFactory;
import ru.forwardmobile.tforwardpayment.spp.PaymentQueueWrapper;
import ru.forwardmobile.tforwardpayment.spp.ProvidersDataSourceFactory;
import ru.forwardmobile.tforwardpayment.spp.impl.CommandRequestImpl;
import ru.forwardmobile.tforwardpayment.widget.FieldWidget;
import ru.forwardmobile.tforwardpayment.widget.FieldWidgetFactory;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Активность для проведения НОВОГО платежа
 * Created by Василий Ванин on 10.09.2014.
 */
public class DataEntryActivity extends AbstractBaseActivity implements View.OnClickListener, ITaskListener {

    private static final String LOGGING_KEY = DataEntryActivity.class.getName();

    public  static final String PS_PARAMETER = "psidx2";
    protected static final String VALUES_MAP = "vmapx8";

    // Старые значения полей, могут быть заполнены при повороте, или при коррекции платежа
    // Ключ - id поля, значение - реальное значение поля
    protected Map<Integer,String> savedValues = new HashMap<Integer, String>();
    protected IProvider provider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_entry_payment);

        Integer psid;

        // Пробуем достать ПС из параметров, либо из Bundle
        if(savedInstanceState == null) {

            psid = getIntent().getIntExtra(PS_PARAMETER, -1);
        } else {
            psid = savedInstanceState.getInt(PS_PARAMETER, -1);
            savedValues = (Map<Integer,String>) savedInstanceState.getSerializable(VALUES_MAP);
        }

        if(psid < 0) {
            throw new IllegalStateException("Ps parameter not found.");
        }

        // Загрузка информации о провайдере
        IProvidersDataSource providersDataSource = ProvidersDataSourceFactory.getDataSource(this);
        provider = providersDataSource.getById(psid);

        onProviderSelect();
    }

    /** Вызивается, когда выбран провайдер */
    protected void onProviderSelect() {

        Log.i(LOGGING_KEY, "Starting payment to provider: " + provider.getName());

        // Создаем поля для ввода
        createFieldView();

        // Вешаем обработчики на кнопочки
        createHandlers();
    }

    /** Вызывается, чтобы повесить обработчики на кнопки */
    protected void createHandlers() {

        View buttonCheck = findViewById(R.id.mde_button_check);
        buttonCheck.setOnClickListener(this);

        View buttonStart = findViewById(R.id.mde_button_start);
        buttonStart.setOnClickListener(this);
    }

    /** Вызывается для создания полей ввода */
    protected void createFieldView() {

        // Подставляем название провайдера
        ((TextView) findViewById(R.id.mde_provider_name))
                .setText(provider.getName());

        // Подготовка контейнера
        ViewGroup fieldGroup = (ViewGroup) findViewById(R.id.mde_fields_container);

        // Чистим все поля ввода
        fieldGroup.removeAllViews();

        // Создание и вывод полей
        for(IField field: provider.getFields()) {

            Log.i(LOGGING_KEY, "Rendering field: " + field.getName());

            FieldWidget widget = FieldWidgetFactory.createWidget(field, this);

            if(savedValues.containsKey(field.getId())) {
                widget.setValue(savedValues.get(field.getId()));
            }
            fieldGroup.addView(widget);
        }
    }

    /** Вызывается, когда нужно прочитать значения полей */
    protected Map<Integer, String> getFieldValues() {

        HashMap<Integer, String> values = new HashMap<Integer, String>();

        ViewGroup fieldGroup = (ViewGroup) findViewById(R.id.mde_fields_container);
        for(int i = 0; i < fieldGroup.getChildCount(); i++) {

            FieldWidget widget = (FieldWidget) fieldGroup.getChildAt(i);

            Log.i(LOGGING_KEY, "Widget Fileds: " + widget.getField().getName()  + " = " + widget.getValue().getValue());
            values.put(widget.getField().getId(), widget.getValue().getValue());
        }

        return values;
    }

    @Override
    public void onClick(View view) {
        if(R.id.mde_button_check == view.getId()) {

            // Получаем поля
            Map<Integer, String> valueMap = getFieldValues();

            // Создаем платеж
            IPayment payment = PaymentFactory.getPayment(DataEntryActivity.this);

            // Создаем поля для платежа
            Collection<IField> fields = provider.getFields();

            // Задаем значения полям
            for(IField field: fields) {
                field.setValue( valueMap.get(field.getId()) );
            }

            payment.setFields(fields);

            CheckTask task = new CheckTask(provider, payment, this, this);
            task.execute();
        } else
        if(R.id.mde_button_start == view.getId() ) {

            // Получаем поля
            Map<Integer, String> valueMap = getFieldValues();

            // Создаем платеж
            IPayment payment = PaymentFactory.getPayment(DataEntryActivity.this);

            // Создаем поля для платежа
            Collection<IField> fields = provider.getFields();

            // Задаем значения полям
            for(IField field: fields) {
                field.setValue( valueMap.get(field.getId()) );
            }

            payment.setFields(fields);

            // Платежная система
            payment.setPsid(provider.getId());

            // Суммы платежа
            payment.setValue( getPaymentValue() );
            payment.setFullValue( getPaymentFullValue() );

            IPaymentQueue queue = PaymentQueueWrapper.getQueue();

            // Добавляем в очередь
            try {
                queue.processPayment(payment);
                Toast.makeText(this, "Платеж поставлен в очередь.", Toast.LENGTH_LONG)
                        .show();

                Intent intent = new Intent(this, MainActivityFlat.class);
                startActivity(intent);

                this.finish();
            }catch (Exception ex) {
                Toast.makeText(this, "Ошибка: " + ex.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PS_PARAMETER, provider.getId());
        outState.putSerializable(VALUES_MAP, (Serializable) getFieldValues());
    }

    @Override
    public void onTaskFinish(Object result) {
        if(result instanceof IResponseSet) {
            IResponseSet responseSet = (IResponseSet) result;
            try {
                ICommandResponse response = responseSet.getResponses().get(0);
                String message;
                if( response.getDone() > 0) {
                    message = "Проверка прошла успешно.";
                } else {
                    message = "Ошибка проверки номера: " + response.getErrDescription();
                }

                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            }catch (Exception ex) {

                Toast.makeText(this, "Ошибка разбора запроса. " + ex.getMessage(), Toast.LENGTH_LONG)
                        .show();
                ex.printStackTrace();
                return;
            }
        } else {
            Toast.makeText(this, "Получен неожиданный ответ от сервера.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public Double getPaymentValue() {
        try {
            TextView valueView = (TextView) findViewById(R.id.mde_value_value);
            return Double.valueOf(valueView.getText().toString().trim());
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка введенной суммы", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
            return null;
        }
    }

    public Double getPaymentFullValue() {
        try {
            TextView valueView = (TextView) findViewById(R.id.mde_full_value_value);
            return Double.valueOf(valueView.getText().toString().trim());
        }
        catch (Exception ex){
                Toast.makeText(this, "Ошибка введенной суммы", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
                return null;
        }
    }

    private class CheckTask extends AbstractTask {

        final IProvider iProvider;
        final IPayment  iPayment;
        final Context   context;

        public CheckTask(IProvider iProvider, IPayment iPayment, Context ctx, ITaskListener listener) {
            super(listener, ctx);

            this.iPayment  = iPayment;
            this.iProvider = iProvider;
            this.context = ctx;
        }

        @Override
        protected Object doInBackground(Object... objects) {

            // Get processor
            IProcessor processor = iProvider.getProcessor();
            IProcessingAction action = processor.getCheckAction();
            RequestBuilder requestBuilder = new RequestBuilder();


            IResponseSet responseSet = null;

            try{
                ICommandRequest request = new CommandRequestImpl("command=JT_CHECK_TARGET&"
                        + requestBuilder.buildRequest(action,iPayment), true, true);

                HttpTransport transport = new HttpTransport();
                transport.setCryptEngine(new CryptEngineImpl(getContext()));
                responseSet = transport.send(request, context);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return responseSet;
        }
    }
}
