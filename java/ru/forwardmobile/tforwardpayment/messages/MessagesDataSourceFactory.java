package ru.forwardmobile.tforwardpayment.messages;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ru.forwardmobile.tforwardpayment.db.DatabaseHelper;

/**
 * Created by Василий Ванин on 30.09.2014.
 */
public class MessagesDataSourceFactory {

    public static IMessageDao getMessageDao(Context context) {
        return createDummyDao(context);
    }

    private static IMessageDao createDummyDao(Context context) {

        final Context ctx = context;

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
                DatabaseHelper sqDb = new DatabaseHelper(ctx);
                Cursor cursor = sqDb.getMessage();
                for (int i = 1; i<=3 ; i++) {
                    cursor.moveToNext();
                    final String str = cursor.getString(cursor.getColumnIndex("messageText"));
                    final Integer id = i;
                    // Добавляем сообщения
                    list.add(new IMessage() {
                        @Override
                        public Integer getId() {
                            return id;
                        }

                        @Override
                        public Integer getType() {
                            return IMessage.TYPE_INFO;
                        }



                        @Override
                        public String getText() {
                            Log.d("getMessagesText: ", str);
                            return str;
                        }

                        @Override
                        public Date regDate() {
                            // Какая разница, любая дата подойдет
                            return new Date();
                        }
                    });
                }



                return list;
            }
        };
    }
}
