package ru.forwardmobile.tforwardpayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;


import ru.forwardmobile.tforwardpayment.app.SettingsLoader;
import ru.forwardmobile.tforwardpayment.dealer.DealerDataSource;
import ru.forwardmobile.tforwardpayment.operators.OperatorsLoadListener;
import ru.forwardmobile.tforwardpayment.security.CryptEngineFactory;
import ru.forwardmobile.tforwardpayment.security.IKeyStorage;
import ru.forwardmobile.tforwardpayment.security.KeySingleton;
import ru.forwardmobile.tforwardpayment.security.KeyStorageFactory;
import ru.forwardmobile.tforwardpayment.security.XorImpl;
import ru.forwardmobile.tforwardpayment.spp.PaymentQueueManager;

/**
 * Created by PiskunovI on 23.06.14.
 */
public class MainAccessActivity extends ActionBarActivity implements  View.OnClickListener {

    final static String LOG_TAG = "TFORWARD.MainActivityAccess";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

    TextView commentary;
    EditText keyWord;
    Button button;
    String message;
    private boolean isFirstRun = false;
    private final Collection<onLoadListener> loadListeners  = new HashSet<onLoadListener>();

    public MainAccessActivity(){
        // Загрузка настроек
        loadListeners.add(new SettingsLoader());
        // Запуск очереди платежей
        loadListeners.add(new PaymentQueueManager());
        // Загрузка информации об агенте
        loadListeners.add(new DealerDataSource(this));
        // Загрузка и кеширование списка операторов
        loadListeners.add(new OperatorsLoadListener());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_access);

        commentary      = (TextView)    findViewById(R.id.access_commentary);
        button          = (Button)      findViewById(R.id.access_button);
        keyWord         = (EditText)    findViewById(R.id.access_pass);

        message         = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);

        Log.d(LOG_TAG, message);


        if (message.equals("true"))
        {
            commentary.setText("*пароль доступа был введен вами при первом при первом запуске приложения");
            button.setText("Вперед");
        } else {
            commentary.setText("*в будущем этот пароль будет  \n запрашиваться приложением");
            button.setText("Подтвердить");
            isFirstRun = true;
        }

        button.setOnClickListener(this);

        Log.d(LOG_TAG, "Activity access form started");
    }

    public void authenticate(String pass) throws Exception {

        // Ключ хранится на диске, его шифрованную версию можно получить с помощью KeySingleton
        // Используется контекст приложения, потому что он доступен во все время выполнения
        byte[] encryptedKey = KeySingleton.getInstance( getApplicationContext() ).getEncKey();

        // Получаем либо ключ в BASE64 либо непонятное месево
        String plainKey = new XorImpl().decrypt(encryptedKey, pass);

        // Расшифрованная версия ключа записыватеся в IKeyStorage, чтобы она была доступна
        KeyStorageFactory.getKeyStorage( getApplicationContext() )
                .setKey(IKeyStorage.SECRET_KEY_TYPE, plainKey.getBytes());

        // Пробуем проинициализировать модуль цифровых подписей, если ключ расшифровался неверно,
        // получим исключение. При этом модуль безопасности не будет инициализирован, можно повторять
        // операцию сколько угодно раз
        CryptEngineFactory.getEngine( getApplicationContext() );
    }

    /**
     * Устанавливает пароль на вход в программу
     *
     * Важно понимать, что вход в данный метод происходит в двух случаях
     * 1. Мы только что загрузили настройки, пользователь должен ввести пароль на вход
     * 2. Пользователь захотел изменить пароль.
     * В обоих случаях у нас в памяти есть нешифрованная версия закрытого ключа, поэтому действия во
     * всех случаях одинаковые. Берем пароль, шифруем, пишем на диск.
     * @param password
     */
    public void setPassword(String password)
    {
        // Если мы находимся в этом методе, значит у нас по-любому доступен расшифрованный закрытый ключ
        // Шифруем этот ключ паролем, который ввел наш пользователь
        byte[] encryptedKey = new XorImpl().encrypt(
                    KeyStorageFactory.getKeyStorage( getApplicationContext() ).getKey(IKeyStorage.SECRET_KEY_TYPE),
                    password
               );
	   

        // Сохраняем шифрованный ключ на диск
        KeySingleton.getInstance( getApplicationContext() )
                .setEncKey( encryptedKey );

        // делаем вид, что мы авторизовались
        onAuthenticationSuccess();
    }

    // Вызывается в случае удачной авторизации
    public void onAuthenticationSuccess() {

        initializeComponents();

        Intent intent = new Intent(this, MainPageActivity.class);
        intent.putExtra(EXTRA_MESSAGE,"true");

        startActivity(intent);
        this.finish();
    }

    private void initializeComponents(){

        //@todo add SplashScreen
        Log.v(LOG_TAG, "Fire listeners");
        for(onLoadListener listener: loadListeners) {
            listener.beforeApplicationStart(this);
        }
    }

    @Override
    public void onClick(View view) {
        Log.v(LOG_TAG, "AccessClick.");
        if( !isFirstRun ) {
            try {
                // Если аутентификация не удалась, будет брошено исключение
                authenticate(((TextView) findViewById(R.id.access_pass)).getText().toString());

                // Если выполнение дошло до этой строки, все нормально
                onAuthenticationSuccess();
            } catch(Exception ex) {
                // Что-то пошло не так. Может нет ключа, может пароль неверный
                ex.printStackTrace();
                Toast.makeText(this,"Ошибка авторизации!", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            setPassword( ((TextView) findViewById(R.id.access_pass)).getText().toString() );
        }
    }



    public interface onLoadListener{
        public void beforeApplicationStart(Context context);
    }
}
