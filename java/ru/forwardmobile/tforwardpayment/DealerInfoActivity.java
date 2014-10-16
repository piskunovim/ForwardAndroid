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

        //заполняем блоки данными
        ViewGroup view = (ViewGroup) findViewById(R.id.activity_dealer_info);
        DealerInfo dealerInfo = new DealerInfo(view);
        dealerInfo.setDealerInfoBlock("Иванов Иван Иванович", "9999");
        dealerInfo.setDealerFinanceBlock("100000","10000", "0", "213560.55", "11.43", "0.00");
        dealerInfo.setManagerBlock("Ерошкина Оксана", "8 (918) 111-11-11", "test@forwardmobile.ru");

        //initialize call/number objects
        call = (LinearLayout) findViewById(R.id.callManager);
        number = (TextView) findViewById(R.id.managerCallNumber);

        //if user push "callManager" layout
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallManagerPressed(number.getText().toString());
            }
        });

       //applyFonts( findViewById(R.id.activity_dealer_info) ,null);
       //applyFonts( findViewById(R.id.activity_dealer_info_flat) ,null);
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

