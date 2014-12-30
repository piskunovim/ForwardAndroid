package ru.forwardmobile.tforwardpayment;

/**
 * Created by PiskunovI on 24.07.2014.
 */

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static ru.forwardmobile.tforwardpayment.CommonUtilities.SERVER_URL;
import static ru.forwardmobile.tforwardpayment.CommonUtilities.TAG;
import static ru.forwardmobile.tforwardpayment.CommonUtilities.displayMessage;


public final class ServerUtilities {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    /**
     * Регистрация пары аккаунт/устройство на сервере.
     *
     */
    public static void register(final Context context, String name, final String regId) {
        Log.i(TAG, "registering device (regId = " + regId + ")");

        if (regId != null)
            TSettings.set(TSettings.REG_ID, regId);

        GCMHTTPTask gcmhttpTask = new GCMHTTPTask();
        gcmhttpTask.execute();

        String serverUrl = SERVER_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("name", name);



        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // После того как GCM возвращает регистрационный идентификатор, мы должны зарегистрироваться на нашем сервере
        // Так как сервер может быть недоступен, мы будем повторять попытку несколько раз.
        /*for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Попытка #" + i + " на регистрацию");
            try {
                displayMessage(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));
                post(serverUrl, params);
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);
                CommonUtilities.displayMessage(context, message);
                return;
            } catch (IOException e) {

                // Здесь упрощенная схема, т.к. повторяем в случае любой ошибки;
                // по хорошему приложение должно повторять попытку только на неустранимых ошибках
                // (Например, как код ошибки HTTP 503).
                Log.e(TAG, "Не удалось зарегистрироваться. Попытка  " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Ожидание " + backoff + " ms перед следующей попыткой");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Активити завершилась прежде чем мы закончили - выход.
                    Log.d(TAG, "Поток прерван: оставшиеся попытки прерваны!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // увеличиваем время ожидания по экспоненте
                backoff *= 2;
            }
        }*/
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        CommonUtilities.displayMessage(context, message);
    }

    /**
     * Отмена регистрации пары аккаунт/устройство с сервера.
     */
    static void unregister(final Context context, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            CommonUtilities.displayMessage(context, message);
        } catch (IOException e) {
            // В этом месте устройство удалено из GCM, но все еще зарегистрировано на сервере.
            // Мы могли бы попытаться отменить регистрацию снова, но это не необходимо:
            // Если сервер попытается отправить сообщение на устройство, он будет получать
            // ошибку "Незарегистрированно" и должен отменить регистрацию устройства.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            CommonUtilities.displayMessage(context, message);
        }
    }

    /**
     * Отправка POST-запроса на сервер.
     *
     * @param endpoint - POST адрес.
     * @param params - требуемые параметры.
     *
     * @throws IOException передается от POST.
     */
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {

        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // создает тело POST-запроса используя параметры
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}