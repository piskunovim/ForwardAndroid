package ru.forwardmobile.tforwardpayment.spp.impl;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

public class CommandCommitImpl extends CommandImpl {

    public CommandCommitImpl(IPayment payment) {
        super(ICommand.COMMIT, payment);
    }

    public String getLine() {
        StringBuffer request = new StringBuffer();
        request.append("command=JT_COMMIT");
        if ( payment.getTransactionId() != null ) {
            request.append("&transactionid=" + payment.getTransactionId().intValue());
        }
        return request.toString();
    }

    public void processResponseImpl(ICommandResponse response) {
        if ( response.getDone() != null ) {
            if ( response.getDone().intValue() > 0 ) {
                payment.setStatus(IPayment.COMMITED);
                payment.delay(/*@TODO delay settings */ 10);
            }
        } else {
            payment.setErrorDescription("Неизвестный ответ севера (нет done)!");
            payment.errorDelay();
        }
        setTerminalStatus(response.getStatus());
    }
}
