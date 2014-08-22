package ru.raketa.util.xml;

import org.xmlpull.v1.XmlPullParser;

/**
 * Syntax Sugar thing
 * Created by Василий Ванин on 25.07.14.
 */
public abstract class AbstractXmlDeserializable implements IXmlDeserializable {

    /**
     * Safe getter for integer values
     * @param parser
     * @param name
     * @return
     */
    protected int readInt(XmlPullParser parser, String name) {
        try {
            return Integer.valueOf(parser.getAttributeValue(null, name));
        } catch(Exception ex) {
            return 0;
        }
    }
/*
    protected Date readDate(XmlPullParser parser, String name) {
        try {
            return Dates.fromSqlDatetime(parser.getAttributeValue(null,name));
        } catch (Exception ex) {
            return null;
        }
    }*/

    protected boolean readBoolean(XmlPullParser parser, String name) {
        try {
            return Boolean.valueOf(parser.getAttributeValue(null, name));
        }catch (Exception ex) {
            return false;
        }
    }
}
