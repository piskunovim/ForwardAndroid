package ru.forwardmobile.tforwardpayment.security;

import android.content.Context;

/**
 * @author Vasiliy Vanin
 */
public class KeyStorageFactory {
    
    public static IKeyStorage getKeyStorage(Context context) {
        return KeySingleton.getInstance(context);
        // return mockKeystorage();
    }
    
    public static IKeyStorage mockKeystorage() {
        return new IKeyStorage() {

            public byte[] getKey(String type) {
                if( "public" . equals( type )) {
                    return ("MIICijCCAfOgAwIBAgICBpAwDQYJKoZIhvcNAQEFBQAwfzELMAkGA1UEBhMCUlUx\n" +
                            "EjAQBgNVBAcTCUtyYXNub2RhcjEjMCEGA1UEChMaT09PIEZvcndhcmQgTW9iaWxl\n" +
                            "IFJvb3QgQ0ExGTAXBgNVBAMTEGZvcndhcmRtb2JpbGUucnUxHDAaBgkqhkiG9w0B\n" +
                            "CQEWDWtpbDgyQG1haWwucnUwHhcNMDgwNTIxMTE1NTEyWhcNMTgwNTE5MTE1NTEy\n" +
                            "WjBjMRIwEAYDVQQHEwlLcmFzbm9kYXIxFjAUBgNVBAoTDUZvcndhcmRNb2JpbGUx\n" +
                            "DjAMBgNVBAMTBUFkbWluMSUwIwYJKoZIhvcNAQkBFhZhZG1pbkBmb3J3YXJkbW9i\n" +
                            "aWxlLnJ1MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANwfbvGF/fGOrswVLSxpQLPp\n" +
                            "U74NNaVElFkkNj9exlEYdgYRo+lt9rBNB2npzh+SxFCZbm1y4shfWdGbF5kKqekC\n" +
                            "AwEAAaN1MHMwCQYDVR0TBAIwADARBglghkgBhvhCAQEEBAMCB4AwCwYDVR0PBAQD\n" +
                            "AgWgMBMGA1UdJQQMMAoGCCsGAQUFBwMCMDEGCWCGSAGG+EIBDQQkFiJPcGVuU1NM\n" +
                            "IENlcnRpZmljYXRlIGZvciBTU0wgQ2xpZW50MA0GCSqGSIb3DQEBBQUAA4GBAEr0\n" +
                            "e7QvXl/5ne5IK/bIkoADKAPXQUhPsWac/OHL7iSjCrqf9qU8tYhoAYu/JzG34nex\n" +
                            "JpgjO+jw7HGdpGqrl2sOBh9Kac3O2XFjutHSueLaXD6USu2JrVHWFswm+Sbc1eQz\n" +
                            "U107qgwNp5BmVekckXXWpafdw7Vkn+SWYmskvP0a").getBytes();
                } else {
                    return (
                            "MIICWwIBAAKBgQDCvWtEwT9+Pta4uuEtksDAYxG/+VWGw955zkWqEH3y6D2jZc7K\n" +
                            "TlkfzJUEy9lfPZH2W+53hQ0KW5odVxvYll5zqdpCUGLmMB6dLP+i3YMtkLKv4VDk\n" +
                            "aPY8JUeIEuS6hBYTSZ8+eO9+N6oyCw0tP/TnS6Kz4N7v6dp+1y2OU5FEwwIDAQAB\n" +
                            "AoGAeIQdFGm7z3c4Dw6oODnvy6AD5hh503L4Bc2f00VtjJwpOSvSM+UUxtcnCdbV\n" +
                            "6VkDMcFm7NSMY3KZB7tW4kz8meeJuL1h9wK1FWj4s9mq2DObD4sBRNBZAlzGCtSg\n" +
                            "SzNx/nihc9Zno6Gfq/1q9PUdNth0FIpuUD3X3Jx2bkihnfECQQDirhs8Kjkj48b9\n" +
                            "KGrc1IVsk+EOZPB49OvfM7cFH5K2Xg0yWw+fZkA9uC0yqf0Pca5+jBU0urZ2v8H1\n" +
                            "wVl5nCOfAkEA2+2zUtGf5uz1iBs0tJs0RrBxSwUzqlXAFcAl3pmkWkQJNH27flTt\n" +
                            "ri5gT6bs+rvT40Z7vSczDF7afoIEDacsXQJAAeYdekqMQf21fAdrpb/uAjPW7czw\n" +
                            "qsK6exsuzE8wZnCxQZu7rMUpgprZXhRId2mnYY4A23k48BO5JzrgagFh1wJAAcFM\n" +
                            "3A1WNFaRwwtMiWBvs1y3Gr0o42QZy1KkOrmJKTs/w455T5HH78ro89nGLF4RW8/1\n" +
                            "LtZAPwoGvlm1RJKf9QJAdAuA+3FFV0pBLcGFIoRVUJ6u5ZWwEwmgX0gIALfTZRaY\n" +
                            "1iyBB/8OX1NBUHbD/zSIxZIqzsgpyQ5TZA+Uu31MPQ==").getBytes();
                }
            }

            public void setKey(String type, byte[] key) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }
        };
    }
}
