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

        //получаем данные диллера =
        DealerAsyncTask dealerAsyncTask = new DealerAsyncTask();

        dealerAsyncTask.execute();
        String JSONString = new String();

        try {
            JSONString = dealerAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JsonObject o = new JsonParser().parse(JSONString).getAsJsonObject();

        String dealersName = o.getAsJsonPrimitive("title").toString().substring(1,o.getAsJsonPrimitive("title").toString().length()-1);
        String dealersBalance = o.getAsJsonPrimitive("account").toString().substring(1,o.getAsJsonPrimitive("account").toString().length()-1);
        String dealersCredit = o.getAsJsonPrimitive("credit").toString().substring(1,o.getAsJsonPrimitive("credit").toString().length()-1);
        String dealerRetentionAmount = o.getAsJsonPrimitive("retention_amount").toString().substring(1,o.getAsJsonPrimitive("retention_amount").toString().length()-1);
        String dealerMayExpend =  o.getAsJsonPrimitive("may_expend").toString().substring(1,o.getAsJsonPrimitive("may_expend").toString().length()-1);
        String dealerFee = o.getAsJsonPrimitive("fee").toString().substring(1,o.getAsJsonPrimitive("fee").toString().length()-1);
        String dealerSummFuftutres = o.getAsJsonPrimitive("summ_fuftutres").toString().substring(1,o.getAsJsonPrimitive("summ_fuftutres").toString().length()-1);

        String managerName = o.getAsJsonPrimitive("fio").toString().substring(1,o.getAsJsonPrimitive("fio").toString().length()-1);
        String managerPhone = o.getAsJsonPrimitive("phone").toString().substring(1,o.getAsJsonPrimitive("phone").toString().length()-1);
        String managerEmail = o.getAsJsonPrimitive("email").toString().substring(1,o.getAsJsonPrimitive("email").toString().length()-1);

        //заполняем блоки данными
        ViewGroup view = (ViewGroup) findViewById(R.id.activity_dealer_info);
        DealerInfo dealerInfo = new DealerInfo(view);
        dealerInfo.setDealerInfoBlock(dealersName, TSettings.get("pointid").toString());
        dealerInfo.setDealerFinanceBlock(dealersBalance, dealersCredit, dealerSummFuftutres, dealerMayExpend, dealerFee, dealerRetentionAmount);
        dealerInfo.setManagerBlock(managerName, managerPhone, managerEmail);

        //dealerInfo.setManagerBlock("Ерошкина Оксана", "8 (918) 111-11-11", "test@forwardmobile.ru");

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

