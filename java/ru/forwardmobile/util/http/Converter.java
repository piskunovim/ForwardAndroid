package ru.forwardmobile.util.http;


public class Converter {

    public static String RUSSIAN_ALPHABET ="АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";
    public static String ADDITIONAL_SIMBOLS = "€”“’‘•™¦§Ё©«®±µ¶·ё№»";
    public static byte[] ADDITIONAL_CODES = {
            -120, -111, -110, -109, -108, -107, -103, -90, -89, -88, -87, // € ” “ ’ ‘• ™ ¦ § Ё ©
            -85, -82, -79,      // « ® ±
            -75, -74, -73, -72, -71, -69 // µ ¶ · ё № »
    };

    static public char toUnicode(byte b) {
        // Расчет на Windows-1251
        if (b >= ADDITIONAL_CODES[0] && b <= ADDITIONAL_CODES[ADDITIONAL_CODES.length - 1]) {
            for (int pos = 0; pos < ADDITIONAL_CODES.length; pos++)
                if (ADDITIONAL_CODES[pos] == b)
                    return ADDITIONAL_SIMBOLS.charAt(pos);
            return '?';
        }
        if(b >= -64 &&  b <= -1)
            return RUSSIAN_ALPHABET.charAt(64 + b);
        return (char)b;

    }

    static public String toUnicode(byte[] ba) {
        if (ba == null)
            return null;
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < ba.length; i++)
            result.append(toUnicode(ba[i]));
        return result.toString();
    }

    static public byte toCp1251(char ch) {
        int pos = ADDITIONAL_SIMBOLS.indexOf(ch);
        if (pos != -1)
            return ADDITIONAL_CODES[pos];
        pos = RUSSIAN_ALPHABET.indexOf(ch);
        if (pos != -1)
            return (byte)(pos + 0xC0);
        return (byte)ch;
    }

    static public byte[] toCp1251(String us) {
        if (us == null)
            return null;
        int length = us.length();
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++)
            result[i] = toCp1251(us.charAt(i));
        return result;
    }

    static public byte[] toCp1251(StringBuffer us) {
        if (us == null)
            return null;
        int length = us.length();
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++)
            result[i] = toCp1251(us.charAt(i));
        return result;
    }
}
