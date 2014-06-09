package ru.forwardmobile.tforwardpayment.spp;

import java.util.Collection;
import java.util.Date;

/**
 * @author Vasiliy Vanin
 */
public interface IPayment {
    
    public static final int NEW       = 0;
    public static final int CHECKED   = 1;
    public static final int COMMITED  = 2;
    public static final int DONE      = 3;
    public static final int FAILED    = 4;
    public static final int CANCELLED = 5;    

    /** @return Integer Идентификатор записи */
    public Integer                  getId();
    public void                     setId(Integer id);

    /** @return Integer Идентификатор транзакции */
    public Integer                  getTransactionId();
    public void                     setTransactionId(Integer transactionId);

    /** @return Collection<IFieldInfo> Набор полей */
    public Collection<IFieldInfo>   getFields();

    /** @return Integer Идентификатор платежной системы */
    public Integer                  getPsid();

    /** @return Double  Сумма платежа к зачислению в рублях с копейками */
    public Double                   getValue();

    /** @return Double  Сумма платежа полная в рублях с копейками */
    public Double                   getFullValue();

    /** @return Integer Код ошибки */
    public Integer                  getErrorCode();
    public void                     setErrorCode(Integer errorCode);

    public String                   getErrorDescription();
    public void                     setErrorDescription(String errorDescription);

    /** @return Date    Начало платежа */
    public Date                     getStartDate();
    public void                     setStartDate(Date startDate);

    /** @return Date    Завершение платежа или null */
    public Date                     getFinishDate();
    public void                     setFinishDate(Date finishDate);



    /** @return IFieldInfo  Основное поле */
    public IFieldInfo               getTarget();

    /** @return Integer Статус платежа, значения в IPayment */
    public Integer                  getStatus();
    public void                     setStatus(Integer status);

    /** @return true, if payment delayed */
    public boolean                  isDelayed();

    public boolean                  isPreparedForCancelling();
    public void                     delay(int interval);
    public void                     errorDelay();

    public void                     setActive(boolean active);
    public boolean                  getActive();

    public void                     setSended(boolean sended);
    public boolean                  getSended();

    public void                     setDateOfProcess(Date dateOfProcess);
    public Date                     getDateOfProcess();

    public void                     incTryCount();
    public Integer                  getTryCount();

    public void                     incErrorRepeatCount();
    public int                      getErrorRepeatCount();

}
