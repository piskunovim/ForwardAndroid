package ru.forwardmobile.tforwardpayment.spp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ru.forwardmobile.tforwardpayment.TSettings;

/**
 * Сервис для проведения платежей
 * Created by Василий Ванин on 03.06.2014.
 */
public class PaymentService extends Service implements Runnable {
    private static final String LOGGER_TAG          = "TFORWARD.SERVICE";
    // Время отдыха между очередьми команд
    private static final int SERVICE_SLEEP_INTERVAL = TSettings.getInt("payment.sleep", 10);

    private Thread  serviceThread   = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceThread = new Thread( this );
        serviceThread.start();
        serviceThread.setDaemon(true);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        serviceThread.interrupt();
        try {
            serviceThread.join( SERVICE_SLEEP_INTERVAL );
        } catch (InterruptedException e) {
            // do nothing
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // For backward compatibility
        return null;
    }

    @Override
    public void run() {

        do {
            // useful job stub!
            try {
                Thread.sleep(SERVICE_SLEEP_INTERVAL * 1000);
            } catch(InterruptedException ex) {
                Log.i(LOGGER_TAG, "Interrupt received. Try to stop service...");
                break;
            }

        } while ( !serviceThread.isInterrupted() );
    }
}
