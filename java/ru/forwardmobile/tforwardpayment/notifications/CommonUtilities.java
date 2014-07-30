package ru.forwardmobile.tforwardpayment.notifications;

/**
 * Created by PiskunovI on 24.07.2014.
 */
import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    // указываем url сервера регистрации здесь
    static final String SERVER_URL = "http://127.0.0.1/gcm_server_php/register.php";

    // Google project id
    static final String SENDER_ID = "421740259735";

    /**
     * Тэг будем использовать в Log-сообщениях.
     */
    static final String TAG = "ForwardPaymentGCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "ru.forwardmobile.tforwardpayment.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Сообщает UI о выводе сообщения.
     * <p>
     * Этот метод определен в общем помощнике, потому как используется
     * как UI, так и фоновыми сервисами.
     *
     * @param context - context приложения.
     * @param message - сообщение для вывода.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}