package ru.forwardmobile.util.http;

import java.util.Collection;

public interface IRequest {
        
        public static final int GET     = 0;
        public static final int POST    = 1;

        public String   getHost();
        public void     setHost(String host);
        public Integer  getPort();
        public void     setPort(Integer port);
        public String   getPath();
        public void     setPath(String path);
        public Integer  getTimeout();
        public void     setTimeout(Integer timeout);
        public int      getMethod();
        public void     setMethod(int method);
        public byte[]   getData();
        public void     setData(byte[] data);
        public Collection<String> getHeaders();
        public void addHeader(String header);
        public void addHeader(String name, String value);

}
