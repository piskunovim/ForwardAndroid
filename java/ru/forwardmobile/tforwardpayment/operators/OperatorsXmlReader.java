package ru.forwardmobile.tforwardpayment.operators;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.forwardmobile.tforwardpayment.operators.impl.ProcessingActionImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.ProcessorImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.ProviderImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.ProvidersEntityManagerImpl;
import ru.forwardmobile.tforwardpayment.operators.impl.RequestPropertyImpl;
import ru.forwardmobile.tforwardpayment.spp.FieldFactory;
import ru.forwardmobile.tforwardpayment.spp.IField;
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
        protected static final String   ENUM_NODE_NAME              = "enum";
        protected static final String   ENUM_ITEM_NODE_NAME         = "item";

        private ProviderImpl            provider;
        private ProcessorImpl           processor;
        private ProcessingActionImpl    processingAction;
        private RequestPropertyImpl     requestProperty;
        private Collection<IRequestProperty>    requestProperties;

        private IOperatorsXmlListener   listener = null;

        // Временные переменные для полей ввода
        private Integer field_id        = null;
        private String  field_name      = null;
        private String  field_comment   = null;
        private String  field_type      = null;
        private String  field_mask      = null;
        private Map<String,String> enum_options = null;
        private String enum_item_value  = null;

        private boolean inside_enum = false;
        private boolean inside_field = false;


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
                provider.setMaxLimit(readDouble(parser, "max"));
                provider.setMinLimit(readDouble(parser, "min"));

                //Log.i(LOGGER_TAG, "Limits " + provider.getMinLimit()
                //        + " to " + provider.getMaxLimit());
            } else

            // Старт разбора поля
            if( FIELD_NODE_NAME . equals(name) ) {

                field_id   = readInt(parser, "id");
                field_type = parser.getAttributeValue(null,"type");

                inside_field = true;

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
            } else
            // Поле с перечислением
            if( ENUM_NODE_NAME . equals(name) && inside_field) {

                enum_options = new HashMap<String, String>();
                inside_enum = true;
            } else
            if( ENUM_ITEM_NODE_NAME . equals(name) && inside_field && inside_enum) {

                enum_item_value = parser.getAttributeValue(null,"value");
            } else
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
                    if(!inside_field) {
                        provider.setName(text);
                    } else {
                        field_name = text;
                    }
                    //Log.i(LOGGER_TAG, "Name: " + provider.getName());
                } else

                if( FIELD_NODE_NAME . equals( name )) {

                    IField field = FieldFactory.getField(field_type, field_comment, field_mask, enum_options);
                    field.setId(field_id);
                    field.setName(field_name);

                    provider.addField(field);

                    field_name = null;
                    field_id = null;
                    field_comment = null;
                    field_mask = null;
                    enum_options = null;

                    inside_field = false;
                } else

                if( PROCESSOR_NODE_NAME . equals(name)) {
                    provider.setProcessor(processor);
                    processor = null;

                    //Log.i(LOGGER_TAG,"Saving processor");
                } else
                if( "name" . equals(name) && inside_field) {

                    field_name = text;
                } else
                if( "mask" . equals(name) && inside_field) {

                    field_mask = text;
                } else
                if( "comment" . equals(name) && inside_field) {

                    field_comment = text.replaceAll("\\[/?\\w+\\]","");
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
                } else
                if( ENUM_ITEM_NODE_NAME . equals(name) && inside_enum ) {
                    enum_options.put(enum_item_value, text.trim());
                } else
                if(ENUM_NODE_NAME . equals(name)) {
                    inside_enum = false;
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
