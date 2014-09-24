package ru.forwardmobile.tforwardpayment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.Objects;

import ru.forwardmobile.tforwardpayment.settings.CreateSetting;
import ru.forwardmobile.tforwardpayment.settings.IGroup;
import ru.forwardmobile.tforwardpayment.settings.ISettings;

/**
 * Created by PiskunovI on 28.08.2014.
 */
public class PageSettings extends ActionBarActivity implements CreateSetting{

    final static String LOG_TAG = "TFORWARD.PageSettings";
    LinearLayout linLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Log.d(LOG_TAG, "Initialize PageSettings");

        //LinearLayout linLayout = new LinearLayout(this);
        // установим вертикальную ориентацию
        linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        // создаем LayoutParams
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // устанавливаем linLayout как корневой элемент экрана
        setContentView(linLayout, linLayoutParam);
        createBlock();

        //LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


//        Button btn = new Button(this);
//        btn.setText("Button");
//        linLayout.addView(btn, lpView);

        //applyFonts( findViewById(R.id.some_id) ,null);

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

    @Override
    public void createBlock() {
        String str[] = {"SomeHeader1", "SomeHeader2", "SomeHeader3"};
        for (int index = 0; index < str.length; index++)
        {
                    Log.d(LOG_TAG, str[index]);

        //            fillHeader(str[index]);
        }

    }

    @Override
    public void fillHeader(String header) {
        LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(this);
        tv.setText(header);
        tv.setLayoutParams(lpView);
        linLayout.addView(tv);
    }

    @Override
    public void fillAttributes(Objects[] arr) {

    }
}