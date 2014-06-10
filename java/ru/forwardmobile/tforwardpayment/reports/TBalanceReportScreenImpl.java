package ru.forwardmobile.tforwardpayment.reports;

import ru.forwardmobile.util.http.IResponse;
import ru.forwardmobile.util.http.ResponseImpl;

/**
 * Created by PiskunovI on 10.06.14.
 */
public class TBalanceReportScreenImpl {

    public TBalanceReportScreenImpl() {}
/*

    public void processResponse(IResponseSet responseSet) throws Exception {
        int balance = null;
        int limit = null;
        int total = null;

        // разбираем ответ
        try {
            if(responseSet == null || responseSet.getSize() == 0)
                throw new Exception("Пустой ответ сервера.");
            IResponse response = (IResponse)responseSet.getResponses().elementAt(0);

        } catch(Exception e) {
            throw new Exception("Ошибка запроса баланса: " + e.getMessage());
        }


        // Показываем результат
        if(balance != null)
            append(new StringItem("Текущий баланс:", balance.toCurrencyString()));
        if(limit != null)
            append(new StringItem("Кредитный лимит:", limit.toCurrencyString()));
        if(total != null)
            if(total.greaterThan(new Amount(0,0)))
                append(new StringItem("Можно израсходовать:", total.toCurrencyString()));
            else
                append(new StringItem("Прием платежей невозможен!",""));

        return this;
    }*/

}
