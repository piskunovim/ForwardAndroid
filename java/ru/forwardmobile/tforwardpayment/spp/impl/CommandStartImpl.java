package ru.forwardmobile.tforwardpayment.spp.impl;

import java.net.URLEncoder;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

public class CommandStartImpl extends CommandImpl {

    public CommandStartImpl(IPayment payment) {
        super(ICommand.START, payment);
    }

    public String getLine() throws Exception {
        StringBuilder request = new StringBuilder();
        request.append("command=JT_START");

        request.append("&value=" + URLEncoder.encode(payment.getValue().toString(), "UTF-8"));
        request.append("&value_sp=" + URLEncoder.encode(payment.getFullValue().toString(), "UTF-8"));
        if ( payment.getPsid() != null ) {
            request.append("&psid=" + payment.getPsid().intValue());
        }
        if ( payment.getId() != null ) {
            request.append("&payment_id=" + payment.getId().intValue());
        }

        StringBuilder xmlData = new StringBuilder();
        for(IFieldInfo field: payment.getFields()) {
            if("target" . equals( field.getName() )) {
                request.append("target=" + URLEncoder.encode(field.getValue(),"UTF-8"));
            } else {
                xmlData.append("<f n=\"" + field.getName() + "\">" + field.getValue() + "</f>");
            }
        }

        if(xmlData.length() > 0) {
            request.append("&xml_data=" + URLEncoder.encode("<data>" + xmlData.toString() + "</data>", "UTF-8"));
        }

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