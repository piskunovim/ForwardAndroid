package ru.forwardmobile.tforwardpayment;

/**
 * Created by PiskunovI on 25.07.2014.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import static ru.forwardmobile.tforwardpayment.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static ru.forwardmobile.tforwardpayment.CommonUtilities.EXTRA_MESSAGE;
import static ru.forwardmobile.tforwardpayment.CommonUtilities.SENDER_ID;


public class GCMActivity extends Activity {

    // для отображения gcm-сообщений
    TextView lblMessage;

    AsyncTask<Void, Void, Void> mRegisterTask;

    AlertDialogManager alert = new AlertDialogManager();

    ConnectionDetector cd;

    public static String name;
    public static String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_gcm);

        cd = new ConnectionDetector(getApplicationContext());

        // Проверяем доступен ли Интернет
        if (!cd.isConnectingToInternet()) {
            // Если интернет соединение недоступно
            alert.showAlertDialog(GCMActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // прерываем исполнение кода вызовом return
            return;
        }

        // Получаем имя, email из intent
        Intent i = getIntent();

        name = i.getStringExtra("name");
        email = i.getStringExtra("email");

        //Убеждаемся, что устройство имеет соответствующие зависимости.
        GCMRegistrar.checkDevice(this);

        // Убеждаемся, что манифест был правильно настроен - закомментируйте эту строку
        // при разработке приложения. Раскомментируйте ее, когда оно будет готово.
        GCMRegistrar.checkManifest(this);

        //lblMessage = (TextView) findViewById(R.id.lblMessage);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Получить GCM регистрационный id
        final String regId = GCMRegistrar.getRegistrationId(this);

        // Проверям был ли regid уже предоставлен ранее
        if (regId.equals("")) {
            // Регистрации ранее небыло, регистрируем сейчас на GCM
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Устройство уже зарегистрированно на  GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Пропустить этап регистрации.
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Попробуем зарегистрироваться снова, но не в потоке пользовательского интерфейса.
                // Это также необходимо для отмены потока OnDestroy (),
                // следовательно используем AsyncTask вместо исходного потока.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Регистрируемся на нашем сервере
                        // На сервере создается новый пользователь
                        ServerUtilities.register(context, name, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    /**
     * Получаем push-сообщения
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Пробуждаем телефон, если он находился в режиме сна
            WakeLocker.acquire(getApplicationContext());

            /**
             * Необходимо предпринять дальнейшие действия над этим сообщением
             * в зависимости от требований вашего приложения
             * на данный момент мы просто отобразим его на экране
             * */

            // Показываем полученное сообщение
            lblMessage.append(newMessage + "\n");
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Освобождаем блокировку сна
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

}
