package ru.forwardmobile.tforwardpayment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gorbovi on 20.08.2014.
 */

public class DealerInfoActivity extends ActionBarActivity {

    final static String LOG_TAG = "TFORWARD.DealerInfoActivity";

    LinearLayout call;
    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dealer_info);

        Log.d(LOG_TAG, "Initialize DealerInfoActivity");

        //initialize call/number objects
        call = (LinearLayout) findViewById(R.id.callManager);
        number = (TextView) findViewById(R.id.managerCallNumber);

        //if user push "callManager" layout
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(view, number.getText().toString());
            }
        });

       //applyFonts( findViewById(R.id.activity_dealer_info) ,null);
        //applyFonts( findViewById(R.id.activity_dealer_info_flat) ,null);



    }

    protected  void applyFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "meVe0se2.ttf");

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFonts(child, fontToSet);
                }
            } else if (v instanceof TextView) {
                ((TextView)v).setTypeface(fontToSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

    private void makeCall(View view, String num){
        Intent callNumber = new Intent(Intent.ACTION_CALL);
        callNumber.setData(Uri.parse("tel:"+num));
        startActivity(callNumber);
    }

}

