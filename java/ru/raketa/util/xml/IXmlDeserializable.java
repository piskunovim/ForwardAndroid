package ru.raketa.util.xml;

import org.xmlpull.v1.XmlPullParser;

/**
 * Интерфейс для классов, которые могут быть проинициализированы 
 * с помощью XmlPullParser. Например ответы от сервера.
 * @author Василий Ванин (http://mindcircus.ru/)
 */
public interface IXmlDeserializable {
    public void onElementStart(String name, XmlPullParser parser) throws Exception;
    public void onElementEnd(String name, String text) throws Exception;
}
