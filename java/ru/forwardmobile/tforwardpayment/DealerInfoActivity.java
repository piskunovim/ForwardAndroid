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
        setDealerInfoBlock("Иванов Иван Иванович", "9999");
        setDealerFinanceBlock("100000","10000", "0", "213560.55", "11.43", "0.00");
        setManagerBlock("Ерошкина Оксана", "8 (918) 111-11-11", "test@forwardmobile.ru");

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

    // = Заполняем информационный блок дилера = //
    public void setDealerInfoBlock(String dealerName, String dealerPoint)
    {
     setDealerName(dealerName);
     setDealerPoint(dealerPoint);
    }

    public void setDealerName(String name){
        TextView dealerName = (TextView) findViewById(R.id.dealerName);
        dealerName.setText(name);
    }

    public void setDealerPoint(String pointNum)
    {
        TextView dealerPoint = (TextView) findViewById(R.id.pointNum);
        dealerPoint.setText(pointNum);
    }

    // = Заполняем блок дилера = //
    public void setDealerFinanceBlock(String balance, String credit, String moneyGo, String realMoney, String fee, String blockedMoney){
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

    // = Заполняем блок менеджера = //
    public void setManagerBlock(String managerName, String managerNumber, String managerEmail)
    {
        setManagerName(managerName);
        setManagerNumber(managerNumber);
        setManagerEmail(managerEmail);
    }

    public void  setManagerName(String name)
    {
        TextView managerName = (TextView) findViewById(R.id.managerFio);
        managerName.setText(name);
    }

    public void setManagerNumber(String number)
    {
        TextView managerNumber = (TextView) findViewById(R.id.managerCallNumber);
        managerNumber.setText(number);
    }

    public void setManagerEmail(String email)
    {
        TextView managerEmail = (TextView) findViewById(R.id.managerEmail);
        managerEmail.setText(email);
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

