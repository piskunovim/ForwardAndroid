package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ru.forwardmobile.tforwardpayment.TSettings;
import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;
import ru.forwardmobile.tforwardpayment.network.HttpTransport;
import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.security.ICryptEngine;
import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.ICommandRequest;
import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IPaymentDao;
import ru.forwardmobile.tforwardpayment.spp.IPaymentQueue;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.tforwardpayment.spp.IRouter;

/**
 * Created by Vasiliy Vanin on 06.06.2014.
 */
public class PaymentQueueImpl implements IPaymentQueue {

    private static final String LOGGER_TAG     = "TFORWARD.QUEUE";
    private static final String PAYMENT_ABSENT = "Платежа нет в очереди!";
    private static final int    QUEUE_SLEEP_MS = 1000 * 5;

    List<IPayment> activePayments = new ArrayList<IPayment>();
    List<IPayment> storedPayments = new ArrayList<IPayment>();

    private boolean     stop    = false;
    private boolean     active  = false;
    private Thread      thread  = null;

    private HttpTransport       transport;
    private IRouter             router;
    private SQLiteOpenHelper    databaseHelper;
    private IPaymentDao         paymentDao;

    public PaymentQueueImpl(Context ctx) {

        try {
            // Транспорт
            transport = new HttpTransport();
            transport.setCryptEngine( new CryptEngineImpl( ctx ) );

            databaseHelper = new DatabaseHelper( ctx );
            paymentDao     = new PaymentDaoImpl( databaseHelper.getWritableDatabase());
            router         = new RouterImpl();
        } catch (Exception ex) {
            Log.i( LOGGER_TAG, ex.toString() );
            ex.printStackTrace();
        }
    }


    public PaymentQueueImpl(ICryptEngine cryptEngine, DatabaseHelper helper) {
        transport = new HttpTransport();
        transport.setCryptEngine(cryptEngine);

        databaseHelper = helper;
        paymentDao = new PaymentDaoImpl( databaseHelper.getWritableDatabase() );
        router         = new RouterImpl();
    }

    public PaymentQueueImpl() {
        transport = new HttpTransport();
    }

    public void setTransport(HttpTransport transport) {
        this.transport = transport;
    }

    public void setDatabaseHelper(SQLiteOpenHelper helper) {
        this.databaseHelper = helper;
        paymentDao = new PaymentDaoImpl( databaseHelper.getWritableDatabase() );
        router         = new RouterImpl();
    }

    @Override
    public void start() throws Exception {
        this.thread = new Thread(this);
        this.thread.start();
        this.active = true;
    }

    @Override
    public void stop() {

        this.stop = true;
        Log.v(LOGGER_TAG, "Stop signal received...");
        try {
            this.thread.join();
        }catch (InterruptedException ex){
            //
        }

        this.thread = null;
        this.active = false;

        databaseHelper.close();
        Log.i(LOGGER_TAG, "Queue was stopped...");
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void processPayment(IPayment payment) throws Exception {

        payment.setStartDate(new Date());
        payment.setStatus(IPayment.NEW);
        payment.setDateOfProcess(new Date());

        paymentDao.save(payment);

        Log.i(LOGGER_TAG, "New payment started with id "
                + payment.getId());

        synchronized ( activePayments ) {
            activePayments.add( payment );
        }

        synchronized ( this ) {
            this.notifyAll();
        }
    }

    @Override
    public Collection<IPayment> getActivePayments() {
        return activePayments;
    }

    @Override
    public Collection<IPayment> getStoredPayments() {
        return storedPayments;
    }

    @Override
    public Collection<IPayment> getActivePaymentsCopy() {
        return Collections.unmodifiableCollection( activePayments );
    }

    @Override
    public Collection<IPayment> getStoredPaymentsCopy() {
        return Collections.unmodifiableCollection( storedPayments );
    }

    @Override
    public int getActivePaymentsCount() {
        return activePayments.size();
    }

    @Override
    public int getStoredPaymentsCount() {
        return storedPayments.size();
    }

    @Override
    public void cancelPayment(IPayment payment) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deletePayment(IPayment payment) throws Exception {

        synchronized(activePayments) {
            if ( activePayments.contains(payment) ) {
                if ( ( payment.getStatus() == IPayment.FAILED ) ||
                        ( payment.getStatus() == IPayment.CANCELLED ) ||
                        ( payment.isDelayed() ) ||
                        ( ( payment.getStatus() == IPayment.NEW ) && !payment.getSended() ) ) {
                    activePayments.remove(payment);
                    // PaymentDAO.delete(payment);
                    // Application.getCounter().count(payment,IPaymentCounter.COUNT_REMOVE);
                    Log.i(LOGGER_TAG, "#" + payment.getId() + " Payment deleted.");
                } else {
                    throw new Exception("Платеж со статусом отличным от \"Ошибочный\" и \"Отмененный\" (\"" + payment.getStatusName() + "\")!");
                }
            } else {
                throw new Exception(PAYMENT_ABSENT);
            }
        }
    }

    @Override
    public void repeatPayment(IPayment payment) throws Exception {
        synchronized(activePayments) {
            if ( activePayments.contains(payment) ) {
                if ( ( payment.getStatus() == IPayment.FAILED ) || ( payment.getStatus() == IPayment.CANCELLED ) ) {
                    payment.setStatus(IPayment.NEW);
                    payment.setSended(false);
                    payment.setActive(false);
                    payment.setTryCount(0);
                    payment.setFinishDate(null);
                    payment.setDateOfProcess(null);
         //           PaymentDAO.store(payment);
                    Log.i(LOGGER_TAG, "#" + payment.getId() + " Payment repeated.");
                } else {
                    throw new Exception("Платеж со статусом отличным от \"Ошибочный\" и \"Отмененный\" (\"" + payment.getStatusName() + "\")!");
                }
            } else {
                throw new Exception(PAYMENT_ABSENT);
            }
        }
    }

    @Override
    public void startPayment(IPayment payment) throws Exception {
        synchronized(activePayments) {
            if ( activePayments.contains(payment) ) {
                if ( payment.getStatus() != IPayment.NEW ) {
                    throw new Exception("Платеж со статусом отличным от \"Новый\" (\"" + payment.getStatusName() + "\")!");
                } else
                if ( !payment.isDelayed() ) {
                    throw new Exception("Платеж уже находится в обработке!");
                } else {
                    payment.setDelayed(false);
                    payment.setTryCount(0);
                    Log.i(LOGGER_TAG, "#" + payment.getId() + " Payment started.");
                }
            } else {
                throw new Exception(PAYMENT_ABSENT);
            }
        }
    }

    @Override
    public int hasActivePayments() {
        return activePayments.size();
    }

    @Override
    public void run() {

        try {
            Thread.sleep(3000); // delayed start

            while ( !stop ) {


                doPayments();
                synchronized(this) {
                    try {
                        this.wait(QUEUE_SLEEP_MS);
                    } catch(InterruptedException e) {
                        // nothing to do
                    }
                }
            }
            synchronized(this) {
                this.notifyAll();
            }
        } catch(InterruptedException e) {
            // nothing to do
        } finally {
            this.active = false;
        }
    }



    private void doPayments() {

        Log.v(LOGGER_TAG, "Search for unprocessed payments.");

        try {

            ICommandRequest request = createRequest();
            try {

                if(request != null) {

                    Log.d(LOGGER_TAG, "Sending " + request.getCommands().size() + " commands.");
                    IResponseSet responseSet = null;
                    try {
                        responseSet = transport.send( request );
                    } catch (Exception ex) {
                        Log.w(LOGGER_TAG, "Sending error: " + ex.getMessage());
                        request.onError("Ошибка обработки: " + ex.getMessage());
                    }

                    if(responseSet != null) {
                        synchronized (activePayments) {
                            parseResponseSet(request, responseSet);
                        }
                    }

                } else {
                    Log.v(LOGGER_TAG, "Request set is empty..");
                }

            } finally {
                 synchronized (activePayments) {
                     if(request != null) {
                         request.freePayments();
                     }
                 }
            }

        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private ICommandRequest createRequest() {
        ICommandRequest request = null;

        synchronized ( activePayments ) {
            try {

                for(IPayment payment: activePayments) {

                    if ( !payment.isDelayed() ) {
                        if ( ( payment.getDateOfProcess() == null ) ||
                                ( payment.getDateOfProcess().getTime() <= System.currentTimeMillis() ) ) {
                            if ( ( payment.getStatus() != IPayment.CANCELLED ) &&
                                    ( payment.getStatus() != IPayment.FAILED ) &&
                                    !( ( payment.getStatus() == IPayment.DONE ) && !payment.isPreparedForCancelling() ) ) {
                                payment.setActive(true);
                                if ( request == null ) {
                                    request = new CommandRequestImpl(true, true);
                                    request.setRouter(this.router);
                                }
                                request.addPayment(payment);
                            }
                        }
                    }

                }

            } catch (Exception ex) {
                Log.e( LOGGER_TAG, "Request creation error: " + ex.getMessage() );
                ex.printStackTrace();
                return null;
            }
        }

        return request;
    }



    private void parseResponseSet(ICommandRequest request, IResponseSet responseSet) {

        Log.d(LOGGER_TAG, "Parsing response set ...");

        try {
            Collection<ICommand>         c = request.getCommands();
            Collection<ICommandResponse> r = responseSet.getResponses();

            if ( c.size() != r.size() ) {
                throw new Exception("Количество строк ответа (" + r.size() + ") не соответствует количеству строк запроса (" + c.size() + ")!");
            }

            Iterator<ICommand>          commands = c.iterator();
            Iterator<ICommandResponse> responses = r.iterator();
            while ( commands.hasNext() && responses.hasNext() ) {
                ICommand command          = commands.next();
                ICommandResponse response = responses.next();
                try {
                    command.processResponse(response);

                    IPayment payment = command.getPayment();

                    String prefix = "#" + payment.getId() + ":";
                    Log.d(LOGGER_TAG,prefix + " new status: " + PaymentImpl.statusNameEng(payment.getStatus()));

                    // Платеж завершен, только в этом случае удаляем из очереди
                    if(payment.getStatus() == IPayment.DONE) {
                        Log.d(LOGGER_TAG, prefix + " Remove from queue ...");
                        removeFromQueue(payment);
                        // @TODO counter
                        //Application.getCounter().count(payment,IPaymentCounter.COUNT_DONE);
                    }

                    /* Повторяем ошибочный платеж, если число попыток меньше заданного в настройках */
                    if((payment.getStatus() == IPayment.FAILED || payment.getStatus() == IPayment.CANCELLED)) {
                        payment.incErrorRepeatCount();
                        if(payment.getErrorRepeatCount() < TSettings.getInt(TSettings.MAXIMUM_START_TRY_COUNT, 10)) {
                            Log.d(LOGGER_TAG, "Repeating #" + payment.getId() + " - try " + payment.getErrorRepeatCount()
                                    + " of " + TSettings.getInt(TSettings.MAXIMUM_START_TRY_COUNT, 10) + "..");
                            repeatPayment(payment);
                            payment.delay(TSettings.getInt(TSettings.QUEUE_ERROR_DELAY, 600));
                        }
                    }
                } catch(Exception e) {
                    Log.w(LOGGER_TAG, "Ошибка обработки платежа (" + command.getPayment().getId() + "):\n" + e.getMessage());
                    command.getPayment().setErrorDescription(e.getMessage());
                    // Эта не ошибка сервера, поэтому не прекращаем обработку платежа
                    command.getPayment().errorDelay();
                }
            }
        } catch(Exception e) {
            Log.e(LOGGER_TAG, "Ошибка обработки ответа:" + e.getMessage());
        }
    }

    private void removeFromQueue(IPayment payment) {
        activePayments.remove(payment);
        if ( storedPayments.size() >= TSettings.getInt(TSettings.MAXIMUM_STORED_PAYMENTS, 500) ) {
            IPayment deletedPayment = storedPayments.remove(0);
            storedPayments.remove(deletedPayment);
            // PaymentDAO.delete(deletedPayment);
        }
        storedPayments.add(payment);
        // Log.trace("Payment (" + payment.getId() + ") is removed from queue.");
    }

}
