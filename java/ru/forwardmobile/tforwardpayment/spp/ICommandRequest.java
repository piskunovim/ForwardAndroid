package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;

public interface ICommandRequest {

    public void setRouter(IRouter router);
    public void addPayment(IPayment payment) throws Exception;
    public Collection<ICommand> getCommands();
    public Collection<String> getLines() throws Exception;
    public void setErrorDescription(String errorDescription);
    public void freePayments();
    public void setSended();

    public void setRequestSigned(boolean requestSigned);
    public boolean isRequestSigned();

    public void setResponseSigned(boolean responseSigned);
    public boolean isResponseSigned();

    public void setBody(String body);
    public String getBody();

    public void onError(String errorDescription);
    public void onTransportError(String errorDescription);
}
