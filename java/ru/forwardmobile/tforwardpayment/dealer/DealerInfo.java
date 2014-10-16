package ru.forwardmobile.tforwardpayment.dealer;

import android.view.ViewGroup;
import android.widget.TextView;

import ru.forwardmobile.tforwardpayment.R;

/**
 * Created by PiskunovI on 29.08.2014.
 */
public class DealerInfo{

    protected ViewGroup viewGroup;

    public DealerInfo(ViewGroup _viewGroup){
        viewGroup = _viewGroup;
    }

    // = Заполняем блок краткой информации дилера = //
    public void setDealerShotBlock(String dealerName, String dealerBalance, String dealerRealMoney, String dealerCredit)
    {
        setDealerName(dealerName);
        setDealerBalance(dealerBalance);
        setDealerRealMoney(dealerRealMoney);
        setDealerCredit(dealerCredit);
    }


    // = Заполняем информационный блок дилера = //
    public void setDealerInfoBlock(String dealerName, String dealerPoint)
    {
        setDealerName(dealerName);
        setDealerPoint(dealerPoint);
    }

    public void setDealerName(String name){
        TextView dealerName = (TextView) viewGroup.findViewById(R.id.dealerName);
        dealerName.setText(name);
    }

    public void setDealerPoint(String pointNum)
    {
        TextView dealerPoint = (TextView) viewGroup.findViewById(R.id.pointNum);
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
        TextView dealerBalance = (TextView) viewGroup.findViewById(R.id.dealerBalance);
        dealerBalance.setText(text + " р.");
    }

    public void setDealerCredit(String text){
        TextView dealerCredit = (TextView) viewGroup.findViewById(R.id.dealerKredit);
        dealerCredit.setText(text + " р.");
    }

    public void setDealerMoneyGo(String text){
        TextView dealerMoneyGo = (TextView) viewGroup.findViewById(R.id.dealerMoneyGo);
        dealerMoneyGo.setText(text + " р.");
    }

    public void setDealerRealMoney(String text){
        TextView dealerRealMoney = (TextView) viewGroup.findViewById(R.id.dealerRealMoney);
        dealerRealMoney.setText(text + " р.");
    }

    public void setDealerFee(String text){
        TextView dealerFee = (TextView) viewGroup.findViewById(R.id.dealerFee);
        dealerFee.setText(text + " р.");
    }

    public void setDealerBlockedMoney(String text){
        TextView dealerBlockedMoney = (TextView) viewGroup.findViewById(R.id.dealerBlockedMoney);
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
        TextView managerName = (TextView) viewGroup.findViewById(R.id.managerFio);
        managerName.setText(name);
    }

    public void setManagerNumber(String number)
    {
        TextView managerNumber = (TextView) viewGroup.findViewById(R.id.managerCallNumber);
        managerNumber.setText(number);
    }

    public void setManagerEmail(String email)
    {
        TextView managerEmail = (TextView) viewGroup.findViewById(R.id.managerEmail);
        managerEmail.setText(email);
    }

}
