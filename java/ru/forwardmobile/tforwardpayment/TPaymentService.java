package ru.forwardmobile.tforwardpayment;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.security.ICryptEngine;
import ru.forwardmobile.tforwardpayment.spp.IPaymentQueue;
import ru.forwardmobile.tforwardpayment.spp.PaymentQueueWrapper;

/**
 * Created by Vasiliy Vanin on 09.06.2014.
 */
public class TPaymentService extends Service {

    SQLiteOpenHelper         helper = null;
    ICryptEngine        cryptEngine = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IPaymentQueue queue = PaymentQueueWrapper.getQueue();
        if(!queue.isActive()) {
            try {

                queue.setDatabaseHelper(new DatabaseHelper(this));

                HttpTransport transport = new HttpTransport();
                transport.setCryptEngine(new CryptEngineImpl(this));

                queue.setTransport(transport);
                queue.start();

            }catch (Exception ex) {
                ex.printStackTrace();
                return START_FLAG_RETRY;
            }
        }


        return START_FLAG_RETRY;
    }

    @Override
    public void onDestroy() {

        PaymentQueueWrapper.getQueue()
                .stop();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
