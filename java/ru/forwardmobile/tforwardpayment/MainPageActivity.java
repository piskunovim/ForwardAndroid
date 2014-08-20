package ru.forwardmobile.tforwardpayment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

            applyFonts(findViewById(R.id.activity_main_page_container), null);

           /* LinearLayout DealerInfo = (LinearLayout) findViewById(R.id.dealerInfo);
            DealerInfo.OnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!onSearch){
                        cancelButton.setVisibility(View.VISIBLE);
                        searchListView.setVisibility(View.VISIBLE);
                        linearLayout01.setVisibility(View.GONE);
                        linearLayout02.setVisibility(View.GONE);
                        linearLayout03.setVisibility(View.GONE);
                        onSearch=true;
                    }
                }
            });
           */


        }

    public void enterDealerInfo(View view)
    {
         Intent intent = new Intent(this,DealerInfoActivity.class);
         startActivity(intent);
    }

    protected  void applyFonts(final View v, Typeface fontToSet)
    {
        if(fontToSet == null)
            fontToSet = Typeface.createFromAsset(getAssets(), "Magistral.TTF");

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

}

