/**
 * ООО "Форвад Мобайл" 2014 г.
 * http://www.forwardmobile.ru/
 */
package ru.forwardmobile.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Помощник для обработки HTTP-запросов
 * @author Василий Ванин (http://mindcircus.ru)
 */
public class HttpUtils {
    
    public static Map<String,String>  getRequestParams(String query) throws IOException {
        
        Map<String,String>  params  = new HashMap<String,String>();
        StringBuilder       field   = new  StringBuilder();
        StringBuilder       value   = new  StringBuilder();
        
        boolean     insideField = true;
        char[]      characters  = query.toCharArray();
        
        for(int i = 0; i < characters.length; i++) {
            
            switch(characters[i]) {
                case '=' : if(insideField) {
                                insideField = false;
                                break;
                           }
                    
                case '&' : params.put(field.toString(), URLDecoder.decode(value.toString(),"UTF-8"));
                           field = new StringBuilder();
                           value = new StringBuilder();
                           insideField = true;
                           break;
                    
                default: if(insideField) field.append(characters[i]);
                         else            value.append(characters[i]);
            }
        }
        
        if(field.length() > 0 && value.length() > 0) {
            params.put(field.toString(), URLDecoder.decode(value.toString(),"UTF-8"));
        }        
        
        return params;
    }
    
    /** @deprecated */
    public static Map<String,String>  getRequestParams(InputStream is) throws IOException  {  
        Map<String,String>  params  = new HashMap<String,String>();
        StringBuilder       field   = new  StringBuilder();
        StringBuilder       value   = new  StringBuilder();
       
        boolean insideField = true;
       
        while(is.available() > 0) {
            byte b = (byte) is.read();
            switch((char) b) {
                case '=' : if(insideField) {
                                insideField = false;
                                break;
                           }
                    
                case '&' : params.put(field.toString(), URLDecoder.decode(value.toString(),"UTF-8"));
                           field = new StringBuilder();
                           value = new StringBuilder();
                           insideField = true;
                           break;
                    
                default: if(insideField) field.append(b);
                         else            value.append(b);
            }
        }
        
        if(field.length() > 0 && value.length() > 0) {
            params.put(field.toString(), URLDecoder.decode(value.toString(),"UTF-8"));
        }        
        
        return params;
    }
}
