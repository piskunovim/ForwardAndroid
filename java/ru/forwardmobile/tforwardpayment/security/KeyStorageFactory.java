package ru.forwardmobile.tforwardpayment.security;

import android.content.Context;

/**
 * @author Vasiliy Vanin
 */
public class KeyStorageFactory {
    
    public static IKeyStorage getKeyStorage(Context context) {
        return new FileKeyStorageImpl(context);
    }
}
