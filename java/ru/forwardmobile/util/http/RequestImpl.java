package ru.forwardmobile.util.http;

import java.util.ArrayList;
import java.util.Collection;

public class RequestImpl implements IRequest {
        
        public static final String VERSION_1_0 = "1.0";
        public static final String VERSION_1_1 = "1.1";
        
        
        private String                  host    = null;
        private Integer                 port    = null;
        private String                  path    = null;
        private Integer                 timeout = null;
        private int                     method  = GET;
        private byte[]                  data    = null;
        private Collection<String>      headers = new ArrayList<String>();
        private String                  version = VERSION_1_0;
        
        public RequestImpl() {
        }
        
        public RequestImpl(String version) {
                this.version = version;
        }

        public void setHost(String host) {
                this.host = host;
        }
        
        public String getHost() {
                return this.host;
        }
        
        public void setPort(Integer port) {
                this.port = port;
        }
        
        public Integer getPort() {
                return this.port;
        }
        
        public void setPath(String path) {
                this.path = path;
        }
        
        public String getPath() {
                return this.path;
        }
        
        public void setTimeout(Integer timeout) {
                this.timeout = timeout;
        }
        
        public Integer getTimeout() {
                return this.timeout;
        }
        
        public int      getMethod() {
                return this.method;
        }
        
        public void     setMethod(int method) {
                this.method = method;
        }
        
        public byte[]   getData() {
                return this.data;
        }
        
        public void     setData(byte[] data) {
                this.data = data;
        }
        
        public Collection<String> getHeaders() {
                return this.headers;
        }
        
        public void addHeader(String header) {
                this.headers.add(header);
        }
        
        public void addHeader(String name, String value) {
                this.headers.add(name + ": " + value);
        }
        
        public String toString() {
                StringBuilder buffer = new StringBuilder();
                
                // {method} {path}(?data) HTTP(1.0|1.1)
                if ( this.method == IRequest.GET ) {
                        buffer.append("GET");
                } else
                if ( this.method == IRequest.POST ) {
                        buffer.append("POST");
                }
                buffer.append(' ');
                buffer.append(this.path);
                if ( ( this.method == IRequest.GET ) && ( data != null ) ) {
                        buffer.append('?');
                        buffer.append(this.data);
                }
                buffer.append(' ');
                if ( version == VERSION_1_1 ) {
                        buffer.append("HTTP/1.1");
                } else {
                        buffer.append("HTTP/1.0");
                }
                buffer.append('\n');
                
                if ( version == VERSION_1_1 ) {
                        buffer.append("Host: ");
                        buffer.append(this.host);
                        buffer.append("\n");
                        buffer.append("Connection: ");
                        buffer.append("close");
                        buffer.append("\n");
                }
                
                for(String header : this.headers) {
                        buffer.append(header);
                        buffer.append('\n');
                }
                
                if ( ( this.method == IRequest.POST ) && ( data != null ) ) {
                        buffer.append("Content-length: " + this.data.length);
                        buffer.append('\n');
                }
                buffer.append('\n');
                        
                if ( this.method == IRequest.POST ) {
                       buffer.append(new String(this.data));
                }
                
                return buffer.toString();
        }

}
