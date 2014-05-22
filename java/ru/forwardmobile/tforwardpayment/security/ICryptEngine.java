package ru.forwardmobile.tforwardpayment.security;

/**
 *
 * @author vaninv
 */
public interface ICryptEngine {
    public String   generateSignature(byte[] message) throws Exception;
    public String   generateSignature(String source) throws Exception;
    public void     verifySignature(byte[] message, byte[] signature) throws Exception;
}
