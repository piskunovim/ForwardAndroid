package ru.forwardmobile.tforwardpayment.spp.impl;

import android.util.Log;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

public class CommandCancelImpl extends CommandImpl {

    public CommandCancelImpl(IPayment payment) {
        super(ICommand.CANCEL, payment);
    }

    public String getLine() {
        StringBuffer request = new StringBuffer();
        request.append("command=JT_CANCEL");
        if ( payment.getTransactionId() != null ) {
            request.append("&transactionid=" + payment.getTransactionId().intValue());
        }
        return request.toString();
    }

    public void processResponseImpl(ICommandResponse response) {
        if ( response.getDone() != null ) {
            if ( response.getDone().intValue() == 1 ) {
                payment.setStatus(IPayment.CANCELLED);
            }
        } else {
            Log.w(" ", "CANCEL command: missed 'done'!");
        }
        setTerminalStatus(response.getStatus());
    }
}