package ru.forwardmobile.tforwardpayment;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import ru.forwardmobile.tforwardpayment.security.IKeyStorage;
import ru.forwardmobile.tforwardpayment.security.KeySingleton;
import ru.forwardmobile.tforwardpayment.security.KeyStorageFactory;
import ru.forwardmobile.tforwardpayment.security.XorImpl;


/**
 * Набор настроек приложения
 * @author Vasiliy Vanin
 */
public class TSettings extends Properties {

    private static final String LOGGER_TAG              = "TFORWARD.TSETTINGS";
    public static final String SERVER_HOST              = "server_host";
    public static final String SERVER_PORT              = "server_port";
    public static final String NODE_HOST                = "node_host";
    public static final String NODE_PORT                = "node_port";
    public static final String CERTIFICATE_ACESS_ID     = "access-id";
    public static final String MAXIMUM_START_TRY_COUNT  = "maxstarttryesx";
    public static final String QUEUE_ERROR_DELAY        = "queue_error_delay";
    public static final String QUEUE_STATUS_DELAY       = "queue_status_delay";
    public static final String MAXIMUM_STORED_PAYMENTS  = "maxstoredsizex";
    public static final String MAXIMUM_TRY_COUNT        = "maximum_try_count";
    public static final char   CRLF                     = '\n';

    public static final Integer VERSION_CODE            = 101;
    public static final String  LOCAL_REPOSITORY_URL    = "http://www.forwardmobile.ru/files/android/payment.apk";

    public static String getVersion() {
        return "Android_101";
    }
    
    /**
     * Usage:
     * <pre>
     *      String host = TSettings.get(TSettings.SERVER_HOST);
     * </pre>
     * @param name
     * @return  String or null
     */
    public static String get(String name ) {
        return instance.getProperty(name);
    }

    public static String get(String name, String value) {
        if(instance.containsKey(name)) return get(name);
        else return value;
    }
    
    /**
     * @param name
     * @return int or null
     */
    public static int getInt(String name) {
        return Integer.valueOf(get(name));
    }
    public static int getInt(String name, int value) {
        try {
            return Integer.parseInt(get(name));
        }catch(Exception ex) {
            return value;
        }
    }
    /**
     * @param name
     * @return double or null
     */
    public static double getDouble(String name) {
        return Double.valueOf(get(name));
    }
    public static double getDouble(String name, Double value) {
        try {
            return Double.parseDouble(get(name));
        }catch(Exception ex){
            return value;
        }
    }
    
    /**
     * @param name
     * @return InputStream (never return null)
     */
    public static InputStream getAsStream(String name) {
        String value = get(name);
        if( value != null ) {
            return new ByteArrayInputStream(value.getBytes());
        } else {
            // Null safe
            return new ByteArrayInputStream(new byte[]{});
        }
    }
    
    public static void  set(String name, String value) {
        instance.setProperty(name, value);
    }
    
    public static Set<Object> getKeys() {
        return instance.keySet();
    }


    private TSettings(){}
    private static final TSettings instance
            = new TSettings();

    public static void setAuthenticationPass(String password, Context context){
        // Если мы находимся в этом методе, значит у нас по-любому доступен расшифрованный закрытый ключ
        // Шифруем этот ключ паролем, который ввел наш пользователь
        byte[] encryptedKey = new XorImpl().encrypt(
                KeyStorageFactory.getKeyStorage(context).getKey(IKeyStorage.SECRET_KEY_TYPE),
                password
        );


        // Сохраняем шифрованный ключ на диск
        KeySingleton.getInstance(context)
                .setEncKey( encryptedKey );
    }

}
