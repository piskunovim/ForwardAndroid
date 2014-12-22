package ru.forwardmobile.tforwardpayment.actions;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;
import ru.forwardmobile.util.http.IRequest;
import ru.forwardmobile.util.http.IResponse;
import ru.forwardmobile.util.http.ITransport;
import ru.forwardmobile.util.http.RequestFactory;
import ru.forwardmobile.util.http.TransportFactory;

/**
 * Обновление для приложение из нашего источника
 * Created by Василий Ванин on 22.12.2014.
 */
public class UpdateCheckTask extends AbstractTask {

    static final String LOGGER = UpdateCheckTask.class.getName();

    public UpdateCheckTask(Context context, ITaskListener listener) {
        super(listener, context);
    }

    @Override
    protected Object doInBackground(Object... objects) {

        IRequest request = RequestFactory.getRequest();

        request.setHost(TSettings.get(TSettings.NODE_HOST));
        request.setPort(TSettings.getInt(TSettings.NODE_PORT));
        request.setPath("/info");
        request.setTimeout(30);

        ITransport transport = TransportFactory.getTransport();


        try {

            IResponse  response  = transport.send(request);
            Gson gson = new Gson();
            Map info = gson.fromJson(new String(response.getData()), Map.class);

            Log.i(LOGGER, "Current version: " + info.get("version"));

            return info;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}
