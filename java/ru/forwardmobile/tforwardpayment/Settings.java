package ru.forwardmobile.tforwardpayment;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class Settings extends Properties {

    private static final String LOGGER_TAG              = "TFORWARD.TSETTINGS";
    public static final String isAuthenticated          = "authenticated";
    public static final String POINT_ID                 = "pointid";
    public static final String REG_ID                   = "regid";
    public static final String DEALERS_NAME             = "dealers_name";
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

    public static final Integer VERSION_CODE            = 102;
    public static final String  LOCAL_REPOSITORY_URL    = "http://www.forwardmobile.ru/files/android/payment.apk";

    // Набор стандартных настроек
    private static final String DEFAULT_SERVER_HOST     = "www.forwardmobile.ru";
    private static final String DEFAULT_SERVER_PORT     = "8193";

    private static final String DEFAULT_NODE_HOST       = "www.forwardmobile.ru";
    private static final String DEFAULT_NODE_PORT       = "3000";

    public static String getVersion() {
        return "Android_102";
    }
    
    /**
     * Usage:
     * <pre>
     *      String host = TSettings.get(TSettings.SERVER_HOST);
     * </pre>
     * @param name
     * @return  String or null
     */
    public static String get(Context context, String name ) {
        return getInstance(context).getProperty(name);
    }

    public static String get(Context context, String name, String value) {
        if(getInstance(context).containsKey(name)) return get(context, name);
        else return value;
    }
    
    /**
     * @param name
     * @return int or null
     */
    public static int getInt(Context context, String name) {
        return Integer.valueOf(get(context, name));
    }
    public static int getInt(Context context, String name, int value) {
        try {
            return Integer.parseInt(get(context, name));
        }catch(Exception ex) {
            return value;
        }
    }
    /**
     * @param name
     * @return double or null
     */
    public static double getDouble(Context context, String name) {
        return Double.valueOf(get(context, name));
    }
    public static double getDouble(Context context, String name, Double value) {
        try {
            return Double.parseDouble(get(context, name));
        }catch(Exception ex){
            return value;
        }
    }
    
    /**
     * @param name
     * @return InputStream (never return null)
     */
    public static InputStream getAsStream(Context context, String name) {
        String value = get(context, name);
        if( value != null ) {
            return new ByteArrayInputStream(value.getBytes());
        } else {
            // Null safe
            return new ByteArrayInputStream(new byte[]{});
        }
    }
    
    public static void  set(Context context, String name, String value) {
        getInstance(context).setProperty(name, value);
        OutputStream os = null;
        try {

            os = context.openFileOutput("settings.properties", Context.MODE_PRIVATE);
            getInstance(context).store(os, null);

        }catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            if(os != null)
                try { os.close();}catch (Exception ex){}
        }
    }

    private Settings(){}
    private static Settings instance;

    private static Settings getInstance(Context context) {

        if(instance == null) {

            instance = new Settings();
            InputStream is = null;
            try {

                is = context.openFileInput("settings.properties");
                instance.load(is);
            } catch(IOException ex) {

                instance.set(context, Settings.NODE_HOST, DEFAULT_NODE_HOST);
                instance.set(context, Settings.NODE_PORT, DEFAULT_NODE_PORT);

                instance.set(context, Settings.SERVER_HOST, DEFAULT_SERVER_HOST);
                instance.set(context, Settings.SERVER_PORT, DEFAULT_SERVER_PORT);

            }finally {
                if(is != null)
                    try { is.close(); }catch (Exception ex){}
            }
        }

        return instance;
    }

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
