package ru.forwardmobile.tforwardpayment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by PiskunovI on 15.07.14.
 */
public class MainActivityFlat extends ActionBarActivity {

    final static String LOG_TAG = "TFORWARD.MainActivityFlat";
    public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "MainActivityFlat resumed");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);
    }
}
