package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IRouter;

public class CommandRequestImpl implements ICommandRequest {

    private IRouter router = null;
    private Collection<ICommand> commands = new ArrayList<ICommand>();

    private String body = null;
    private boolean requestSigned = false;
    private boolean responseSigned = false;

    private Context context = null;

    public CommandRequestImpl(String body) {
        this.body = body;
    }

    public CommandRequestImpl(boolean requestSigned, boolean responseSigned) {
        this.requestSigned = requestSigned;
        this.responseSigned = responseSigned;
    }

    public CommandRequestImpl(String body, boolean requestSigned, boolean responseSigned) {
        this(requestSigned, responseSigned);
        this.body = body;
    }

    public void setRouter(IRouter router) {
        this.router = router;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void addPayment(IPayment payment) throws Exception {
        // Log.debug("Payment " + payment.getId() + " was added to request.");
        ICommand command = CommandFactory.getCommand(this.router, payment, context);
        this.commands.add(command);
    }

    public void setErrorDescription(String errorDescription) {
        for(ICommand command: commands)
            command.getPayment().setErrorDescription(errorDescription);
            //try {
            //    PaymentDAO.store(command.getPayment());
            //} catch(Exception ex) {
                // Log.trace("Error saving payment:\n" + ex.getMessage());
            //}
    }

    public void onError(String errorDescription) {
        // Log.trace("Processing error ...");
        for(ICommand command: commands) {
            IPayment payment = command.getPayment();
            payment.setErrorDescription(errorDescription);
            if ( payment.getStatus() == IPayment.NEW ) {
                payment.setStatus(IPayment.FAILED);
                payment.setErrorCode(new Integer(1024));
                payment.setErrorDescription(errorDescription);
            } else {
                payment.errorDelay();
            }
            //try {
            //    PaymentDAO.store(command.getPayment());
            //} catch(Exception ex) {
                // Log.trace("Error saving payment:\n" + ex.getMessage());
            //}
        }
    }

    public void onTransportError(String errorDescription) {
        // Log.trace("Processing transport error ...");
        for(ICommand command: commands) {
            IPayment payment = command.getPayment();
            payment.setErrorDescription(errorDescription);
            if ( payment.getStatus() == IPayment.NEW ) {
                payment.incTryCount();
                if ( payment.getTryCount() >= TSettings.getInt(TSettings.MAXIMUM_START_TRY_COUNT, 10)) {
                    payment.setStatus(IPayment.FAILED);
                    payment.setErrorCode(new Integer(200));
                    payment.setErrorDescription(errorDescription);
                } else {
                    payment.delay(TSettings.getInt(TSettings.QUEUE_ERROR_DELAY, 600));
                }
            } else {
                payment.errorDelay();
            }

            //try {
            //    PaymentDAO.store(command.getPayment());
            //} catch(Exception ex) {
                // Log.trace("Error saving payment:\n" + ex.getMessage());
            //}
        }
    }

    public Collection<ICommand> getCommands() {
        return this.commands;
    }

    public Collection<String> getLines() throws Exception {
        // Log.trace("Creating request text ...");
        Collection<String> lines = new ArrayList<String>();
        for(ICommand command: commands)
                lines.add(command.getLine());

        return lines;
    }

    public void freePayments() {
       for(ICommand command: commands)
           command.getPayment().setActive(false);
    }

    public void setSended() {
        for(ICommand command: commands)
            command.getPayment().setSent(true);
    }

    public void setRequestSigned(boolean requestSigned) {
        this.requestSigned = requestSigned;
    }

    public boolean isRequestSigned() {
        return this.requestSigned;
    }

    public void setResponseSigned(boolean responseSigned) {
        this.responseSigned = responseSigned;
    }

    public boolean isResponseSigned() {
        return this.responseSigned;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }
}