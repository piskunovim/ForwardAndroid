package ru.forwardmobile.tforwardpayment.security;

/**
 * @author Vasiliy Vanin
 */
public interface IKeyStorage {
    public static final String    PUBLIC_KEY_TYPE          = "public";
    public static final String    SECRET_KEY_TYPE          = "secret";    
    
    public byte[]   getKey(String type);
    public void     setKey(String type, byte[] key);
}
