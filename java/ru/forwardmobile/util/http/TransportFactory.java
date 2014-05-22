package ru.forwardmobile.util.http;

public class TransportFactory {

    public static ITransport getTransport() {
            TransportImpl transport = new TransportImpl();
            return transport;
    }
}
