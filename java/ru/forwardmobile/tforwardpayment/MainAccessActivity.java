package ru.forwardmobile.tforwardpayment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.EditText;

import ru.forwardmobile.tforwardpayment.security.IXorImpl;

/**
 * Created by PiskunovI on 23.06.14.
 */
public class MainAccessActivity extends ActionBarActivity {

    final static String LOG_TAG = "TForwardPayment.MainActivityAccess";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);



        Log.d(LOG_TAG, "Activity access form started");


    }

    public void SavePass()
    {
        IXorImpl xor = new IXorImpl();
        String text = "1";
        EditText keyWord =  (EditText) findViewById(R.id.access_pass);;
        xor.encrypt(text, keyWord.getText().toString());

    }



}
