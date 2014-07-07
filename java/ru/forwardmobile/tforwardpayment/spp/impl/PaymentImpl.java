package ru.forwardmobile.tforwardpayment.spp.impl;

import ru.forwardmobile.tforwardpayment.spp.IPayment;

/**
 * Created by Mindbreaker on 08.06.14.
 */
public abstract class PaymentImpl implements IPayment {

    public static final String getErrorName(Integer errorCode) {
        if(errorCode == null) {
            return "";
        }
        switch(errorCode.intValue()) {
            case 0:	return "Без ошибок.";
            case 1: return "Неизвестная команда.";
            case 2: return "Неизвестный сертификат. Т.Точка не найдена.";
            case 3: return "Работа Т.Точки заблокирована.";
            case 4: return "Недостаточно средств на счету.";
            case 5: return "Неизвестная ПС.";
            case 6: return "Не указано назначение платежа.";
            case 7: return "Транзакция не найдена.";
            case 8: return "Недопустимая команда  в текущем состоянии. Повторите платеж.";
            case 9: return "Неизвестная операция";
            case 10: return "Не указано сумма.";
            case 11: return "Не определен дилер.";
            case 12: return "Работа дилера заблокирована.";
            case 13: return "Превышено время ожидания команды.";
            case 14: return "Не определен маршрут проведения.";
            case 15: return "Сбой базы данных.";
            case 16: return "Не настроен драйвер ВПС.";
            case 17: return "Неправильный телефон.";
            case 18: return "Недопустимое время платежа.";
            case 19: return "Неверная сумма или превышено ограничение.";
            case 20: return "Превышен лимит суммы платежа.";
            case 21: return "Платеж дублируется в течение часа.";
            case 22: return "Не определена Т.Точка.";
            case 23: return "Ошибка SMS-терминала.";
            case 24: return "Ошибка XML.";
            case 25: return "Ошибка разбора SMS.";
            case 26: return "Ошибка в драйвере ВПС.";
            case 27: return "Не разрешен интерфейс приема платежа.";
            case 28: return "Ошибка проверки номера.";
            case 29: return "Устаревшая дата отправки платежа.";
            case 30: return "Системная ошибка.";
            case 100: return "Дублированный платеж. Обратитесь в техподдержку.";
            case 101: return "Неверный ID платежа (проверка дублирования).";
            case 102: return "Неизвестный системный параметр.";
            case 105: return "Платеж в обработке - запросить статус позже.";
            case 106: return "Неизвестный сертификат.";
            case 107: return "Доступ закрыт.";
            case 1024: return "Неизвестная ошибка.";
            case 986: return "Дублирование платежа за час или запрещенный номер.";
            case 987: return "Ошибка системы.";
            case 988: return "Ошибка создания ЭЦП.";
            case 989: return "Ошибка проверки ЭЦП.";
            case 990: return "Неизвестная ошибка провайдера.";
            case 991: return "Повторный платеж.";
            case 992: return "Отсутствие средств на счете в ВПС.";
            case 993: return "Платеж не принят провайдером по неизвестной причине.";
            case 994: return "Ошибка на стороне провайдера.";
            case 995: return "Доступ запрещен.";
            case 996: return "Неправильный формат запроса.";
            case 997: return "Неверная сумма платежа, платеж не принят.";
            case 998: return "Неверный номер назначения платежа, платеж не принят.";
            case 999: return "Временно нет связи с провайдером.";
            case 200: return "Временно нет связи с сервером.";
        };

        return "Неизвестная ошибка.";
    }


    public static String statusNameEng(int status) {
        switch(status) {
            case IPayment.NEW:
                return "NEW";
            case IPayment.CHECKED:
                return "CHECKED";
            case IPayment.COMMITED:
                return "COMMITED";
            case IPayment.DONE:
                return "DONE";
            case IPayment.FAILED:
                return "FAILED";
            case IPayment.CANCELLED:
                return "CANCELLED";
        }
        return "UNKNOWN";
    }

    @Override
    public String getStatusName() {
        switch( getStatus() ) {
            case NEW:
                return isDelayed() ? "Отложен" : "В обработке (новый)";
            case CHECKED:
                return "В обработке (проверен)";
            case COMMITED:
                return "В обработке (отправлен)";
            case DONE:
                return "Проведен";
            case FAILED:
                return "Завершен ошибкой";
            case CANCELLED:
                return "Отменен";
        }
        return "Неизвестно";
    }
}
