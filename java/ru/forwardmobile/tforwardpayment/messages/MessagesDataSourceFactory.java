package ru.forwardmobile.tforwardpayment.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Василий Ванин on 30.09.2014.
 */
public class MessagesDataSourceFactory {

    public static IMessageDao getMessageDao() {
        return createDummyDao();
    }

    private static IMessageDao createDummyDao() {
        return new IMessageDao() {
            @Override
            public IMessage find(Integer id) {
                return null;
            }

            @Override
            public void save(IMessage message) {

            }

            @Override
            public void delete(Integer id) {

            }

            @Override
            public Collection<IMessage> getMessages() {
                return null;
            }

            // все методы, которые выше, пока вообще не нужны, можно их даже из интерфейса выкинуть нахрен

            @Override
            public List<IMessage> getLastMessages(int limit) {

                // Ну а йа всегда возвращаю три сообщения, что хотите, то и передавайте
                List<IMessage> list = new ArrayList<IMessage>();

                // Последнее сообщение
                list.add(new IMessage() {
                    @Override
                    public Integer getId() {
                        return 1;
                    }

                    @Override
                    public Integer getType() {
                        return IMessage.TYPE_INFO;
                    }

                    @Override
                    public String getText() {
                        return "Я последнее сообщение. Напишите в меня много текста";
                    }

                    @Override
                    public Date regDate() {
                        // Какая разница, любая дата подойдет
                        return new Date();
                    }
                });


                // Предпоследнее сообщение
                list.add(new IMessage() {
                    @Override
                    public Integer getId() {
                        return 2;
                    }

                    @Override
                    public Integer getType() {
                        return IMessage.TYPE_INFO;
                    }

                    @Override
                    public String getText() {
                        return "Йа Предпоследнее сообщение. Я как последнее, только пришло чуть раньше.";
                    }

                    @Override
                    public Date regDate() {
                        // Какая разница, любая дата подойдет
                        return new Date();
                    }
                });



                // И еще одно
                list.add(new IMessage() {
                    @Override
                    public Integer getId() {
                        // Тоже без разницы
                        return 999;
                    }

                    @Override
                    public Integer getType() {
                        return IMessage.TYPE_INFO;
                    }

                    @Override
                    public String getText() {
                        return "Йа еще одно сообщение, может я тут давно, а может тоже только пришло. Да кого это волнует, всем плевать на меня, потому что я в конце.";
                    }

                    @Override
                    public Date regDate() {
                        // Какая разница, любая дата подойдет
                        return new Date();
                    }
                });

                return list;
            }
        };
    }
}
