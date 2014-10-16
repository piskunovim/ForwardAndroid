package ru.raketa.util.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * XML - помощник
 * @author Василий Ванин (http://mindcircus.ru/)
 */
public class XmlHelper {
    
    public static final String UNICODE  = "utf-8";
    
    public static void parse(IXmlDeserializable handler, InputStream is) throws Exception
    {
        XmlPullParser parser    = Xml.newPullParser();
        parser.setInput(is, "UTF-8");
        
        parse(handler, parser);
    }
    
    private static void parse(IXmlDeserializable handler, XmlPullParser parser) throws Exception
    {
        int event;
        String text = null;
        while((int)(event = parser.next()) != XmlPullParser.END_DOCUMENT) {
            
            if( XmlPullParser.START_TAG == event ) {
                handler.onElementStart( parser.getName(), parser );
            } else 
            if( XmlPullParser.END_TAG == event) {
                handler.onElementEnd( parser.getName(), text );
            } else
            if( XmlPullParser.TEXT == event) {
                text = parser.getText();
            }
        }
    }
}
