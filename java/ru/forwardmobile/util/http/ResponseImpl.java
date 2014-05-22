package ru.forwardmobile.util.http;

import java.util.ArrayList;
import java.util.Collection;

public class ResponseImpl implements IResponse {

        private int                     code    = 0;
        private Collection<String>      headers = new ArrayList<String>();
        private byte[]                  data    = null;
        
        public int getCode() {
                return this.code;
        }
        
        public void setCode(int code) {
                this.code = code;
        }
        
        public Collection<String> getHeaders() {
                return  this.headers;
        }
        
        public void addHeader(String header) {
                this.headers.add(header);
        }
        
        public byte[] getData() {
                return this.data;
        }
        
        public void setData(byte[] data) {
                this.data = data;
        }
        
        public String toString() {
                StringBuilder buffer = new StringBuilder();
                
                buffer.append("http-code: ");
                buffer.append("" + this.code);
                buffer.append(", ");
                buffer.append("headers count: " + this.headers.size());
                buffer.append(", ");
                if ( this.data != null ) {
                        buffer.append("data size: ");
                        buffer.append("" + this.data.length);
                } else {
                        buffer.append("no data");
                }
                
                return buffer.toString();
        }

}
