package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;

import java.net.URLEncoder;

import ru.forwardmobile.tforwardpayment.operators.IProcessingAction;
import ru.forwardmobile.tforwardpayment.operators.IProcessor;
import ru.forwardmobile.tforwardpayment.operators.RequestBuilder;
import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.forwardmobile.tforwardpayment.spp.IProvidersDataSource;
import ru.forwardmobile.tforwardpayment.spp.IRequestBuilder;
import ru.forwardmobile.tforwardpayment.spp.ProvidersDataSourceFactory;

public class CommandStartImpl extends CommandImpl {

    public CommandStartImpl(IPayment payment, Context context) {
        super(ICommand.START, payment, context);
        this.context = context;
    }

    public String getLine() throws Exception {
        StringBuilder request = new StringBuilder();
        request.append("command=JT_START");


        IProvidersDataSource dataSource = ProvidersDataSourceFactory.getDataSource(context);
        IProvider            provider = dataSource.getById(payment.getPsid());
        IProcessor           processor = provider.getProcessor();

        IRequestBuilder      requestBuilder = RequestBuilderFactory.getRequestBuilder();
        request.append("&");
        request.append(requestBuilder.buildRequest(processor.getPaymentAction(), payment));

        if ( payment.getId() != null ) {
            request.append("&payment_id=" + payment.getId().intValue());
        }

        /*
        request.append("&value=" + URLEncoder.encode(payment.getValue().toString(), "UTF-8"));
        request.append("&value_sp=" + URLEncoder.encode(payment.getFullValue().toString(), "UTF-8"));
        if ( payment.getPsid() != null ) {
            request.append("&psid=" + payment.getPsid().intValue());
        }
        if ( payment.getId() != null ) {
            request.append("&payment_id=" + payment.getId().intValue());
        }



        StringBuilder xmlData = new StringBuilder();
        for(IField field: payment.getFields()) {
            if("target" . equals( field.getName() )) {
                request.append("&target=" + URLEncoder.encode(field.getValue().getValue(),"UTF-8"));
            } else {
                xmlData.append("<f n=\"" + field.getName() + "\">" + field.getValue().getValue() + "</f>");
            }
        }

        if(xmlData.length() > 0) {
            request.append("&xml_data=" + URLEncoder.encode("<data>" + xmlData.toString() + "</data>", "UTF-8"));
        }*/

        return request.toString();
    }

    public void processResponseImpl(ICommandResponse response) throws Exception {
        if ( response.getTransactionId() != null ) {
            // Log.trace("Setting transactionId to " + response.getTransactionId() + " ...");
            payment.setTransactionId(response.getTransactionId());
            payment.setStatus(IPayment.CHECKED);
            setTerminalStatus(response.getStatus());
        } else {
            // Log.trace("transactionid is absent in response");
            payment.setStatus(IPayment.FAILED);
            throw new Exception("Отсутствуют данные в ответе (transactionid)!");
        }
    }
}