package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.ExecutionException;

import ru.forwardmobile.tforwardpayment.dealer.DealerAsyncTask;
import ru.forwardmobile.tforwardpayment.dealer.DealerInfo;

/**
 * Created by gorbovi on 20.08.2014.
 */

public class DealerInfoActivity extends AbstractBaseActivity {

    final static String LOG_TAG = "TFORWARD.DealerInfoActivity";

    LinearLayout call; //блок информации о менеджере(необходим для обработки нажатия)
    TextView number;  //поле телефона менеджера
    String n;        //телефон менеджера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dealer_info);

        Log.d(LOG_TAG, "Initialize DealerInfoActivity");

        call = (LinearLayout) findViewById(R.id.callManager);
        number = (TextView) findViewById(R.id.managerCallNumber);

        //получаем данные диллера
        ViewGroup view = (ViewGroup) findViewById(R.id.activity_dealer_info);
        DealerInfo dealerInfo = new DealerInfo(view, this);
        dealerInfo.getFullInfo();

        //if user push "callManager" layout
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallManagerPressed(number.getText().toString());
            }
        });

    }

    // = Диалоговое окно "Позвонить менеджеру?" = //
    public void onCallManagerPressed(String num) {
        n = num;
        new AlertDialog.Builder(this)
                .setTitle("Позвонить?")
                .setMessage("Вы действительно хотите позвонить " + n + "?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        makeCall(n);
                    }
                }).create().show();
    }

    // = Звонок менеджеру = //
    private void makeCall(String num){
        Intent callNumber = new Intent(Intent.ACTION_CALL);
        callNumber.setData(Uri.parse("tel:"+num));
        startActivity(callNumber);
    }

}

