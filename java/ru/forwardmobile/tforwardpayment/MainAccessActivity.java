package ru.forwardmobile.tforwardpayment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import ru.forwardmobile.tforwardpayment.dealer.DealerDataSource;
import ru.forwardmobile.tforwardpayment.dealer.DealerInfo;
import ru.forwardmobile.tforwardpayment.operators.OperatorsLoadListener;
import ru.forwardmobile.tforwardpayment.security.CryptEngineFactory;
import ru.forwardmobile.tforwardpayment.security.IKeyStorage;
import ru.forwardmobile.tforwardpayment.security.KeySingleton;
import ru.forwardmobile.tforwardpayment.security.KeyStorageFactory;
import ru.forwardmobile.tforwardpayment.security.XorImpl;
import ru.forwardmobile.tforwardpayment.spp.PaymentQueueManager;
import ru.forwardmobile.util.android.AbstractTask;
import ru.forwardmobile.util.android.ITaskListener;

/**
 * Created by PiskunovI on 23.06.14.
 */
public class MainAccessActivity extends ActionBarActivity implements  View.OnClickListener {

    final static String LOG_TAG = "TFORWARD.MainActivityAccess";
    private boolean isTaskRunning = false;
    ProgressDialog progressDialog = null;

    TextView commentary;
    EditText keyWord;
    Button button;

    private boolean isFirstRun = true;
    private final Collection<onLoadListener> loadListeners  = new HashSet<onLoadListener>();

    public MainAccessActivity(){
        // Запуск очереди платежей
        loadListeners.add(new PaymentQueueManager());
        // Загрузка информации об агенте
        loadListeners.add(new DealerDataSource());
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

        isFirstRun = Settings.getInt(this, Settings.isAuthenticated, 0) == 0;

        if ( !isFirstRun )
        {
            commentary.setText("*пароль доступа был введен вами при первом при первом запуске приложения");
            button.setText("Вперед");
        } else {
            commentary.setText("*в будущем этот пароль будет  \n запрашиваться приложением");
            button.setText("Подтвердить");
        }

        button.setOnClickListener(this);

        Log.d(LOG_TAG, "Activity access form started");
        if(savedInstanceState != null) {
            isTaskRunning = savedInstanceState.getBoolean("init_running");
            if(isTaskRunning) {
                showDialog();
            }
        }
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста подождите...");
        progressDialog.show();
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
        CryptEngineFactory.getEngine( this );
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

        Settings.setAuthenticationPass(password, getApplicationContext());
        onAuthenticationSuccess();
    }

    // Вызывается в случае удачной авторизации
    public void onAuthenticationSuccess() {

        AsyncTask task = new InitializeTask(this, new ITaskListener() {
            @Override
            public void onTaskFinish(Object result) {

                isTaskRunning = false;
                if(result != null) {

                    Toast.makeText(
                            MainAccessActivity.this,
                            "Ошибка при загрузке компонентов: " + result.toString(),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {

                    Intent intent = new Intent(MainAccessActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    MainAccessActivity.this.finish();
                }
            }
        });

        showDialog();
        task.execute();
        isTaskRunning = true;
    }

    @Override
    public void onClick(View view) {

        String access_pass = ((TextView) findViewById(R.id.access_pass)).getText().toString();
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
            setPassword(access_pass);
            Settings.set(this, Settings.isAuthenticated, "1");
        }
    }


    public interface onLoadListener{
        public String beforeApplicationStart(Context context);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("init_running", isTaskRunning);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        super.onDestroy();
    }

    protected class InitializeTask extends AsyncTask<Object,Object,Object> {

        private final ITaskListener listener;
        private final Context context;

        public InitializeTask(Context context, ITaskListener listener) {
            this.context = context;
            this.listener = listener;
        }

        protected void onPreExecute(){}

        @Override
        protected Object doInBackground(Object... objects) {

            for(onLoadListener listener: loadListeners) {
                Log.v(LOG_TAG, "Executing:  " + listener.getClass().getName());
                String error = null;
                while ((error = listener.beforeApplicationStart(context)) != null) {
                    return error;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            listener.onTaskFinish(o);
        }
    }
}
