package ru.forwardmobile.tforwardpayment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;

/**
 * Created by gorbovi on 19.08.2014.
 */
public class MainPageActivity extends ActionBarActivity {

        final static String LOG_TAG = "TFORWARD.MainPageActivity";
        public final static String EXTRA_MESSAGE = "ru.forwardmobile.tforwardpayment";

        Button button;
        String message;
        private boolean isFirstRun = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main_page);

            button          = (Button)      findViewById(R.id.access_button);

            message         = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);

            Log.d(LOG_TAG, "Initialize MainPageActivity");



        }


}
