package ru.forwardmobile.util.http;

public interface ITransport {

        public IResponse send(IRequest request) throws Exception;
        
}
