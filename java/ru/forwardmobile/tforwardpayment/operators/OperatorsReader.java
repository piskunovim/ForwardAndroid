package ru.forwardmobile.tforwardpayment.operators;

import org.xmlpull.v1.XmlPullParser;

import java.util.Collection;

import ru.raketa.util.xml.IXmlDeserializable;

/**
 * Reader for operators.xml
 * Created by Vanin V on 15.08.2014.
 */
public class OperatorsReader implements IXmlDeserializable {



    @Override
    public void onElementStart(String name, XmlPullParser parser) throws Exception {

    }

    @Override
    public void onElementEnd(String name, String text) throws Exception {

    }


    protected class GroupItem {

        private Collection<OperatorItem>    providers;
        private Collection<GroupItem>       groups;
        private Integer                     id;
        private String                      title;

        public void addProvider(OperatorItem item) {
            providers.add(item);
        }

        public void addGroup(GroupItem item) {
            groups.add(item);
        }

        public Collection<OperatorItem> getProviders() {
            return providers;
        }

        public void setProviders(Collection<OperatorItem> providers) {
            this.providers = providers;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Collection<GroupItem> getGroups() {
            return groups;
        }

        public void setGroups(Collection<GroupItem> groups) {
            this.groups = groups;
        }
    }

    protected class OperatorItem {

        private Integer                 providerId;
        private String                  title;
        private Integer                 psId;
        private Collection<FieldItem>   fields;
        private Double                  amountValue;

        public Integer getProviderId() {
            return providerId;
        }

        public void setProviderId(Integer providerId) {
            this.providerId = providerId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getPsId() {
            return psId;
        }

        public void setPsId(Integer psId) {
            this.psId = psId;
        }

        public Collection<FieldItem> getFields() {
            return fields;
        }

        public void setFields(Collection<FieldItem> fields) {
            this.fields = fields;
        }

        public Double getAmountValue() {
            return amountValue;
        }

        public void setAmountValue(Double amountValue) {
            this.amountValue = amountValue;
        }
    }

    protected class FieldItem {

    }
}
