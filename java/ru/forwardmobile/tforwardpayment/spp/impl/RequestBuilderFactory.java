package ru.forwardmobile.tforwardpayment.spp.impl;

import ru.forwardmobile.tforwardpayment.operators.RequestBuilder;
import ru.forwardmobile.tforwardpayment.spp.IRequestBuilder;

/**
 * DI-контейнер конструктора запросов
 * Created by Василий Ванин on 19.09.2014.
 */
public class RequestBuilderFactory  {
    public static IRequestBuilder getRequestBuilder() {
        return new RequestBuilder();
    }
}
