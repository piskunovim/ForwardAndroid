package ru.forwardmobile.tforwardpayment.spp;

public interface ICommandResponse {

    public static final int CHECKED = 1;
    public static final int VPS_CHECKED = 2;
    public static final int COMMITED = 4;
    public static final int VPS_COMMITED = 8;
    public static final int CANCELLED = 16;
    public static final int FAILED = 32;
    public static final int DONE = 64;

    public void setTransactionId(String id);
    public Integer getTransactionId();

    public void setStatus(String status);
    public Integer getStatus();

    public void setDone(String done);
    public Integer getDone();

    public void setErrorCode(String errorCode);
    public Integer getErrorCode();

    public void setErrorDescription(String errorDescription);
    public String getErrorDescription();

    public void setErrCode(String errCode);
    public Integer getErrCode();

    public void setErrDescription(String errDescription);
    public String getErrDescription();

    public String getParam(String name);
}