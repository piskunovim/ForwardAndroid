package ru.forwardmobile.tforwardpayment;

import java.io.ByteArrayInputStream;
import java.util.Properties;
import java.io.InputStream;
import java.util.Set;


/**
 * Набор настроек приложения
 * @author Vasiliy Vanin
 */
public class TSettings extends Properties {
    
    public static final String SERVER_HOST              = "server-host";
    public static final String SERVER_PORT              = "server-port";
    public static final String CERTIFICATE_ACESS_ID     = "access-id";
    public static final String MAXIMUM_START_TRY_COUNT  = "maxstarttryes";
    public static final String QUEUE_ERROR_DELAY        = "queueerrdelay";
    public static final String MAXIMUM_STORED_PAYMENTS  = "maxstoredsize";
    public static final char   CRLF                     = '\n';



    public static String getVersion() {
        return "Android 1.0 (Build 201405161511)";
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

    static {
        instance.setProperty(CERTIFICATE_ACESS_ID, "1882");
    }
}
