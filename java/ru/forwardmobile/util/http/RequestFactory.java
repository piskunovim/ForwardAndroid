package ru.forwardmobile.util.http;

public class RequestFactory {

        public static IRequest getRequest() {
                RequestImpl request = new RequestImpl();
                return request;
        }
        
        public static IRequest getRequest_1_1() {
                RequestImpl request = new RequestImpl(RequestImpl.VERSION_1_1);
                return request;
        }

}
