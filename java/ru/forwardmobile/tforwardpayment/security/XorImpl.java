package ru.forwardmobile.tforwardpayment.security;

/**
 * Created by PiskunovI on 23.06.14.
 */
public class XorImpl {

    public byte[] encrypt(byte[] text, String keyWord)
    {
        byte[] keyarr = keyWord.getBytes();
        byte[] result = new byte[text.length];
        for(int i = 0; i< text.length; i++)
        {
            result[i] = (byte) (text[i] ^ keyarr[i % keyarr.length]);
        }
        return result;
    }
    public String decrypt(byte[] text, String keyWord)
    {
        byte[] result  = new byte[text.length];
        byte[] keyarr = keyWord.getBytes();
        for(int i = 0; i < text.length;i++)
        {
            result[i] = (byte) (text[i] ^ keyarr[i% keyarr.length]);
        }
        return new String(result);
    }
}