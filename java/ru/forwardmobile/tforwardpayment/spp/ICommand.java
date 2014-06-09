package ru.forwardmobile.tforwardpayment.spp;

public interface ICommand {

    public static final int START = 0;
    public static final int COMMIT = 1;
    public static final int CHECK = 2;
    public static final int CANCEL = 3;
    public static final int CHECK_TARGET = 4;

    public int getType();
    public IPayment getPayment();
    public String getLine() throws Exception;
    public void processResponse(ICommandResponse response) throws Exception;
}