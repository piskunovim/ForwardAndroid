package ru.forwardmobile.tforwardpayment.messages;

import java.util.Date;

/**
 * Информационное сообщение или уведомление
 * Created by Василий Ванин on 24.09.2014.
 */
public interface IMessage {

    /** Информационное сообщение */
    public static int TYPE_INFO = 1;

    /** Уведомление */
    public static int TYPE_NOTIFY = 2;

    /**
     * Идентификатор сообщения
     * @return Integer идентификатор сообщения в бд
     */
    public Integer getId();

    /**
     * Тип сообщения
     * @return Integer тип сообщения (см. TYPE_INFO и  TYPE_NOTIFY)
     */
    public Integer getType();

    /**
     * Текст сообщения
     * @return String содержимое сообщения
     */
    public String getText();

    /**
     * Дата создания сообщения
     * @return java.util.Date
     */
    public Date regDate();
}
