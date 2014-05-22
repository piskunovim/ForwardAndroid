package ru.forwardmobile.util.http;

import java.util.Collection;

public interface IResponse {

        public int getCode();
        public void setCode(int code);
        public Collection<String> getHeaders();
        public void addHeader(String header);
        public byte[] getData();
        public void setData(byte[] data);

}
