package ru.forwardmobile.tforwardpayment.spp.impl;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IPayment;

public abstract class CommandImpl implements ICommand {

    private int type = -1;
    protected IPayment payment = null;

    public CommandImpl(int type, IPayment payment) {
        this.type = type;
        this.payment = payment;
    }

    public int getType() {
        return this.type;
    }

    public IPayment getPayment() {
        return this.payment;
    }

    public void processResponse(ICommandResponse response) throws Exception {
        if ( response == null ) {
            throw new Exception("Пустой ответ сервера!");
        }
        if ( response.getErrCode() != null ) {
            payment.setErrorCode(response.getErrCode());
            if ( response.getErrDescription() != null ) {
                payment.setErrorDescription(response.getErrDescription());
            }
            payment.setStatus(IPayment.FAILED);
        } else {
            processResponseImpl(response);
        }
        // PaymentDAO.store(payment);
    }

    public abstract void processResponseImpl(ICommandResponse response) throws Exception;

    public void setTerminalStatus(Integer s) {
        if(s != null) {
            int status = s.intValue();
            if ( ( status & ICommandResponse.DONE ) != 0 ) {
                payment.setStatus(IPayment.DONE);
            } else
            if ( ( status & ICommandResponse.FAILED ) != 0 ) {
                payment.setStatus(IPayment.FAILED);
            } else
            if ( ( status & ICommandResponse.CANCELLED ) != 0 ) {
                payment.setStatus(IPayment.CANCELLED);
            }
        }
    }
}