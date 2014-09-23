package ru.forwardmobile.tforwardpayment.operators;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.forwardmobile.tforwardpayment.operators.impl.FieldImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.ProvidersEntityManagerImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.ProcessingActionImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.ProcessorImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.ProviderImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.RequestPropertyImpl;
import ru.forwardmobile.tforwardpayment.spp.IProvider;
import ru.raketa.util.xml.AbstractXmlDeserializable;
import ru.raketa.util.xml.XmlHelper;

/**
 * Created by Василий Ванин on 25.08.2014.
 */
public class OperatorsXmlReader  {

    static final String LOGGER_TAG = "READER";

    public ProvidersEntityManagerImpl readOperators(InputStream is) throws Exception {
        ProvidersEntityManagerImpl em = new ProvidersEntityManagerImpl();
        Reader reader = new Reader(em);
        XmlHelper.parse(reader, is);

        return em;
    }


    protected class Reader extends AbstractXmlDeserializable {

        protected static final String   OPERATOR_NODE_NAME          = "operator";
        protected static final String   OPERATOR_NAME_NODE_NAME     = "name";
        protected static final String   LIMIT_NODE_NAME             = "limit";
        protected static final String   FIELD_NODE_NAME             = "field";
        protected static final String   PROCESSOR_NODE_NAME         = "processor";
        protected static final String   CHECK_NODE_NAME             = "check";
        protected static final String   PAYMENT_NODE_NAME           = "payment";
        protected static final String   STATUS_NODE_NAME            = "status";

        private ProviderImpl            provider;
        private ProcessorImpl           processor;
        private ProcessingActionImpl    processingAction;
        private RequestPropertyImpl     requestProperty;
        private FieldImpl               field;
        private Collection<IRequestProperty>    requestProperties;

        private IOperatorsXmlListener   listener = null;

        protected Reader(IOperatorsXmlListener listener) {
            this.listener = listener;
        }

        @Override
        public void onElementStart(String name, XmlPullParser parser) throws Exception {

            // Начало разбра оператора
            if( OPERATOR_NODE_NAME . equals(name) ) {
                if(provider == null) {
                    provider = new ProviderImpl();
                    provider.setId(readInt(parser, "id"));

                //    Log.i(LOGGER_TAG, "Found operator id " + provider.getId());
                }
            } else

            // Лимит по провайдеру
            if( LIMIT_NODE_NAME . equals(name) ) {
                provider.setMaxLimit( readDouble(parser, "max") );
                provider.setMinLimit(readDouble(parser, "min"));

                //Log.i(LOGGER_TAG, "Limits " + provider.getMinLimit()
                //        + " to " + provider.getMaxLimit());
            } else

            // Старт разбора поля
            if( FIELD_NODE_NAME . equals(name) ) {

                Integer id   = readInt(parser, "id");
                String  type = parser.getAttributeValue(null, "type");

                field = new FieldImpl();
                field.setId(id);
                field.setType(type);

                //Log.i(LOGGER_TAG, "New field: " + field.getId() + " - " + field.getType());
            } else

            // Старт разбора процессора
            if( PROCESSOR_NODE_NAME . equals(name) ) {

                processor = new ProcessorImpl();
                processor.setOffline( readInt(parser,"offline") > 0 );

                //Log.i(LOGGER_TAG, "New processor.");
            } else

            // Старт разбора полей процессора
            if( CHECK_NODE_NAME . equals(name) ) {

                processingAction = new ProcessingActionImpl();
                // На всякий случай
                try {
                    processingAction.setAmountValue(readDouble(parser, "amount-value"));
                }catch(Exception ex){
                    // Default value
                    processingAction.setAmountValue(10d);
                };

                requestProperties = new ArrayList<IRequestProperty>();

                //Log.i(LOGGER_TAG, "Found processor action check with value: " + processingAction.getAmountValue());
            } else

            // Старт разбора ACTION PAYMENT
            if( PAYMENT_NODE_NAME . equals(name) ) {

                processingAction = new ProcessingActionImpl();
                try {
                    int withCheck = readInt(parser, "with-check");
                    processingAction.setWithCheck(withCheck > 0);
                }catch (Exception ex){}

                requestProperties = new ArrayList<IRequestProperty>();
                //Log.i(LOGGER_TAG, "Found processor action payment.");
            } else

            // Старт разбора ACTION STATUS
            if( STATUS_NODE_NAME . equals(name)) {

                processingAction = new ProcessingActionImpl();
                requestProperties = new ArrayList<IRequestProperty>();
                //Log.i(LOGGER_TAG, "Found processor action status.");
            }
            // Старт разбора свойства запроса
            if( "request-property" . equals(name) ) {

                requestProperty = new RequestPropertyImpl();
                requestProperty.setName(parser.getAttributeValue(null, "name"));
                requestProperty.setRefField(parser.getAttributeValue(null,"field-id"));

                //Log.i(LOGGER_TAG, "New request property: " + requestProperty.getName() + " - " + requestProperty.getRefField());
            } else

            if( "group" . equals(name) ) {
                listener.onGroupStart(parser.getAttributeValue(null,"title"), readInt(parser,"id"));
            } else
            if( "operator_id" . equals(name)) {
                listener.onOperatorMenuItem(readInt(parser,"id"));
            } else
            if("fixed-text" . equals(name) && requestProperty != null) {
                requestProperty.addItem(IRequestProperty.FIXED_TEXT_ITEM_TYPE, parser.getAttributeValue(null,"id"));
            } else
            if("field-ref" . equals(name) && requestProperty != null) {
                requestProperty.addItem(IRequestProperty.FIELD_REF_ITEM_TYPE, parser.getAttributeValue(null,"id"));
            }

        }

        @Override
        public void onElementEnd(String name, String text) throws Exception {

                // Завершение чтения оператора
                if( OPERATOR_NODE_NAME . equals(name)) {
                    //Log.i(LOGGER_TAG, "Saving operator " + provider.getId());

                    listener.onNewOperator(provider);
                    provider = null;
                } else
                // Название оператора
                if( OPERATOR_NAME_NODE_NAME . equals(name) ) {
                    if(field == null) {
                        provider.setName(text);
                    } else {
                        field.setName(text);
                    }
                    //Log.i(LOGGER_TAG, "Name: " + provider.getName());
                } else

                if( FIELD_NODE_NAME . equals( name )) {

                    provider.addField(field);
                    field = null;
                } else

                if( PROCESSOR_NODE_NAME . equals(name)) {
                    provider.setProcessor(processor);
                    processor = null;

                    //Log.i(LOGGER_TAG,"Saving processor");
                } else
                if( "name" . equals(name) ) {
                    if(field != null) {
                        field.setName(text);
                    }
                } else
                if( "mask" . equals(name)) {
                    if(field != null) {
                        field.setMask(text);
                    }
                } else
                if( "comment" . equals(name)) {
                    if(field != null) {
                          field.setComment(text.replaceAll("\\[/?\\w+\\]",""));
                    }
                } else
                if( CHECK_NODE_NAME.equals(name) ) {

                    processingAction.setRequestProperties(requestProperties);

                    processor.setCheckAction(processingAction);
                    processingAction = null;
                } else
                if( PAYMENT_NODE_NAME.equals(name)) {

                    processingAction.setRequestProperties(requestProperties);

                    processor.setPaymentAction(processingAction);
                    processingAction = null;
                } else
                if( STATUS_NODE_NAME . equals(name) ) {
                    processingAction.setRequestProperties(requestProperties);
                    processor.setGetStatusAction(processingAction);
                    processingAction = null;
                } else
                if("url" . equals(name)) {

                    Pattern pattern = Pattern.compile("/(\\d+)\\.\\w{5,6}$");
                    Matcher matcher = pattern.matcher(text);
                    if( matcher.find()) {
                        //@Todo: поместить чтение operators.xml в загрузку, сделать dataSource чисто на чтение и совать его через DI-контейнер
                        processingAction.setPsId(Integer.valueOf( matcher.group(1)));
                        //Log.i(LOGGER_TAG, "Found psid for processor action. Ps:  " + processingAction.getPsId() );
                    }
                } else
                if("request-property" . equals(name)) {
                    requestProperties.add(requestProperty);
                } else
                if( "group" . equals(name) ) {
                    listener.onGroupEnd();
                }
        }
    }

    public interface IOperatorsXmlListener {
        public void onNewOperator(IProvider provider);
        public void onGroupStart(String name, Integer id);
        public void onGroupEnd();
        public void onOperatorMenuItem(Integer id);
    }
}
