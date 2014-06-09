package ru.forwardmobile.tforwardpayment.spp.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;

/**
 * Created by Mindbreaker on 08.06.14.
 */
public class CommandResponseImpl  implements ICommandResponse {

    private Integer transactionId = null;
    private Integer status = null;
    private Integer done = null;
    private Integer errorCode = null;
    private String errorDescription = null;
    private Integer errCode = null;
    private String errDescription = null;
    private Map<String,String> params = null;

    public CommandResponseImpl(String line) throws Exception {
        params = new HashMap<String,String>();
        // Log.trace("Creating response from \"" + line + "\" ...");
        char[] chars = line.toCharArray();
        StringBuffer valuePair = new StringBuffer();
        for(int i = 0; i < chars.length; ++i) {
            if ( chars[i] == '&' ) {
                if ( valuePair.length() > 0 ) {
                    parseValuePair(valuePair.toString());
                }
                valuePair = new StringBuffer();
            } else {
                valuePair.append(chars[i]);
            }
        }
        if ( valuePair.length() > 0 ) {
            parseValuePair(valuePair.toString());
        }
    }

    private void parseValuePair(String valuePair) throws Exception {
        // Log.trace("Parsing value pair \"" + valuePair + "\" ...");
        char[] chars = valuePair.toCharArray();
        StringBuffer name = new StringBuffer();
        StringBuffer value = null;
        for(int i = 0; i < chars.length; ++i) {
            if ( chars[i] == '=' ) {
                value = new StringBuffer();
            } else
            if ( value != null ) {
                value.append(chars[i]);
            } else {
                name.append(chars[i]);
            }
        }
        if ( ( name != null ) && ( name.length() > 0 ) && ( value != null ) && ( value.length() > 0 ) ) {
            setValuePair(name.toString(), URLDecoder.decode(value.toString(),"UTF-8"));
        }
    }

    private void setValuePair(String name, String value) {
        // Log.trace("+ name: " + name + ", value: " + value);
        String param = name.toLowerCase();
        if ( "transactionid".equals(param) ) {
            setTransactionId(value);
        } else
        if ( "status".equals(param) ) {
            setStatus(value);
        } else
        if ( "done".equals(param) ) {
            setDone(value);
        } else
        if ( "error_code".equals(param) ) {
            setErrorCode(value);
        } else
        if ( "err_code".equals(param) ) {
            setErrCode(value);
        } else
        if ( "error_descr".equals(param) ) {
            setErrorDescription(value);
        } else
        if ( "err_descr".equals(param) ) {
            setErrDescription(value);
        }

        params.put(param, value);
    }


    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : Integer.valueOf(transactionId);
    }

    public Integer getTransactionId() {
        return this.transactionId;
    }

    private void addStatus(int status) {
        if ( this.status == null ) {
            this.status = new Integer(status);
        } else {
            this.status = new Integer(this.status.intValue() | status);
        }
    }

    private void addStatus(String status) {
        //// Log.trace("* " + status);
        String statusComponent = status.toUpperCase();
        if ( "CHECKED".equals(statusComponent) ) {
            addStatus(CHECKED);
        } else
        if ( "VPS_CHECKED".equals(statusComponent) ) {
            addStatus(VPS_CHECKED);
        } else
        if ( "COMMITED".equals(statusComponent) ) {
            addStatus(COMMITED);
        } else
        if ( "VPS_COMMITED".equals(statusComponent) ) {
            addStatus(VPS_COMMITED);
        } else
        if ( "CANCELLED".equals(statusComponent) ) {
            addStatus(CANCELLED);
        } else
        if ( "FAILED".equals(statusComponent) ) {
            addStatus(FAILED);
        } else
        if ( "DONE".equals(statusComponent) ) {
            addStatus(DONE);
        }
    }

    public void setStatus(String status) {
        // Log.trace("** " + status);
        char[] statusChars = status.toCharArray();
        String statusComponent = null;
        for(int i = 0; i < statusChars.length; ++i) {
            switch (statusChars[i]) {
                case(','): {
                    if ( ( statusComponent != null ) && ( statusComponent.trim().length() > 0 ) ) {
                        addStatus(statusComponent);
                    }
                    statusComponent = null;
                }; break;
                case(' '): break;
                default: {
                    if ( statusComponent == null ) {
                        statusComponent = "" + statusChars[i];
                    } else {
                        statusComponent += statusChars[i];
                    }
                }
            }
        }
        if ( ( statusComponent != null ) && ( statusComponent.trim().length() > 0 ) ) {
            addStatus(statusComponent);
        }
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setDone(String done) {
        this.done = done == null ? null : Integer.valueOf(done);
    }

    public Integer getDone() {
        return this.done;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : Integer.valueOf(errorCode);
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : Integer.valueOf(errCode);
    }

    public Integer getErrCode() {
        return this.errCode;
    }

    public void setErrDescription(String errDescription) {
        this.errDescription = errDescription;
    }

    public String getErrDescription() {
        return this.errDescription;
    }



    public static void checkResponseDone(ICommandResponse response) throws Exception {
        if ( response == null ) {
            throw new Exception("Пустой ответ сервера!");
        }
        if ( response.getErrCode() != null ) {
            String err = PaymentImpl.getErrorName(response.getErrCode()) + " (" + response.getErrCode().toString();
            if ( response.getErrDescription() != null ) {
                err+= " " + response.getErrDescription();
            }
            throw new Exception("Ошибка: " + err + ")");
        }
        if ( response.getDone() == null ) {
            throw new Exception("Неизвестный ответ сервера");
        }

    }

    public String getParam(String name) {
        return (String)params.get(name.toLowerCase());
    }
}