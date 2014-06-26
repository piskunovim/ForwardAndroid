package ru.forwardmobile.tforwardpayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.security.CryptEngineImpl;
import ru.forwardmobile.tforwardpayment.security.FileKeyStorageImpl;
import ru.forwardmobile.tforwardpayment.security.KeySingleton;
import ru.forwardmobile.tforwardpayment.security.XorImpl;
import ru.forwardmobile.tforwardpayment.security.KeyStorageFactory;

/**
 * Created by PiskunovI on 23.06.14.
 */
public class MainAccessActivity extends ActionBarActivity {

    final static String LOG_TAG = "TForwardPayment.MainActivityAccess";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

    //KeyStorageFactory KSF;
    KeySingleton KS;
    FileKeyStorageImpl FKSI;

    TextView commentary;
    EditText keyWord;
    Button button;
    String text, dectext, enctext, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        commentary = (TextView) findViewById(R.id.access_commentary);
        button = (Button) findViewById(R.id.access_button);
        keyWord =  (EditText) findViewById(R.id.access_pass);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.d(LOG_TAG, message);

        FKSI = new FileKeyStorageImpl(MainAccessActivity.this);

        if (message.equals("true"))
        {

            commentary.setText("*пароль доступа был введен вами при первом \n             при первом запуске приложения");
            button.setText("Вперед");
            //сюда добавить кнопку входа в приложение
            button.setOnClickListener(new View.OnClickListener() {
                	    @Override
                        public void onClick(View view) {
                          //  UserAuthentication(keyWord.getText().toString());
                            SendMessage(message); // запуск activity
                        }
   	        });
            //SendMessage(message); // запуск activity

        } else {

            commentary.setText("*в будущем этот пароль будет  \n запрашиваться приложением");

            button.setText("Подтвердить");
            //сюда добавить кнопку сохранения/назначения пароля
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SavePass();
                    SendMessage(message); // запуск activity
                }
            });

            //SendMessage(message); // запуск activity

         }

        Log.d(LOG_TAG, "Activity access form started");
        text = "";
    }

    public byte[] UserAuthentication(String pass){
        XorImpl xor = new XorImpl();
        text = FKSI.getKey("tfpk").toString(); // tforwardpayment key (tfpk)
        dectext = xor.decrypt(text.getBytes(),pass);

        try{
            new CryptEngineImpl(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return dectext.getBytes();
    }

    public void SavePass()
    {
       // XorImpl xor = new XorImpl();
        SendMessage(message);
        /*KS = new KeySingleton(this).getInstance(this);


        text = new String(KSF.mockKeystorage().getKey("secret"));
        Log.d(LOG_TAG, "text: " + text);

        Log.d(LOG_TAG, "keyWord: " + keyWord);

        if (keyWord.getText().length() >= 4){

                enctext = new String (xor.encrypt(text, keyWord.getText().toString()));

                Log.d(LOG_TAG, "encText: " + enctext);

                FKSI.setKey("tfpk", enctext.getBytes());  //<==============

                SendMessage(message); // запуск activity
        }
        else {
            commentary.setText("пароль не может содержать менее 4х символов");
        }*/

    }

    public void SendMessage(String message)
    {
        Intent newIntent = new Intent(this, MainListActivity.class);
        newIntent.putExtra(EXTRA_MESSAGE, message);
        startActivity(newIntent);
        this.finish();
    }


}
