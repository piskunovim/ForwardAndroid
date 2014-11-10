package ru.forwardmobile.tforwardpayment.operators;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;

import ru.forwardmobile.tforwardpayment.operators.impl.ProvidersEntityManagerImpl;

/**
 * Класс, который обеспечивает функционал синглтона для бд операторов
 * Created by Василий Ванин on 10.09.2014.
 */
public class OperatorsEntityManagerFactory {

    private static ProvidersEntityManagerImpl manager;

    public static ProvidersEntityManagerImpl getManager(Context ctx) {
        if (manager == null) {
            try {

                File file = new File(ctx.getFilesDir() + "/" + "operators.xml");
                FileInputStream fileInputStream = new FileInputStream(file);

                manager = new OperatorsXmlReader()
                        .readOperators(fileInputStream);
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return manager;
    }

}
