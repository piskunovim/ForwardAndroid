package ru.forwardmobile.tforwardpayment;

/**
 * Created by PiskunovI on 24.07.2014.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {
    /**
     * Функция вывода простейшего Alert Dialog
     * @param context - context приложения
     * @param title - заголовок alert-окна
     * @param message - alert-сообщение
     * @param status - выполнено/не выполнено (используется, чтобы установить иконки)
     *               - сделать null если не нужны иконки
     * */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Устанавливаем Заголовок Окна
        alertDialog.setTitle(title);

        // Устанавливаем Сообщение Окна
        alertDialog.setMessage(message);

   //     if(status != null)
            // Устанавливаем иконки алерт-окна
     //       alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Устанавливаем кнопку OK
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Выводим Alert-Сообщение
        alertDialog.show();


    }
}
