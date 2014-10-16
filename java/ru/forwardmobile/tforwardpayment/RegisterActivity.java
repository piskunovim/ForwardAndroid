package ru.forwardmobile.tforwardpayment;

/**
 * Created by PiskunovI on 25.07.2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static ru.forwardmobile.tforwardpayment.CommonUtilities.SENDER_ID;
import static ru.forwardmobile.tforwardpayment.CommonUtilities.SERVER_URL;

public class RegisterActivity extends Activity {
    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Internet detector
    ConnectionDetector cd;

    // UI elements
    EditText txtName;
    EditText txtEmail;

    // Register button
    Button btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cd = new ConnectionDetector(getApplicationContext());

        // Проверка доступа в Интернет
        if (!cd.isConnectingToInternet()) {
            // Интернет соединение недоступно
            alert.showAlertDialog(RegisterActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // останавливаем исполнение кода при помощи return
            return;
        }

        // Проверяем настроена ли GCM конфигурация
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM id_отправителя/ссылка_на_сервер отсутствует
            alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // останавливаем исполнение кода при помощи return
            return;
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        /*
         * Событие по клику на кнопку Регистрации
         * */
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();

                // Проверяем заполнил ли пользователь форму
                if(name.trim().length() > 0 && email.trim().length() > 0){
                    // Запускаем наше основное активити
                    Intent i = new Intent(getApplicationContext(), GCMActivity.class);

                    // Регистрируем пользователя на нашем сервере
                    // Отсылаем MainActivity детали регистрации
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    startActivity(i);
                    finish();
                }else{
                    // пользователь не заполнил данные
                    // проосим его заполнить форму
                    alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter your details", false);
                }
            }
        });
    }

}
