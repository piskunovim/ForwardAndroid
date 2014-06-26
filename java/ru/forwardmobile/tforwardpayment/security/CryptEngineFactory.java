package ru.forwardmobile.tforwardpayment.security;

import android.content.Context;

/**
 * DI-container для модуля безопасности
 * Created by vaninv on 26.06.2014.
 */
public class CryptEngineFactory {

    private static ICryptEngine engine = null;

    public static ICryptEngine getEngine(Context ctx) throws Exception {

        if( engine == null)
            engine = new CryptEngineImpl(ctx);

        return engine;
    }
}
