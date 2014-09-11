package ru.forwardmobile.tforwardpayment.spp;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.operators.OperatorsEntityManagerFactory;

/**
 * DI-Контейнер провайдера
 * Created by Василий Ванин on 11.09.2014.
 */
public class ProvidersDataSourceFactory {

    public static IProvidersDataSource getDataSource(Context ctx) {
        return OperatorsEntityManagerFactory.getManager(ctx);
    }
}
