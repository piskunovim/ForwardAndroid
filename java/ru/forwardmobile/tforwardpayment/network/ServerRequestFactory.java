package ru.forwardmobile.tforwardpayment.network;

import ru.forwardmobile.util.http.IRequest;
import ru.forwardmobile.util.http.RequestFactory;

/**
 * @author Vasiliy Vanin
 */
public class ServerRequestFactory {
    
    private static final int DEFAULT_TIMEOUT = 30;

    public static IRequest getRequest(String command) {
        
        IRequest request = RequestFactory.getRequest_1_1();
        request.setMethod(IRequest.POST);
        request.setTimeout(DEFAULT_TIMEOUT);
        request.setData( command.getBytes() );
        return request;
    }    
}
