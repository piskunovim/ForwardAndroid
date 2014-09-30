package ru.forwardmobile.tforwardpayment.messages;

import java.util.Collection;

/**
 * DataAccessObject сообщений
 * Created by Василий Ванин on 24.09.2014.
 */
public interface IMessageDao {

    /**
     * Поиск сообщения по идентификатору
     * @param id
     * @return
     */
    public IMessage find(Integer id);

    /**
     * Сохранение / обновление сообщения
     * @param message
     */
    public void save(IMessage message);

    /**
     * Удаление сообщения
     * @param id
     */
    public void delete(Integer id);


    /**
     * Список сохраненных сообщений
     * @return
     */
    public Collection<IMessage> getMessages();
}
