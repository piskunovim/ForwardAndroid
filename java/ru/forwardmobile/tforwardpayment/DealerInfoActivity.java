package ru.forwardmobile.tforwardpayment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    LinearLayout call; //блок информации о менеджере(необходим для обработки нажатия)
    TextView number;  //телефон менеджера
    String n;        //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dealer_info);

        Log.d(LOG_TAG, "Initialize DealerInfoActivity");

        setDealerBlock("100000","10000", "0", "213560.55", "11.43", "0.00");

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

    public void setDealerBlock(String balance, String credit, String moneyGo, String realMoney, String fee, String blockedMoney){
      setDealerBalance(balance);
      setDealerCredit(credit);
      setDealerMoneyGo(moneyGo);
      setDealerRealMoney(realMoney);
      setDealerFee(fee);
      setDealerBlockedMoney(blockedMoney);
    }

    public void setDealerBalance(String text){
        TextView dealerBalance = (TextView) findViewById(R.id.dealerBalance);
        dealerBalance.setText(text + " р.");
    }

    public void setDealerCredit(String text){
        TextView dealerCredit = (TextView) findViewById(R.id.dealerKredit);
        dealerCredit.setText(text + " р.");
    }

    public void setDealerMoneyGo(String text){
        TextView dealerMoneyGo = (TextView) findViewById(R.id.dealerMoneyGo);
        dealerMoneyGo.setText(text + " р.");
    }

    public void setDealerRealMoney(String text){
        TextView dealerRealMoney = (TextView) findViewById(R.id.dealerRealMoney);
        dealerRealMoney.setText(text + " р.");
    }

    public void setDealerFee(String text){
        TextView dealerFee = (TextView) findViewById(R.id.dealerFee);
        dealerFee.setText(text + " р.");
    }

    public void setDealerBlockedMoney(String text){
        TextView dealerBlockedMoney = (TextView) findViewById(R.id.dealerBlockedMoney);
        dealerBlockedMoney.setText(text + " р.");
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

