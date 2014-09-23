package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

public class CommandCheckImpl extends CommandImpl {

    public CommandCheckImpl(IPayment payment, Context context) {
        super(ICommand.CHECK, payment, context);
    }

    public String getLine() {
        StringBuffer request = new StringBuffer();
        request.append("command=JT_CHECK");
        if ( payment.getTransactionId() != null ) {
            request.append("&transactionid=" + payment.getTransactionId().intValue());
        }
        return request.toString();
    }

    public void processResponseImpl(ICommandResponse response) throws Exception {
        if ( response.getStatus() != null ) {
            int status = response.getStatus().intValue();
            if ( ( status & ICommandResponse.DONE ) != 0 ) {
                payment.setStatus(IPayment.DONE);
            } else if ( ( status & ICommandResponse.FAILED ) != 0 ) {
                payment.setStatus(IPayment.FAILED);
            } else if ( ( status & ICommandResponse.CANCELLED ) != 0 ) {
                payment.setStatus(IPayment.CANCELLED);
            } else {
                payment.errorDelay();
            }
        } else {
            throw new Exception("Transaction status in response is null!");
        }
    }
}